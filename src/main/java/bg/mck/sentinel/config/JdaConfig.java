package bg.mck.sentinel.config;

import bg.mck.sentinel.utils.Adapters;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdaConfig {

    @Bean
    public JDA startJda() {
        JDABuilder builder = JDABuilder.createDefault(System.getenv("BOT_TOKEN"))
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.MESSAGE_CONTENT);
        Adapters.getAdapterListeners().forEach(builder::addEventListeners);

        return builder.build();
    }
}
