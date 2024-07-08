package bg.mck.sentinel.listeners;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartingUpCommands extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        OptionData option = new OptionData(OptionType.STRING, "domain", "The name of the site.", true);

        commandData.add(Commands.slash("add-site", "Adds a good site to the database.")
                .addOptions(option));
        commandData.add(Commands.slash("remove-site", "Removing site from the database.").addOptions(option));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
