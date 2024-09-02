package bg.mck.sentinel.listeners.slashcommands;

import bg.mck.sentinel.entities.GoodSite;
import bg.mck.sentinel.service.GoodSiteService;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllGoodSiteProcessor implements SlashCommandProcessor {

    private final GoodSiteService goodSiteService;

    @Value("${jda.bot.commands[0].name}")
    private String commandName;

    @Autowired
    public AllGoodSiteProcessor(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public void process(SlashCommandInteractionEvent event) {
        List<String> domains = goodSiteService.getAll().stream().map(GoodSite::getDomain)
                .toList();

        event.replyEmbeds(EmbeddedMessages.getMessageWithAllDomains(String.join("\n", domains))).setEphemeral(true).queue();

    }
}
