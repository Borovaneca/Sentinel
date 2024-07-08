package bg.mck.sentinel.listeners.commands;

import bg.mck.sentinel.entities.GoodSite;
import bg.mck.sentinel.service.GoodSiteService;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static bg.mck.sentinel.constants.ImportantConstants.ALL_DOMAINS_COMMAND;

@Component
public class AllGoodSiteSlashCommand extends ListenerAdapter {

    private final GoodSiteService goodSiteService;

    @Autowired
    public AllGoodSiteSlashCommand(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals(ALL_DOMAINS_COMMAND)) return;

        List<String> domains = goodSiteService.getAll().stream().map(GoodSite::getDomain)
                .toList();

        event.replyEmbeds(EmbeddedMessages.getMessageWithAllDomains(String.join("\n", domains))).setEphemeral(true).queue();
    }
}
