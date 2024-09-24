package bg.mck.sentinel.config.guilds;

import bg.mck.sentinel.constants.BasicsRole;
import bg.mck.sentinel.constants.FundamentalsRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "jda.roles")
public class RoleProperties {

    Map<BasicsRole, String> basics;
    Map<FundamentalsRole, String> fundamentals;
}
