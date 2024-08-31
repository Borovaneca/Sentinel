package bg.mck.sentinel.utils;

import bg.mck.sentinel.entities.Seminar;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

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

    public static MessageEmbed getSeminarMessage(Seminar seminar) {
        String imageUrl = seminar.getImageUrl().trim();

        return new EmbedBuilder()
                .setTitle(seminar.getTitle(), seminar.getLink())
                .setDescription("Онлайн събитие | Безплатно")
                .addField("Дата", seminar.getDate(), true)
                .addField("Час", seminar.getTime(), true)
                .addField("Лектори", seminar.getLecturers(), false)
                .setThumbnail(imageUrl)
                .setColor(Color.ORANGE)
                .addField("SoftUni Discord Community <:softuni:926272135255707718>", "", false)
                .build();
    }
}
