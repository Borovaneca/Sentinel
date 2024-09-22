package bg.mck.sentinel.service;

import bg.mck.sentinel.config.GuildProperties;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;

@Service
public class ChannelsService {


    private final JDA jda;

    private GuildProperties guildProperties;

    public ChannelsService(JDA jda, GuildProperties guildProperties) {
        this.jda = jda;
        this.guildProperties = guildProperties;
    }


    public void lockChannels(Guild guild, boolean byModerator) {

        if (guild == null) return;

        List<String> textCategories = guildProperties.getTextChannelsToLock().get(guild.getId());
        String voiceCategory = guildProperties.getVoiceChannelsToLock().get(guild.getId());

        lockTextCategories(guild, textCategories);
        lockVoiceCategories(guild, voiceCategory);

        if (byModerator) return;
        TextChannel channel = jda.getTextChannelById(guildProperties.getLogsChannels().get(guild.getId()));
        channel.sendMessageEmbeds(EmbeddedMessages.getChannelsClosedLogMessage()).queue();
    }

    public void unlockChannels(Guild guild, boolean byModerator) {
        if (guild == null) return;

        List<String> textCategories = guildProperties.getTextChannelsToLock().get(guild.getId());
        String voiceCategory = guildProperties.getVoiceChannelsToLock().get(guild.getId());

        unlockTextChannels(guild, textCategories);
        unlockVoiceChannels(guild, voiceCategory);

        if (byModerator) return;
        TextChannel channel = jda.getTextChannelById(guildProperties.getLogsChannels().get(guild.getId()));
        channel.sendMessageEmbeds(EmbeddedMessages.getChannelsOpenedLogMessage()).queue();
    }


    private void lockTextCategories(Guild guild, List<String> textCategoriesIds) {

        Role everyOneRole = guild.getPublicRole();
        for (String categoryId : textCategoriesIds) {
            Category category = guild.getCategoryById(categoryId);
            category.getChannels().forEach(channel -> {
                if (channel.getType() == ChannelType.TEXT) {
                    TextChannel textChannel = (TextChannel) channel;
                    textChannel.getManager().putPermissionOverride(everyOneRole, null, EnumSet.of(Permission.MESSAGE_SEND)).queue();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    textChannel.getManager().putPermissionOverride(guild.getPublicRole(), EnumSet.of(Permission.VIEW_CHANNEL), EnumSet.of(Permission.MESSAGE_SEND)).queue();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (channel.getType() == ChannelType.NEWS) {
                    NewsChannel newsChannel = (NewsChannel) channel;
                    newsChannel.getManager().putPermissionOverride(everyOneRole, null, EnumSet.of(Permission.MESSAGE_SEND)).queue();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    newsChannel.getManager().putPermissionOverride(guild.getPublicRole(), EnumSet.of(Permission.VIEW_CHANNEL), EnumSet.of(Permission.MESSAGE_SEND)).queue();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private void lockVoiceCategories(Guild guild, String voiceCategory) {

        Category category = guild.getCategoryById(voiceCategory);
        category.getChannels().forEach(channel -> {
            if (channel.getType() == ChannelType.VOICE) {
                VoiceChannel voiceChannel = (VoiceChannel) channel;
                voiceChannel.getManager().setUserLimit(1).queue();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void unlockTextChannels(Guild guild, List<String> textCategories) {
        Role everyRole = guild.getPublicRole();
        for (String categoryId : textCategories) {
            guild.getCategoryById(categoryId).getChannels().forEach(channel -> {
                if (channel instanceof TextChannel textChannel) {
                    textChannel.getManager().removePermissionOverride(everyRole).queue();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (channel instanceof NewsChannel newsChannel) {
                    newsChannel.getManager().removePermissionOverride(everyRole).queue();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private void unlockVoiceChannels(Guild guild, String categoryId) {
        guild.getCategoryById(categoryId).getChannels().forEach(channel -> {
            if (channel instanceof VoiceChannel voiceChannel) {
                voiceChannel.getManager().setUserLimit(50).queue();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
