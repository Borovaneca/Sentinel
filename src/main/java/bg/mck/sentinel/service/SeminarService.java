package bg.mck.sentinel.service;

import bg.mck.sentinel.config.ValuableMaterialsProperties;
import bg.mck.sentinel.entities.Seminar;
import bg.mck.sentinel.reposotories.SeminarRepository;
import bg.mck.sentinel.utils.DateChecker;
import bg.mck.sentinel.utils.EmbeddedMessages;
import bg.mck.sentinel.utils.TextMessages;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static bg.mck.sentinel.constants.Urls.SOFT_UNI_SEMINARS_URL;

@Service
public class SeminarService {

    private SeminarRepository seminarRepository;
    private JDA jda;
    private ValuableMaterialsProperties valuableMaterials;

    @Autowired
    public SeminarService(SeminarRepository seminarRepository, JDA jda, ValuableMaterialsProperties valuableMaterials) {
        this.seminarRepository = seminarRepository;
        this.jda = jda;
        this.valuableMaterials = valuableMaterials;
    }


    @PostConstruct
    public void initializeSeminarsDb() {
        if (seminarRepository.count() != 0) return;

        try {
            BufferedReader in = getConnection();
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            Document doc = Jsoup.parse(content.toString());
            Elements seminarElements = doc.select(".events-container-item");

            List<Seminar> seminars = new ArrayList<>();

            for (Element seminarElement : seminarElements) {
                if (seminars.size() >= 6) break;
                Seminar seminar = mapToSeminar(seminarElement);
                if (DateChecker.checkDateIfItsBefore(seminar.getDate())) {
                    seminars.add(seminar);
                }
            }

            seminarRepository.saveAll(seminars);
            valuableMaterials.getChannels().forEach((guild, channel) -> {
                try {
                    var guildObj = Objects.requireNonNull(jda.getGuildById(guild), "Guild not found: " + guild);
                    var channelObj = Objects.requireNonNull(guildObj.getTextChannelById(channel), "Channel not found: " + channel);

                    channelObj.sendMessage(TextMessages.getSeminarMessage())
                            .queue(success -> System.out.println("Message sent successfully to guild: " + guild + ", channel: " + channel),
                                    error -> System.err.println("Failed to send message to guild: " + guild + ", channel: " + channel + " due to: " + error.getMessage()));

                    seminars.forEach(seminar -> {
                        channelObj.sendMessageEmbeds(EmbeddedMessages.getSeminarMessage(seminar))
                                .queue(success -> System.out.println("Embed message sent successfully for seminar: " + seminar.getTitle()),
                                        error -> System.err.println("Failed to send embed message for seminar: " + seminar.getTitle() + " due to: " + error.getMessage()));
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("An error occurred while sending messages to guild: " + guild + ", channel: " + channel);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedReader getConnection() throws IOException {
        String url = SOFT_UNI_SEMINARS_URL;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Language", "bg-BG,bg;q=0.9");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    public static Seminar mapToSeminar(Element seminarElement) {
        String title = seminarElement.select(".events-container-item-header-content-title").text();
        String date = seminarElement.select(".events-container-item-body-content-date .events-container-item-body-content-value").text();
        String time = seminarElement.select(".events-container-item-body-content-hour .events-container-item-body-content-value").text();
        String lecturers = seminarElement.select(".events-container-item-body-content-lecturer .events-container-item-body-content-lecturer-value").text().replace("\n", ", ");

        String[] lecturersArray = lecturers.replace("Лекторски състав", "").trim().split("\\s+");

        StringBuilder formattedLecturers = new StringBuilder();
        for (int i = 0; i < lecturersArray.length; i += 2) {
            if (i > 0) {
                formattedLecturers.append(", ");
            }
            formattedLecturers.append(lecturersArray[i]);
            if (i + 1 < lecturersArray.length) {
                formattedLecturers.append(" ").append(lecturersArray[i + 1]);
            }
        }

        String processedLecturers = formattedLecturers.toString();

        String link = "https://softuni.bg" + seminarElement.select("a").attr("href");
        String imageUrl = seminarElement.select(".events-container-item-header-avatar-image").attr("src");

        if (imageUrl.startsWith("http://")) {
            imageUrl = imageUrl.replace("http://", "https://");
        } else if (!imageUrl.startsWith("https://")) {
            imageUrl = "https://softuni.bg" + imageUrl;
        }

        try {
            String[] urlParts = imageUrl.split("/", 4);
            if (urlParts.length == 4) {
                String encodedPath = URLEncoder.encode(urlParts[3], "UTF-8").replace("+", "%20");
                imageUrl = urlParts[0] + "//" + urlParts[2] + "/" + encodedPath; // Rebuild the URL
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new Seminar()
                .setTitle(title)
                .setDate(date)
                .setTime(time)
                .setLecturers(processedLecturers)
                .setLink(link)
                .setImageUrl(imageUrl);
    }
}
