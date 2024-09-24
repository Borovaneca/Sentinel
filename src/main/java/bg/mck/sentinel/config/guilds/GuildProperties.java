package bg.mck.sentinel.config.guilds;

import bg.mck.sentinel.constants.GuildName;
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
    private Map<String, String> voiceChannelsToLock;
    private Map<String, String> logsChannels;
    private Map<GuildName, String> guildIdByName;

}
