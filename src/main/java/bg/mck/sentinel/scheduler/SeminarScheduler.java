package bg.mck.sentinel.scheduler;

import bg.mck.sentinel.config.GuildProperties;
import bg.mck.sentinel.config.ValuableMaterialsProperties;
import bg.mck.sentinel.entities.Seminar;
import bg.mck.sentinel.reposotories.SeminarRepository;
import bg.mck.sentinel.utils.EmbeddedMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static bg.mck.sentinel.constants.Urls.SOFT_UNI_SEMINARS_URL;
import static bg.mck.sentinel.service.SeminarService.mapToSeminar;
import static bg.mck.sentinel.utils.TextMessages.getSeminarMessage;

@Component
public class SeminarScheduler {

    private JDA jda;
    private ValuableMaterialsProperties valuableMaterialsChannels;
    private SeminarRepository repository;

    @Autowired
    public SeminarScheduler(JDA jda, ValuableMaterialsProperties valuableMaterialsChannels, SeminarRepository repository) {
        this.jda = jda;
        this.valuableMaterialsChannels = valuableMaterialsChannels;
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void sendDailyMessage() {
        System.setProperty("webdriver.chrome.driver", "/home/petyo/bin/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(SOFT_UNI_SEMINARS_URL);

            Thread.sleep(5000);


            List<WebElement> seminarElements = driver.findElements(By.cssSelector(".events-container-item"));

            List<Seminar> seminars = repository.findTop5ByOrderByIdDesc();

            for (WebElement seminarElement : seminarElements) {
                Seminar seminar = mapToSeminar(seminarElement);
                if (!seminars.contains(seminar)) {
                    valuableMaterialsChannels.getChannels().forEach((guild, channel) -> {
                        Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(guild)).getTextChannelById(channel))
                                .sendMessage(getSeminarMessage()).queue();

                        Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(guild)).getTextChannelById(channel))
                                .sendMessageEmbeds(EmbeddedMessages.getSeminarMessage(seminar)).queue();
                    });
                    repository.save(seminar);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
