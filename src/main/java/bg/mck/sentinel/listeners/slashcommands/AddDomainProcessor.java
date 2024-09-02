package bg.mck.sentinel.listeners.slashcommands;

import bg.mck.sentinel.entities.GoodSite;
import bg.mck.sentinel.service.GoodSiteService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static bg.mck.sentinel.constants.Replies.ADD_DOMAIN_SUCCESS;
import static bg.mck.sentinel.constants.Replies.DOMAIN_ALREADY_EXISTS;

@Component
public class AddDomainProcessor implements SlashCommandProcessor {

    private final GoodSiteService goodSiteService;

    @Value("${jda.bot.commands[1].name}")
    private String commandName;

    public static String OPTION_DOMAIN;

    @Autowired
    public AddDomainProcessor(GoodSiteService goodSiteService, @Value("${jda.bot.commands[1].options[0].name}") String OPTION_DOMAIN) {
        AddDomainProcessor.OPTION_DOMAIN = OPTION_DOMAIN;
        this.goodSiteService = goodSiteService;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public void process(SlashCommandInteractionEvent event) {

        OptionMapping name = event.getOption(OPTION_DOMAIN);
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
