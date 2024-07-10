package bg.mck.sentinel.listeners.commands;

import bg.mck.sentinel.service.GoodSiteService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bg.mck.sentinel.constants.Replies.DOMAIN_DOES_NOT_EXIST;
import static bg.mck.sentinel.constants.Replies.REMOVE_DOMAIN_SUCCESS;

@Component
public class RemoveGoodSiteProcessor implements SlashCommandProcessor {

    private final GoodSiteService goodSiteService;

    @Autowired
    public RemoveGoodSiteProcessor(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }

    @Override
    public String getCommandName() {
        return "remove-site";
    }

    @Override
    public void process(SlashCommandInteractionEvent event) {
        OptionMapping domain = event.getOption("domain");
        String domainName = domain.getAsString().toLowerCase();


        if (goodSiteService.findByDomain(domainName) == null) {
            event.reply(String.format(DOMAIN_DOES_NOT_EXIST, domainName)).setEphemeral(true).queue();
            return;
        }

        goodSiteService.remove(domainName);

        event.reply(String.format(REMOVE_DOMAIN_SUCCESS, domainName)).setEphemeral(true).queue();
    }
}
