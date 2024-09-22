package bg.mck.sentinel.controllers;

import bg.mck.sentinel.service.ChannelsService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SentinelController {

    @Autowired
    private ChannelsService channelsService;

    @Autowired
    private JDA jda;


    @PostMapping("/lock/{guildId}")
    public ResponseEntity<?> lock(@PathVariable String guildId) {
        Guild guild = jda.getGuildById(guildId);
        channelsService.lockChannels(guild, false);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unlock/{guildId}")
    public ResponseEntity<?> unlock(@PathVariable String guildId) {
        Guild guild = jda.getGuildById(guildId);
        channelsService.unlockChannels(guild, false);
        return ResponseEntity.ok().build();
    }
}
