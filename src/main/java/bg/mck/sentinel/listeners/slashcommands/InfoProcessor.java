package bg.mck.sentinel.listeners.slashcommands;

import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class InfoProcessor implements SlashCommandProcessor {
    @Override
    public String getCommandName() {
        return "info";
    }

    @Override
    public void process(SlashCommandInteractionEvent event) {
        event.replyEmbeds(EmbeddedMessages.getInfoMessage()).queue();
    }
}
