package bg.mck.sentinel.listeners.slashcommands;

import bg.mck.sentinel.service.SubDomainService;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AllSubDomain implements SlashCommandProcessor {

    private final SubDomainService subDomainService;


    @Value("${jda.bot.commands[4].name}")
    private String commandName;

    @Autowired
    public AllSubDomain(SubDomainService subDomainService) {
        this.subDomainService = subDomainService;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public void process(SlashCommandInteractionEvent event) {
        String allSubDomains = subDomainService.getAllSubDomains();
        event.replyEmbeds(EmbeddedMessages.getMessageWithAllDomains(allSubDomains)).setEphemeral(true).queue();
    }
}
