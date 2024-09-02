package bg.mck.sentinel.listeners.slashcommands;

import bg.mck.sentinel.listeners.EventListener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Service;

import java.util.List;

import static bg.mck.sentinel.constants.Replies.COMMAND_NOT_SUPPORTED;

@Service
public class SlashCommandListener extends ListenerAdapter implements EventListener {

    private List<SlashCommandProcessor> processors;

    public SlashCommandListener(List<SlashCommandProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        processors.stream()
                .filter(processor -> processor.getCommandName().equals(event.getName()))
                .findFirst()
                .ifPresentOrElse(
                        processor -> processor.process(event),
                        () -> event.reply(COMMAND_NOT_SUPPORTED).setEphemeral(true).queue()
                );
    }
}
