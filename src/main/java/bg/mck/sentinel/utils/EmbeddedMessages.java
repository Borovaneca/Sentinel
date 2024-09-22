package bg.mck.sentinel.utils;

import bg.mck.sentinel.entities.PenalizedUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.util.Collection;

public class EmbeddedMessages {

    private static final String BOT_NAME = "Sentinel";
    private static final String BOT_ICON = "https://i.pinimg.com/originals/9c/09/4b/9c094b59ed5110b74f24a048e4e1c633.png";
    private static final String TITLE = "Allowed domains";
    private static final String DESCRIPTION = "Here you can see all approved domains.\n\n\n> Domains\n```\n%s\n```";
    private static final String THUMBNAIL_URL = "https://i.ibb.co/xLFyhH2/Grey-minimalist-business-project-presentation.jpg";
    private static final String MESSAGE_DOMAINS_COLOR = "#00b0f4";
    private static final String MESSAGE_DOMAINS_FOOTER = "SoftUni Discord Community";


    public static MessageEmbed getMessageWithAllDomains(String domains) {
        return new EmbedBuilder().setAuthor(BOT_NAME, null, BOT_ICON)
                .setTitle(TITLE).setDescription(String.format(DESCRIPTION, domains))
                .setThumbnail(THUMBNAIL_URL)
                .setColor(Color.decode(MESSAGE_DOMAINS_COLOR))
                .setFooter(MESSAGE_DOMAINS_FOOTER)
                .build();
    }

    public static MessageEmbed createPunishmentInfoEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("User Punishment System");
        embed.setColor(Color.RED);
        embed.setDescription("This system allows you to punish users based on the severity of their infractions. " +
                "Please provide the User ID to administer the appropriate punishment.");

        embed.addField("Punishment Levels",
                "1. **First Offense:** 1-hour timeout.\n" +
                        "2. **Second Offense:** 24-hour timeout.\n" +
                        "3. **Third Offense:** Permanent ban.",
                false);

        embed.setFooter("Use the provided button to initiate the punishment process.");

        return embed.build();
    }

    public static MessageEmbed createPunishmentExecutionMessage (PenalizedUser user, String times) {
        return new EmbedBuilder()
                .setTitle("🚫 User Punished")
                .setDescription(String.format("**%s** has been given a punishment for **%s **.\n\n**Reason:** %s\n\n**By:** %s",
                        user.getAsMentioned(),
                        times,
                        user.getReason(),
                        user.getPunishedBy()
                        ))
                .setColor(0xFF0000)
                .setFooter("Please ensure to follow the community guidelines.")
                .build();
    }

    public static MessageEmbed getInfoMessage() {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("🛡️ **Sentinel Bot Information**")
                .setDescription("Welcome to the **Sentinel Bot**! A powerful moderation and security bot for managing your server effectively.")
                .addField("Features", "• 🚫 **Automatic Punishments**\n" +
                        "• 🧰 **Advanced Moderation Tools**\n" +
                        "• 🌐 **Subdomain Management**\n" +
                        "• 🔍 **Site Whitelisting & Blacklisting**", false)
                .addField("Developed by", "👨‍💻 Developed and maintained by **<@312912204175245333>**", false)
                .setColor(0xFF4500)
                .setFooter("Stay safe and secure with Sentinel!", "https://cdn.discordapp.com/avatars/1259631076792467486/3623dda732bf4f43ce0c6b0ecd16959a.png")
                .setThumbnail("https://cdn.discordapp.com/avatars/1259631076792467486/3623dda732bf4f43ce0c6b0ecd16959a.png");

        return embed.build();
    }

    public static MessageEmbed getMemberLeaveMessage(User user) {
        return new EmbedBuilder()
                .setTitle("**" + user.getName() + "** has left the server.")
                .setDescription("User ID: " + user.getId())
                .setColor(Color.RED)
                .build();
    }

    public static MessageEmbed getMemberKickedOrBannedMessage(User kickedUser, User moderator, String reason, boolean kicked) {
        return new EmbedBuilder()
                .setTitle("**"+ kickedUser.getName() + "** has been "+ (kicked ? "KICKED" : "BANNED") +" from the server!")
                .setDescription("User ID: " + kickedUser.getId())
                .addField((kicked ? "Kicked" : "Banned") +" by: ", moderator.getAsMention(), true)
                .addField("Reason: ", reason, true)
                .setColor(Color.RED)
                .build();
    }

    public static MessageEmbed getLockByModeratorMessage(Member moderator) {
        return new EmbedBuilder()
                .setTitle(":lock: Channels Locked")
                .setColor(Color.RED)
                .setDescription("A channels has been locked by a moderator.")
                .addField("Moderator", moderator.getAsMention(), true)
                .setFooter("Lock Action Logged", null)
                .build();
    }

    public static MessageEmbed getChannelsClosedLogMessage() {
        return new EmbedBuilder()
        .setTitle("🚨 Channels Closed for Exam")
                .setDescription("""
                        The channels have been **LOCKER** for the duration of the exam.
                        Text and voice channels are now restricted until the exam is over.""")
                .setColor(Color.RED)
                .setFooter("Channels will be unlocked after the exam ends.")
                .build();
    }

    public static MessageEmbed getChannelsOpenedLogMessage() {
        return new EmbedBuilder()
                .setTitle("✅ Channels Reopened")
                .setDescription("""
                        The channels have been **UNLOCKED** after the exam.
                        Text and voice channels are now available again for all users.""")
                .setColor(Color.GREEN)
                .setFooter("Thank you for your patience during the exam period!")
                .build();
    }

    public static MessageEmbed getUnlockByModeratorMessage(Member member) {
        return new EmbedBuilder()
                .setTitle(":unlock: Channels Unlocked")
                .setColor(Color.GREEN)
                .setDescription("A channels has been unlocked by a moderator.")
                .addField("Moderator", member.getAsMention(), true)
                .setFooter("Unlock Action Logged", null)
                .build();

    }
}
