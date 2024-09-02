package bg.mck.sentinel.listeners.slashcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommandProcessor {

    String getCommandName();
    void process(SlashCommandInteractionEvent event);
}
