package bg.mck.sentinel.config;

import bg.mck.sentinel.listeners.EventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static bg.mck.sentinel.constants.Identification.*;

@Configuration
public class JdaConfig {

    @Autowired
    private CommandProperties commandProperties;

    @Autowired
    private GuildProperties guildProperties;

    @Autowired
    private List<EventListener> listeners;

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

        listeners.forEach(builder::addEventListeners);

        JDA jda = builder.build().awaitReady();

        guildProperties.getGuilds()
                .forEach(
                        guildId ->
                                jda.getGuildById(guildId)
                                        .updateCommands()
                                        .addCommands(commandProperties.getCommands()).queue()
                );



        return jda;
    }
}
