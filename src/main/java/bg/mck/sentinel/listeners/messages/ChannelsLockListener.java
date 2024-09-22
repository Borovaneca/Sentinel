package bg.mck.sentinel.listeners.messages;

import bg.mck.sentinel.config.GuildProperties;
import bg.mck.sentinel.service.ChannelsService;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ChannelsLockListener extends ListenerAdapter {


    @Autowired
    private GuildProperties guildProperties;


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("lock")) return;

        Member member = event.getMember();
        if (!Objects.requireNonNull(member).hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("You don't have permission to use this button!").setEphemeral(true).queue();
            return;
        }

        Guild guild = event.getGuild();
        ChannelsService channelsService = new ChannelsService(event.getJDA(), guildProperties);

        event.deferReply(true)
                .queue(interactionHook -> {
                    interactionHook.sendMessage("Locking channels... This might take a while.")
                            .setEphemeral(true)
                            .queue();

                    new Thread(() -> {
                        try {
                            channelsService.lockChannels(guild, true);
                            interactionHook.sendMessage("Channels have been successfully locked!")
                                    .setEphemeral(true)  // Make this message ephemeral
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
    }
}
