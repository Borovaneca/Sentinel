package bg.mck.sentinel.config.punishment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "jda.bot.unprotected-channels")
public class UnProtectedChannelProperties {

    private List<String> channels;

    public boolean isChannelFreeToUseAllDomain(String channelId) {
        return channels.contains(channelId);
    }
}
