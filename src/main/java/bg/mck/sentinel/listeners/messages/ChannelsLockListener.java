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
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ChannelsLockListener extends ListenerAdapter {

    @Autowired
    private GuildProperties guildProperties;
    @Autowired
    private RoleProperties roleProperties;
    @Autowired
    private CategoryProperties categoryProperties;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        Member member = event.getMember();
        Guild guild = event.getGuild();

        if (!Objects.requireNonNull(member).hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("You don't have permission to use this button!").setEphemeral(true).queue();
            return;
        }

        if (buttonId.equals("lock")) {
            event.reply("Are you sure you want to lock all channels?")
                    .addActionRow(
                            Button.danger("confirm_lock", "Yes"),
                            Button.secondary("cancel_lock", "No")
                    ).setEphemeral(true).queue();
            return;
        }

        if (buttonId.equals("confirm_lock")) {
            ChannelsService channelsService = new ChannelsService(event.getJDA(), guildProperties, roleProperties, categoryProperties);

            event.deferReply(true)
                    .queue(interactionHook -> {
                        interactionHook.sendMessage("Locking channels... This might take a while.")
                                .setEphemeral(true)
                                .queue();

                        new Thread(() -> {
                            try {
                                channelsService.lockChannels(guild, true);
                                interactionHook.sendMessage("Channels have been successfully locked!")
                                        .setEphemeral(true)
                                        .queue();

                                guild.getTextChannelById(guildProperties.getLogsChannels().get(guild.getId()))
                                        .sendMessageEmbeds(EmbeddedMessages.getLockByModeratorMessage(member))
                                        .queue();
                            } catch (Exception e) {
                                interactionHook.sendMessage("An error occurred while locking the channels.")
                                        .setEphemeral(true)
                                        .queue();
                            }
                        }).start();
                    });
            return;
        }

        if (buttonId.equals("cancel_lock")) {
            event.reply("Channel lock action has been cancelled.").setEphemeral(true).queue();
        }
    }
}
