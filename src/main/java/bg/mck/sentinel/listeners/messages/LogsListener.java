package bg.mck.sentinel.listeners.messages;

import bg.mck.sentinel.config.GuildProperties;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Objects;

@Component
public class LogsListener extends ListenerAdapter {

    private final GuildProperties guildProperties;

    @Autowired
    public LogsListener(GuildProperties guildProperties) {
        this.guildProperties = guildProperties;
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();

        guild.retrieveAuditLogs().type(ActionType.BAN).queue(banLog -> {
            if (!banLog.isEmpty()) {
                AuditLogEntry banEntry = banLog.get(0);

                if (banEntry.getTimeCreated().isAfter(OffsetDateTime.now().minusSeconds(10))) {
                    return;
                }
            }

            guild.retrieveAuditLogs().type(ActionType.KICK).queue(kickLog -> {
                if (!kickLog.isEmpty()) {
                    AuditLogEntry kickEntry = kickLog.get(0);

                    if (kickEntry.getTimeCreated().isAfter(OffsetDateTime.now().minusSeconds(10))) {
                        User moderator = kickEntry.getUser();
                        String reason = kickEntry.getReason() != null ? kickEntry.getReason() : "No reason provided";

                        Objects.requireNonNull(guild.getTextChannelById(guildProperties.getKickedChannelsLog().get(guild.getId())))
                                .sendMessageEmbeds(EmbeddedMessages.getMemberKickedOrBannedMessage(user, moderator, reason, true)).queue();
                        return;
                    }
                }

                Objects.requireNonNull(guild.getTextChannelById(guildProperties.getLeavingChannelsLog().get(guild.getId())))
                        .sendMessageEmbeds(EmbeddedMessages.getMemberLeaveMessage(user)).queue();
            });
        });
    }


        @Override
        public void onGuildBan (GuildBanEvent event){
            Guild guild = event.getGuild();
            User bannedUser = event.getUser();

            guild.retrieveAuditLogs().type(ActionType.BAN).queue(log -> {
                if (!log.isEmpty()) {
                    AuditLogEntry entry = log.get(0);

                    if (entry.getTimeCreated().isAfter(OffsetDateTime.now().minusSeconds(10))) {
                        User moderator = entry.getUser();
                        String reason = entry.getReason() != null ? entry.getReason() : "No reason provided";

                        Objects.requireNonNull(guild.getTextChannelById(guildProperties.getBanChannelsLog().get(guild.getId())))
                                .sendMessageEmbeds(EmbeddedMessages.getMemberKickedOrBannedMessage(bannedUser, moderator, reason, false)).queue();
                    }
                }
            });
        }
    }
