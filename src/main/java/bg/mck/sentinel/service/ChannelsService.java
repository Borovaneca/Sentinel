package bg.mck.sentinel.service;

import bg.mck.sentinel.config.guilds.CategoryProperties;
import bg.mck.sentinel.config.guilds.GuildProperties;
import bg.mck.sentinel.config.guilds.RoleProperties;
import bg.mck.sentinel.constants.BasicsRole;
import bg.mck.sentinel.constants.CategoryName;
import bg.mck.sentinel.constants.FundamentalsRole;
import bg.mck.sentinel.constants.GuildName;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;

@Service
public class ChannelsService {


    private final JDA jda;
    private GuildProperties guildProperties;
    private RoleProperties roleProperties;
    private CategoryProperties categoryProperties;

    public ChannelsService(JDA jda, GuildProperties guildProperties, RoleProperties roleProperties, CategoryProperties categoryProperties) {
        this.jda = jda;
        this.guildProperties = guildProperties;
        this.roleProperties = roleProperties;
        this.categoryProperties = categoryProperties;
    }


    public void lockChannels(Guild guild, boolean byModerator) {

        if (guild == null) return;

        if (guild.getId().equals(guildProperties.getGuildIdByName().get(GuildName.BASICS))) {
            lockBasics(guild);
        } else if (guild.getId().equals(guildProperties.getGuildIdByName().get(GuildName.FUNDAMENTALS))) {
            lockFundamentals(guild);
        }

        if (byModerator) return;
        TextChannel channel = jda.getTextChannelById(guildProperties.getLogsChannels().get(guild.getId()));
        channel.sendMessageEmbeds(EmbeddedMessages.getChannelsClosedLogMessage()).queue();
    }

    public void unlockChannels(Guild guild, boolean byModerator) {
        if (guild == null) return;

        if (guild.getId().equals(guildProperties.getGuildIdByName().get(GuildName.BASICS))) {
            unlockBasics(guild);
        } else if (guild.getId().equals(guildProperties.getGuildIdByName().get(GuildName.FUNDAMENTALS))) {
            unlockFundamentals(guild);
        }

        if (byModerator) return;
        TextChannel channel = jda.getTextChannelById(guildProperties.getLogsChannels().get(guild.getId()));
        channel.sendMessageEmbeds(EmbeddedMessages.getChannelsOpenedLogMessage()).queue();
    }

    private void lockBasics(Guild guild) {
        Category javaCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.JAVA));
        Category csharpCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.CSHARP));
        Category javascriptCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.JAVASCRIPT));
        Category pythonCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.PYTHON));
        Category cplusplusCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.CPLUSPLUS));
        Category improvement = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.IMPROVEMENT));

        Role javaRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.JAVA));
        Role csharpRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.CSHARP));
        Role javascriptRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.JAVASCRIPT));
        Role pythonRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.PYTHON));
        Role cplusplusRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.CPLUSPLUS));

        EnumSet<Permission> deny = EnumSet.of(Permission.MESSAGE_SEND);
        EnumSet<Permission> allow = EnumSet.of(Permission.VIEW_CHANNEL);

        categoryProperties.getBasicsRandomChannelsToLock().forEach(channelId -> {
            guild.getTextChannelById(channelId).getManager()
                    .putPermissionOverride(javaRole, allow, deny)
                    .putPermissionOverride(csharpRole, allow, deny)
                    .putPermissionOverride(javascriptRole, allow, deny)
                    .putPermissionOverride(pythonRole, allow, deny)
                    .putPermissionOverride(cplusplusRole, allow, deny).queue();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        javaCategory.getManager().putPermissionOverride(javaRole, allow, deny).queue();
        csharpCategory.getManager().putPermissionOverride(csharpRole, allow, deny).queue();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        javascriptCategory.getManager().putPermissionOverride(javascriptRole, allow, deny).queue();
        pythonCategory.getManager().putPermissionOverride(pythonRole, allow, deny).queue();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        cplusplusCategory.getManager().putPermissionOverride(cplusplusRole, allow, deny).queue();

        improvement.getManager()
                .putPermissionOverride(javaRole, allow, deny)
                .putPermissionOverride(javascriptRole, allow, deny)
                .putPermissionOverride(csharpRole, allow, deny)
                .putPermissionOverride(pythonRole, allow, deny)
                .putPermissionOverride(cplusplusRole, allow, deny)
                .queue();

        lockVoice(guild.getCategoryById(guildProperties.getVoiceChannelsToLock().get(guild.getId())));
        syncChannels(List.of(javaCategory, javascriptCategory, pythonCategory, csharpCategory, cplusplusCategory, improvement));
    }

    private void lockFundamentals(Guild guild) {
        Category javaCategory = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.JAVA));
        Category csharpCategory = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.CSHARP));
        Category javascriptCategory = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.JAVASCRIPT));
        Category pythonCategory = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.PYTHON));
        Category common = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.COMMON));
        Category improvement = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.IMPROVEMENT));

        Role javaRole = guild.getRoleById(roleProperties.getFundamentals().get(FundamentalsRole.JAVA));
        Role csharpRole = guild.getRoleById(roleProperties.getFundamentals().get(FundamentalsRole.CSHARP));
        Role javascriptRole = guild.getRoleById(roleProperties.getFundamentals().get(FundamentalsRole.JAVASCRIPT));
        Role pythonRole = guild.getRoleById(roleProperties.getFundamentals().get(FundamentalsRole.PYTHON));

        EnumSet<Permission> deny = EnumSet.of(Permission.MESSAGE_SEND);
        EnumSet<Permission> allow = EnumSet.of(Permission.VIEW_CHANNEL);

        categoryProperties.getFundamentalsRandomChannelsToLock().forEach(channelId -> {
            guild.getTextChannelById(channelId).getManager()
                    .putPermissionOverride(javaRole, allow, deny)
                    .putPermissionOverride(csharpRole, allow, deny)
                    .putPermissionOverride(javascriptRole, allow, deny)
                    .putPermissionOverride(pythonRole, allow, deny).queue();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        javaCategory.getManager().putPermissionOverride(javaRole, allow, deny).queue();
        csharpCategory.getManager().putPermissionOverride(csharpRole, allow, deny).queue();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        javascriptCategory.getManager().putPermissionOverride(javascriptRole, allow, deny).queue();
        pythonCategory.getManager().putPermissionOverride(pythonRole, allow, deny).queue();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        common.getManager()
                .putPermissionOverride(javaRole, allow, deny)
                .putPermissionOverride(javascriptRole, allow, deny)
                .putPermissionOverride(csharpRole, allow, deny)
                .putPermissionOverride(pythonRole, allow, deny)
                .queue();

        improvement.getManager()
                .putPermissionOverride(javaRole, allow, deny)
                .putPermissionOverride(javascriptRole, allow, deny)
                .putPermissionOverride(csharpRole, allow, deny)
                .putPermissionOverride(pythonRole, allow, deny)
                .queue();

        lockVoice(guild.getCategoryById(guildProperties.getVoiceChannelsToLock().get(guild.getId())));
        syncChannels(List.of(common, javaCategory, javascriptCategory, pythonCategory, csharpCategory, improvement));
    }

    private void unlockBasics(Guild guild) {
        Category javaCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.JAVA));
        Category csharpCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.CSHARP));
        Category javascriptCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.JAVASCRIPT));
        Category pythonCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.PYTHON));
        Category cplusplusCategory = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.CPLUSPLUS));
        Category improvement = guild.getCategoryById(categoryProperties.getBasics().get(CategoryName.IMPROVEMENT));

        Role javaRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.JAVA));
        Role csharpRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.CSHARP));
        Role javascriptRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.JAVASCRIPT));
        Role pythonRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.PYTHON));
        Role cplusplusRole = guild.getRoleById(roleProperties.getBasics().get(BasicsRole.CPLUSPLUS));

        EnumSet<Permission> allow = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);

        categoryProperties.getBasicsRandomChannelsToLock().forEach(channelId -> {
            guild.getTextChannelById(channelId).getManager()
                    .putPermissionOverride(javaRole, allow, null)
                    .putPermissionOverride(csharpRole, allow, null)
                    .putPermissionOverride(javascriptRole, allow, null)
                    .putPermissionOverride(pythonRole, allow, null)
                    .putPermissionOverride(cplusplusRole, allow, null).queue();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        javaCategory.getManager().putPermissionOverride(javaRole, allow, null).queue();
        csharpCategory.getManager().putPermissionOverride(csharpRole, allow, null).queue();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        javascriptCategory.getManager().putPermissionOverride(javascriptRole, allow, null).queue();
        pythonCategory.getManager().putPermissionOverride(pythonRole, allow, null).queue();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        cplusplusCategory.getManager().putPermissionOverride(cplusplusRole, allow, null).queue();
        improvement.getManager()
                .putPermissionOverride(javaRole, allow, null)
                .putPermissionOverride(javascriptRole, allow, null)
                .putPermissionOverride(pythonRole, allow, null)
                .putPermissionOverride(csharpRole, allow, null)
                .putPermissionOverride(cplusplusRole, allow, null).queue();

        unlockVoice(guild.getCategoryById(guildProperties.getVoiceChannelsToLock().get(guild.getId())));
        syncChannels(List.of(javaCategory, javascriptCategory, pythonCategory, csharpCategory, cplusplusCategory, improvement));
    }

    private void unlockFundamentals(Guild guild) {
        Category javaCategory = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.JAVA));
        Category csharpCategory = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.CSHARP));
        Category javascriptCategory = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.JAVASCRIPT));
        Category pythonCategory = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.PYTHON));
        Category common = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.COMMON));
        Category improvement = guild.getCategoryById(categoryProperties.getFundamentals().get(CategoryName.IMPROVEMENT));

        Role javaRole = guild.getRoleById(roleProperties.getFundamentals().get(FundamentalsRole.JAVA));
        Role csharpRole = guild.getRoleById(roleProperties.getFundamentals().get(FundamentalsRole.CSHARP));
        Role javascriptRole = guild.getRoleById(roleProperties.getFundamentals().get(FundamentalsRole.JAVASCRIPT));
        Role pythonRole = guild.getRoleById(roleProperties.getFundamentals().get(FundamentalsRole.PYTHON));

        EnumSet<Permission> allow = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);

        categoryProperties.getFundamentalsRandomChannelsToLock().forEach(channelId -> {
            guild.getTextChannelById(channelId).getManager()
                    .putPermissionOverride(javaRole, allow, null)
                    .putPermissionOverride(csharpRole, allow, null)
                    .putPermissionOverride(javascriptRole, allow, null)
                    .putPermissionOverride(pythonRole, allow, null).queue();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        javaCategory.getManager().putPermissionOverride(javaRole, allow, null).queue();
        csharpCategory.getManager().putPermissionOverride(csharpRole, allow, null).queue();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        javascriptCategory.getManager().putPermissionOverride(javascriptRole, allow, null).queue();
        pythonCategory.getManager().putPermissionOverride(pythonRole, allow, null).queue();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        common.getManager()
                .putPermissionOverride(javaRole, allow, null)
                .putPermissionOverride(javascriptRole, allow, null)
                .putPermissionOverride(pythonRole, allow, null)
                .putPermissionOverride(csharpRole, allow, null)
                .queue();

        improvement.getManager()
                .putPermissionOverride(javaRole, allow, null)
                .putPermissionOverride(javascriptRole, allow, null)
                .putPermissionOverride(csharpRole, allow, null)
                .putPermissionOverride(pythonRole, allow, null)
                .queue();

        unlockVoice(guild.getCategoryById(guildProperties.getVoiceChannelsToLock().get(guild.getId())));
        syncChannels(List.of(javaCategory, common, javascriptCategory, pythonCategory, csharpCategory, improvement));
    }

    private void lockVoice(Category categoryById) {
        categoryById.getChannels().forEach(channel -> {
            if (channel instanceof VoiceChannel voice) {
                voice.getManager().setUserLimit(1).queue();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void unlockVoice(Category categoryById) {
        categoryById.getChannels().forEach(channel -> {
            if (channel instanceof VoiceChannel voice) {
                voice.getManager().setUserLimit(50).queue();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void syncChannels(List<Category> categories) {
        categories.forEach(category -> {
            category.getTextChannels().forEach(channel -> {
                channel.getManager().sync().queue();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }
}