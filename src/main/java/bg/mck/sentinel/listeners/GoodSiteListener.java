package bg.mck.sentinel.listeners;

import bg.mck.sentinel.service.GoodSiteService;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bg.mck.sentinel.constants.ImportantID.LOG_CHANNEL_ID;

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

            String domain = matcher.group("domain");
            if (!goodSiteService.isGoodSite(domain)) {
                event.getGuild().getTextChannelById(LOG_CHANNEL_ID).sendMessage("A user: " +event.getAuthor().getAsMention() + " used suspicious link -> " + message).queue();
                event.getMessage().delete().queue();
            }
        }
    }
}
