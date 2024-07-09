package bg.mck.sentinel.utils;

import java.util.List;

public class ChannelValidator {
    private static final List<String> ignoreChannelsIds = List.of(
            "1175192132785553418",
            "1169279090667303054",
            "954298971281580037",
            "1111273018346442752",
            "954298971281580040",
            "1175184880741650513",
            "1231642371842637834",
            "1164687223099228335",
            "886603663961907261",
            "1111272906111066234",
            "886269958760308757",
            "954298971281580036",
            "926476896751980544"
    );

    public static boolean isChannelFreeToUseAllDomain(String channelId) {
        return ignoreChannelsIds.stream().anyMatch(id -> id.equals(channelId));
    }
}
