package bg.mck.sentinel.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "jda.bot.guild")
public class GuildProperties {

    private Map<String,String> channels;
    private List<String> guilds;

}
