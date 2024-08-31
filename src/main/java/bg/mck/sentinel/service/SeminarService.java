package bg.mck.sentinel.service;

import bg.mck.sentinel.config.GuildProperties;
import bg.mck.sentinel.config.ValuableMaterialsProperties;
import bg.mck.sentinel.entities.Seminar;
import bg.mck.sentinel.reposotories.SeminarRepository;
import bg.mck.sentinel.utils.EmbeddedMessages;
import bg.mck.sentinel.utils.TextMessages;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static bg.mck.sentinel.constants.Urls.SOFT_UNI_SEMINARS_URL;

@Service
public class SeminarService {

    private final GuildProperties guildProperties;
    private SeminarRepository seminarRepository;
    private JDA jda;
    private ValuableMaterialsProperties valuableMaterials;

    @Autowired
    public SeminarService(SeminarRepository seminarRepository, JDA jda, ValuableMaterialsProperties valuableMaterials, GuildProperties guildProperties) {
        this.seminarRepository = seminarRepository;
        this.jda = jda;
        this.valuableMaterials = valuableMaterials;
        this.guildProperties = guildProperties;
    }


    @PostConstruct
    public void initializeSeminarsDb() {
        if (seminarRepository.count() != 0) return;

        System.setProperty("webdriver.chrome.driver", "/home/petyo/bin/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(SOFT_UNI_SEMINARS_URL);

            Thread.sleep(5000);


            List<WebElement> seminarElements = driver.findElements(By.cssSelector(".events-container-item"));

            List<Seminar> seminars = new ArrayList<>();

            for (WebElement seminarElement : seminarElements) {
                if (seminars.size() >= 5) break;
                Seminar seminar = mapToSeminar(seminarElement);
                seminars.add(seminar);
            }

            seminarRepository.saveAll(seminars);
            valuableMaterials.getChannels().forEach((guild, channel) -> {
                Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(guild)).getTextChannelById(channel))
                        .sendMessage(TextMessages.getSeminarMessage()).queue();

                seminars.forEach(seminar -> {
                    Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(guild)).getTextChannelById(channel))
                            .sendMessageEmbeds(EmbeddedMessages.getSeminarMessage(seminar)).queue();
                });
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            driver.quit();
        }
    }

    public static Seminar mapToSeminar(WebElement seminarElement) {
        String title = seminarElement.findElement(By.cssSelector(".events-container-item-header-content-title")).getText();
        String date = seminarElement.findElement(By.cssSelector(".events-container-item-body-content-date .events-container-item-body-content-value")).getText();
        String time = seminarElement.findElement(By.cssSelector(".events-container-item-body-content-hour .events-container-item-body-content-value")).getText();
        String lecturers = seminarElement.findElement(By.cssSelector(".events-container-item-body-content-lecturer .events-container-item-body-content-lecturer-value")).getText().replace("\n", ", ");
        String link = seminarElement.findElement(By.tagName("a")).getAttribute("href");
        String imageUrl = seminarElement.findElement(By.cssSelector(".events-container-item-header-avatar-image")).getAttribute("src");

        return new Seminar()
                .setTitle(title)
                .setDate(date)
                .setTime(time)
                .setLecturers(lecturers.replace("Лекторски състав,", ""))
                .setLink(link)
                .setImageUrl(imageUrl);
    }
}
