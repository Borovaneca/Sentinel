package bg.mck.sentinel.config.guilds;

import bg.mck.sentinel.constants.CategoryName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "jda.categories")
public class CategoryProperties {

    Map<CategoryName, String> basics;
    Map<CategoryName, String> fundamentals;
    List<String> basicsRandomChannelsToLock;
    List<String> fundamentalsRandomChannelsToLock;
}
