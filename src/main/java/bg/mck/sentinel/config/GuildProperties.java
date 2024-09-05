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

    private Map<String,String> removeChannelsLog;
    private List<String> guilds;
    private Map<String, String> leavingChannelsLog;
    private Map<String, String> banChannelsLog;
    private Map<String, String> kickedChannelsLog;

}
