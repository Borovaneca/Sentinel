package bg.mck.sentinel.listeners.punisments;

import bg.mck.sentinel.config.PunishmentChannels;
import bg.mck.sentinel.entities.PenalizedUser;
import bg.mck.sentinel.service.PunishmentService;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static bg.mck.sentinel.utils.EmbeddedMessages.createPunishmentInfoEmbed;

@Component
public class PunishmentSystem extends ListenerAdapter {

    private final PunishmentService punishmentService;
    private final String PUNISH_USER_BUTTON_ID = "punish";
    private final String PUNISH_USER_MODAL_ID = "punishUser";
    private final PunishmentChannels punishmentChannels;

    @Autowired
    public PunishmentSystem(PunishmentService punishmentService, PunishmentChannels punishmentChannels) {
        this.punishmentService = punishmentService;
        this.punishmentChannels = punishmentChannels;
    }


    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getGuilds()
                .forEach(guild -> {
                    TextChannel channel = Objects.requireNonNull(guild.getTextChannelById(punishmentChannels.getManagerChannels().get(guild.getId())));
                    generateMessage(channel);
                });
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!Objects.equals(event.getButton().getId(), "punish")) {
            return;
        }

        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE)) {
            Modal punishUserModal = createPunishUserModal();
            event.replyModal(punishUserModal).queue();
        } else {
            event.reply("You don't have permission to use this button!").setEphemeral(true).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals(PUNISH_USER_MODAL_ID)) {
            String userId = event.getValue("userId").getAsString();
            String reason = event.getValue("reason").getAsString();

            Member memberToPunish = event.getGuild().getMemberById(userId);

            if (memberToPunish == null) {
                event.reply("User not found in this guild! Please make sure the User ID is correct.").setEphemeral(true).queue();
                return;
            }

            PenalizedUser user = punishmentService.findByUserId(userId);
            if (user != null) {
                user.setExecutionTime(LocalDateTime.now());
                user.setPunishedBy(Objects.requireNonNull(event.getMember()).getEffectiveName());
                user.setReason(reason);
                user.setTimesHasBeenPunished(user.getTimesHasBeenPunished() + 1);
                executePunishment(user, event, memberToPunish);
            } else {
                PenalizedUser newUserToPunish = PenalizedUser.builder()
                        .punishedBy(Objects.requireNonNull(event.getMember()).getEffectiveName())
                        .executionTime(LocalDateTime.now())
                        .timesHasBeenPunished(1)
                        .reason(reason)
                        .userId(userId)
                        .asMentioned(memberToPunish.getAsMention())
                        .build();
                executePunishment(newUserToPunish, event, memberToPunish);
            }
        }
    }

    private void executePunishment(PenalizedUser user, ModalInteractionEvent event, Member memberToPunish) {
        TextChannel punishmentLogChannel = event.getGuild().getTextChannelById(punishmentChannels.getLogChannels().get(event.getGuild().getId()));

        if (punishmentLogChannel == null) {
            event.reply("Punishment log channel not found!").setEphemeral(true).queue();
            return;
        }

        switch (user.getTimesHasBeenPunished()) {
            case 1:
                event.getGuild().timeoutFor(memberToPunish, Duration.ofHours(1)).queue();
                punishmentLogChannel.sendMessageEmbeds(EmbeddedMessages.createPunishmentExecutionMessage(user, "1 hour")).queue();
                break;
            case 2:
                event.getGuild().timeoutFor(memberToPunish, Duration.ofHours(24)).queue();
                punishmentLogChannel.sendMessageEmbeds(EmbeddedMessages.createPunishmentExecutionMessage(user, "24 hours")).queue();
                break;
            case 3:
                punishmentLogChannel.sendMessageEmbeds(EmbeddedMessages.createPunishmentExecutionMessage(user, "LIFE")).queue();
                event.getGuild().ban(memberToPunish, 7, TimeUnit.DAYS).queue();
                break;
            default:
                event.reply("Invalid punishment level!").setEphemeral(true).queue();
                return;
        }

        punishmentService.save(user);
        event.reply("Punishment applied successfully!").setEphemeral(true).queue();
    }

    public Modal createPunishUserModal() {
        TextInput userIdInput = TextInput.create("userId", "User ID", TextInputStyle.SHORT)
                .setPlaceholder("Enter the User ID")
                .setRequired(true)
                .build();

        TextInput reasonInput = TextInput.create("reason", "Reason for Punishment", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Enter the reason for punishment")
                .setRequired(true)
                .build();

        return Modal.create(PUNISH_USER_MODAL_ID, "Punish User")
                .addComponents(
                        ActionRow.of(userIdInput),
                        ActionRow.of(reasonInput)
                )
                .build();
    }

    private static void generateMessage(TextChannel channel) {
        MessageEmbed embed = createPunishmentInfoEmbed();
        channel.sendMessageEmbeds(embed)
                .setActionRow(
                        Button.danger("punish", "Punishment")
                ).queue();
    }
}
