package bg.mck.sentinel.listeners.commands;

import bg.mck.sentinel.entities.SubDomain;
import bg.mck.sentinel.service.SubDomainService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static bg.mck.sentinel.constants.Replies.SUB_DOMAIN_ADD_SUCCESS;
import static bg.mck.sentinel.constants.Replies.SUB_DOMAIN_ALREADY_EXISTS;
import static bg.mck.sentinel.listeners.commands.AddDomainProcessor.OPTION_DOMAIN;

@Component
public class AddSubDomainProcessor implements SlashCommandProcessor {

    private final SubDomainService subDomainService;

    @Value("${jda.bot.commands[3].name}")
    private String commandName;

    @Autowired
    public AddSubDomainProcessor(SubDomainService subDomainService) {
        this.subDomainService = subDomainService;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public void process(SlashCommandInteractionEvent event) {
        OptionMapping name = event.getOption(OPTION_DOMAIN);
        String subDomain = name.getAsString().toLowerCase();

        String bySubDomain = subDomainService.findBySubDomain(subDomain);
        if (bySubDomain != null) {
            event.reply(String.format(SUB_DOMAIN_ALREADY_EXISTS, name.getAsString())).setEphemeral(true).queue();
            return;
        }

        subDomainService.saveSubDomain(SubDomain.builder().subDomain(subDomain).build());
        event.reply(String.format(SUB_DOMAIN_ADD_SUCCESS, subDomain)).setEphemeral(true).queue();
    }
}
