package bg.mck.sentinel.listeners.messages;

import bg.mck.sentinel.config.GuildProperties;
import bg.mck.sentinel.config.UnProtectedChannelProperties;
import bg.mck.sentinel.listeners.EventListener;
import bg.mck.sentinel.service.GoodSiteService;
import bg.mck.sentinel.service.SubDomainService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bg.mck.sentinel.constants.Messages.BAD_URL_DETECTED_MESSAGE;
import static bg.mck.sentinel.listeners.commands.AddDomainProcessor.OPTION_DOMAIN;

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
        if (event.getAuthor().isBot()) return;
        if (unProtectedChannelProperties.isChannelFreeToUseAllDomain(event.getChannel().getId())) return;

        String allSubdomains = subDomainService.getAllSubDomainsForRegex();
        String regex = "\\b(?:(?<protocol>https?)://)(?:(?<subdomain>)"+ allSubdomains +"\\.)?(?<domain>[a-zA-Z0-9.-]+)\\.(?<end>[a-zA-Z]{2,})(?:/\\S*)?\\b";
        String message = event.getMessage().getContentRaw();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);


        if (matcher.find()) {

            String domain = matcher.group(OPTION_DOMAIN).toLowerCase().replaceAll("\\.", "");
            if (!goodSiteService.isGoodSite(domain)) {
                String memberName = event.getMember().getAsMention();

                sendRemovingMessage(event.getGuild(), memberName, guildProperties.getChannels().get(event.getGuild().getId()), message);;
                event.getMember().timeoutFor(Duration.ofMillis(10000)).queue();
                event.getMessage().delete().queue();
            }
        }
    }

    private void sendRemovingMessage(Guild guild, String memberName, String channelId, String removedMessage) {
        guild.getTextChannelById(channelId).sendMessage(String.format(BAD_URL_DETECTED_MESSAGE, memberName, removedMessage)).queue();
    }
}