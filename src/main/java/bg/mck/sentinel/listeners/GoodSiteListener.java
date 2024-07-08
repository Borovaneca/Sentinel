package bg.mck.sentinel.listeners;

import bg.mck.sentinel.service.GoodSiteService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bg.mck.sentinel.constants.ImportantConstants.*;
import static bg.mck.sentinel.constants.Messages.BAD_URL_DETECTED_MESSAGE;

@Component
public class GoodSiteListener extends ListenerAdapter {

    private final GoodSiteService goodSiteService;
    private final String regex = "^(?<protocol>https?:\\/\\/)?(?<subdomain>www.)?(?<domain>\\w+)+.(?<end>[a-zA-z]{2,})";

    @Autowired
    public GoodSiteListener(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);


        if (matcher.find()) {

            String domain = matcher.group(OPTION_DOMAIN).toLowerCase();
            if (!goodSiteService.isGoodSite(domain)) {

                if (event.getGuild().getId().equals(GUILD_BASICS_ID)) {
                    sendRemovingMessage(event.getGuild(), event.getMember().getId(), LOG_CHANNEL_BASICS_ID, message);

                } else if (event.getGuild().getId().equals(GUILD_FUNDAMENTALS_ID)) {
                    sendRemovingMessage(event.getGuild(), event.getMember().getId(), LOG_CHANNEL_FUNDAMENTALS_ID, message);

                } else {
                    sendRemovingMessage(event.getGuild(), event.getMember().getId(), LOG_CHANNEL_TEST_ID, message);
                }
            }
        }
    }

    private void sendRemovingMessage(Guild guild, String memberId, String channelId, String removedMessage) {
        guild.getTextChannelById(channelId).sendMessage(String.format(BAD_URL_DETECTED_MESSAGE, guild.getMemberById(memberId).getAsMention(), removedMessage)).queue();
    }
}
