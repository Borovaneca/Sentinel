package bg.mck.sentinel.listeners.messages;

import bg.mck.sentinel.config.guilds.CategoryProperties;
import bg.mck.sentinel.config.guilds.GuildProperties;
import bg.mck.sentinel.config.guilds.RoleProperties;
import bg.mck.sentinel.service.ChannelsService;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ChannelsUnlockListener extends ListenerAdapter {

    @Autowired
    private GuildProperties guildProperties;
    @Autowired
    private RoleProperties roleProperties;
    @Autowired
    private CategoryProperties categoryProperties;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
       if (!event.getButton().getId().equals("unlock")) return;

        Member member = event.getMember();
        if (!Objects.requireNonNull(member).hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("You don't have permission to use this button!").setEphemeral(true).queue();
            return;
        }

        Guild guild = event.getGuild();
        ChannelsService channelsService = new ChannelsService(event.getJDA(), guildProperties, roleProperties, categoryProperties);
        event.deferReply(true)
                .queue(interactionHook -> {
                    interactionHook.sendMessage("Unlocking channels... Please wait.")
                            .setEphemeral(true)
                            .queue();

                    new Thread(() -> {
                        try {
                            channelsService.unlockChannels(guild, true);

                            interactionHook.sendMessage("Channels have been successfully unlocked!")
                                    .setEphemeral(true)
                                    .queue();

                            guild.getTextChannelById(guildProperties.getLogsChannels().get(guild.getId()))
                                    .sendMessageEmbeds(EmbeddedMessages.getUnlockByModeratorMessage(member))
                                    .queue();
                        } catch (Exception e) {
                            interactionHook.sendMessage("An error occurred while unlocking the channels.")
                                    .setEphemeral(true)
                                    .queue();
                        }
                    }).start();
                });
    }
}
