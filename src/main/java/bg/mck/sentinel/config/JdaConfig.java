package bg.mck.sentinel.config;

import bg.mck.sentinel.utils.Adapters;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static bg.mck.sentinel.constants.ImportantID.GUILD_ID;

@Configuration
public class JdaConfig {

    @Bean
    public JDA startJda() throws InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(System.getenv("BOT_TOKEN"))
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.MESSAGE_CONTENT);
        Adapters.getAdapterListeners().forEach(builder::addEventListeners);

        JDA jda = builder.build().awaitReady();

        List<CommandData> commandData = new ArrayList<>();

        OptionData option = new OptionData(OptionType.STRING, "domain", "The name of the site.", true);

        commandData.add(Commands.slash("add-site", "Adds a good site to the database.")
                .addOptions(option).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_PERMISSIONS)));
        commandData.add(Commands.slash("remove-site", "Removing site from the database.").addOptions(option)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_PERMISSIONS)));

        Guild guild = jda.getGuildById(GUILD_ID);
        if (guild != null) {
            guild.updateCommands().addCommands(commandData).queue();
        }


        return jda;
    }
}
