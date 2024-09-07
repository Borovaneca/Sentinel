package bg.mck.sentinel.listeners.slashcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class UserCountSlashCommand implements SlashCommandProcessor{

    @Value("${jda.bot.commands[6].name}")
    private String name;

    @Value("${jda.bot.commands[6].options[0].name}")
    private String field;


    @Override
    public String getCommandName() {
        return name;
    }

    @Override
    public void process(SlashCommandInteractionEvent event) {
        if (!event.getName().equals(name)) return;

        if (!event.isFromGuild()) {
            event.reply("This command can only be used in a server.").setEphemeral(true).queue();
            return;
        }

        OptionMapping option = event.getOption(field);
        if (option == null) {
            event.reply("Please insert a valid number!").setEphemeral(true).queue();
            return;
        }
        int days = option.getAsInt();
        long count = event.getGuild()
                .getMembers()
                .stream()
                .filter(member -> member.getTimeJoined().isAfter(OffsetDateTime.now().minusDays(days)))
                .count();

        event.reply(String.format("📅 In the last **%d** day(s), **%d** user(s) joined the server!", days, count))
                .setEphemeral(true)
                .queue();
    }
}
