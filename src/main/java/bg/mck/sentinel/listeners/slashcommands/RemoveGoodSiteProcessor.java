package bg.mck.sentinel.listeners.slashcommands;

import bg.mck.sentinel.service.GoodSiteService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static bg.mck.sentinel.constants.Replies.DOMAIN_DOES_NOT_EXIST;
import static bg.mck.sentinel.constants.Replies.REMOVE_DOMAIN_SUCCESS;
import static bg.mck.sentinel.listeners.slashcommands.AddDomainProcessor.OPTION_DOMAIN;

@Component
public class RemoveGoodSiteProcessor implements SlashCommandProcessor {

    private final GoodSiteService goodSiteService;

    @Value("${jda.bot.commands[2].name}")
    private String commandName;

    @Autowired
    public RemoveGoodSiteProcessor(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public void process(SlashCommandInteractionEvent event) {
        OptionMapping domain = event.getOption(OPTION_DOMAIN);
        String domainName = domain.getAsString().toLowerCase();


        if (goodSiteService.findByDomain(domainName) == null) {
            event.reply(String.format(DOMAIN_DOES_NOT_EXIST, domainName)).setEphemeral(true).queue();
            return;
        }

        goodSiteService.remove(domainName);

        event.reply(String.format(REMOVE_DOMAIN_SUCCESS, domainName)).setEphemeral(true).queue();
    }
}
