package bg.mck.sentinel.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "jda.bot.punishments")
public class PunishmentChannels {

    private Map<String, String> logChannels;
    private Map<String, String> managerChannels;
}
