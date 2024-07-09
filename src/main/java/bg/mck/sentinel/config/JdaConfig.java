package bg.mck.sentinel.config;

import bg.mck.sentinel.utils.Adapters;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static bg.mck.sentinel.constants.Commands.*;
import static bg.mck.sentinel.constants.Identification.*;

@Configuration
public class JdaConfig {

    @Value("${BOT_TOKEN}")
    private String BOT_TOKEN;

    @Bean
    public JDA startJda() throws InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(BOT_TOKEN)
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.competing("HOD - TEAM BLACK"))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableCache(CacheFlag.ONLINE_STATUS);
        Adapters.getAdapterListeners().forEach(builder::addEventListeners);

        JDA jda = builder.build().awaitReady();

        List<CommandData> commandData = getAllCommands();

        List.of(jda.getGuildById(GUILD_BASICS_ID), jda.getGuildById(GUILD_FUNDAMENTALS_ID),
                        jda.getGuildById(GUILD_TEST_ID))
                .forEach(guild -> guild.updateCommands().addCommands(commandData).queue());


        return jda;
    }

    private List<CommandData> getAllCommands() {
        List<CommandData> commandData = new ArrayList<>();

        OptionData option = new OptionData(OptionType.STRING, OPTION_DOMAIN, OPTION_DESCRIPTION, true);

        commandData.add(Commands.slash(ADD_SITE_COMMAND, ADD_DOMAIN_DESCRIPTION)
                .addOptions(option).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
        commandData.add(Commands.slash(REMOVE_SITE_COMMAND, REMOVE_SITE_DESCRIPTION).addOptions(option)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
        commandData.add(Commands.slash(ALL_DOMAINS_COMMAND, ALL_DOMAINS_COMMAND_DESCRIPTION)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
        return commandData;
    }
}
