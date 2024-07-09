package bg.mck.sentinel.listeners;

import bg.mck.sentinel.service.GoodSiteService;
import bg.mck.sentinel.utils.ChannelValidator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bg.mck.sentinel.constants.Commands.OPTION_DOMAIN;
import static bg.mck.sentinel.constants.Identification.*;
import static bg.mck.sentinel.constants.Messages.BAD_URL_DETECTED_MESSAGE;

@Component
public class GoodSiteListener extends ListenerAdapter {

    private final GoodSiteService goodSiteService;
    private final String regex = "^(?<protocol>https?:\\/\\/)?(?<subdomain>www.|teams.|admin.|accounts.|judge.|support.)?(?<domain>\\w+)+.(?<end>[a-zA-z]{2,})";

    @Autowired
    public GoodSiteListener(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (ChannelValidator.isChannelFreeToUseAllDomain(event.getChannel().getId())) return;

        String message = event.getMessage().getContentRaw();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);


        if (matcher.find()) {

            String domain = matcher.group(OPTION_DOMAIN).toLowerCase();
            if (!goodSiteService.isGoodSite(domain)) {
                String memberName = event.getMember().getAsMention();

                if (event.getGuild().getId().equals(GUILD_BASICS_ID)) {
                    sendRemovingMessage(event.getGuild(), memberName, LOG_CHANNEL_BASICS_ID, message);

                } else if (event.getGuild().getId().equals(GUILD_FUNDAMENTALS_ID)) {
                    sendRemovingMessage(event.getGuild(), memberName, LOG_CHANNEL_FUNDAMENTALS_ID, message);

                } else {
                    sendRemovingMessage(event.getGuild(), memberName, LOG_CHANNEL_TEST_ID, message);
                }
                event.getMessage().delete().queue();
            }
        }
    }

    private void sendRemovingMessage(Guild guild, String memberName, String channelId, String removedMessage) {
        guild.getTextChannelById(channelId).sendMessage(String.format(BAD_URL_DETECTED_MESSAGE, memberName, removedMessage)).queue();
    }
}
