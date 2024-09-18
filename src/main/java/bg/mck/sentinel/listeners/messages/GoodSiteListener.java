package bg.mck.sentinel.listeners.messages;

import bg.mck.sentinel.config.GuildProperties;
import bg.mck.sentinel.config.UnProtectedChannelProperties;
import bg.mck.sentinel.listeners.EventListener;
import bg.mck.sentinel.service.GoodSiteService;
import bg.mck.sentinel.service.SubDomainService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.EnumSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bg.mck.sentinel.constants.Messages.BAD_URL_DETECTED_MESSAGE;
import static bg.mck.sentinel.listeners.slashcommands.AddDomainProcessor.OPTION_DOMAIN;

@Component
public class GoodSiteListener extends ListenerAdapter implements EventListener {

    private final GoodSiteService goodSiteService;
    private final SubDomainService subDomainService;
    private final GuildProperties guildProperties;
    private final UnProtectedChannelProperties unProtectedChannelProperties;

    @Autowired
    public GoodSiteListener(GoodSiteService goodSiteService, SubDomainService subDomainService, GuildProperties guildProperties, UnProtectedChannelProperties unProtectedChannelProperties) {
        this.goodSiteService = goodSiteService;
        this.subDomainService = subDomainService;
        this.guildProperties = guildProperties;
        this.unProtectedChannelProperties = unProtectedChannelProperties;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        executeValidation(event.getAuthor(), event.getMember(), event.getChannel(), event.getMessage(), event.getGuild(), event);
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        executeValidation(event.getAuthor(), event.getMember(), event.getChannel(), event.getMessage(), event.getGuild(), event);
    }

    private void executeValidation(User author, Member member2, MessageChannelUnion channel, Message message2, Guild guild, GenericMessageEvent event) {
        if (author.isBot()) return;
        Member member = member2;
        if (member == null) return;
        EnumSet<Permission> permissions = member.getPermissions();
        if (permissions.contains(Permission.MESSAGE_MANAGE)) return;

        if (unProtectedChannelProperties.isChannelFreeToUseAllDomain(channel.getId())) return;

        String allSubdomains = subDomainService.getAllSubDomainsForRegex();
        String regex = "\\b(?:(?<protocol>https?)://)(?:(?<subdomain>)" + allSubdomains + "\\.)?(?<domain>[a-zA-Z0-9.-]+)\\.(?<end>[a-zA-Z]{2,})(?:/\\S*)?\\b";
        String message = message2.getContentRaw();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);


        if (matcher.find()) {

            String domain = matcher.group(OPTION_DOMAIN).toLowerCase().replaceAll("\\.", "");
            if (!goodSiteService.isGoodSite(domain)) {
                String memberName = member.getAsMention();

                sendRemovingMessage(guild, memberName, guildProperties.getRemoveChannelsLog().get(guild.getId()), event.getChannel().getAsMention(), message);
                member.timeoutFor(Duration.ofMillis(10000)).queue();
                message2.delete().queue();
            }
        }
    }

    private void sendRemovingMessage(Guild guild, String memberName, String logChannel, String actualChannel, String removedMessage) {
        Objects.requireNonNull(guild.getTextChannelById(logChannel)).sendMessage(String.format(BAD_URL_DETECTED_MESSAGE, memberName, actualChannel, removedMessage)).queue();
    }
}