package bg.mck.sentinel.listeners.commands;

import bg.mck.sentinel.entities.GoodSite;
import bg.mck.sentinel.service.GoodSiteService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bg.mck.sentinel.constants.Replies.ADD_DOMAIN_SUCCESS;
import static bg.mck.sentinel.constants.Replies.DOMAIN_ALREADY_EXISTS;

@Component
public class AddDomainProcessor implements SlashCommandProcessor {

    private final GoodSiteService goodSiteService;

    @Autowired
    public AddDomainProcessor(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }

    @Override
    public String getCommandName() {
        return "add-site";
    }

    @Override
    public void process(SlashCommandInteractionEvent event) {

        OptionMapping name = event.getOption("domain");
        String domainName = name.getAsString().toLowerCase();

        if (goodSiteService.findByDomain(domainName) != null) {
            event.reply(String.format(DOMAIN_ALREADY_EXISTS, name.getAsString())).setEphemeral(true).queue();
            return;
        }

        GoodSite goodSite = GoodSite.builder().domain(domainName).build();
        goodSiteService.save(goodSite);
        event.reply(String.format(ADD_DOMAIN_SUCCESS, domainName)).setEphemeral(true).queue();
    }
}
