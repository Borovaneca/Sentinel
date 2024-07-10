package bg.mck.sentinel.config;

import lombok.Data;
import lombok.Setter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Setter
@ConfigurationProperties(prefix = "jda.bot")
public class CommandProperties {

    private List<Command> commands;

    public List<CommandData> getCommands() {
        return commands.stream().map(Command::mapToSlashCommandData).toList();
    }

    @Data
    public static class Command{
       private String name;
       private String description;
       private boolean hasOptions;
       private List<CommandOption> options;

       public CommandData mapToSlashCommandData() {
           SlashCommandData slashCommandData = Commands.slash(name, description);
           if (hasOptions) {
               slashCommandData.addOptions(options.stream().map(CommandOption::mapToOptionData).toList());
           }
           return slashCommandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE));
       }
    }

    @Data
    public static class CommandOption {

        private String name;
        private String description;
        private boolean required;
        private String type;

        public OptionData mapToOptionData() {
            return new OptionData(OptionType.valueOf(type), name, description, required);
        }
    }
}
