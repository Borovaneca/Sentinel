package bg.mck.sentinel.listeners.messages;

import bg.mck.sentinel.reposotories.GoodSiteRepository;
import bg.mck.sentinel.reposotories.SubDomainRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetrieveDB extends ListenerAdapter {

    private final GoodSiteRepository goodSiteRepository;
    private final SubDomainRepository subDomainRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public RetrieveDB(GoodSiteRepository goodSiteRepository, SubDomainRepository subDomainRepository, ObjectMapper objectMapper) {
        this.goodSiteRepository = goodSiteRepository;
        this.subDomainRepository = subDomainRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String contentRaw = event.getMessage().getContentRaw();
        if (!contentRaw.startsWith("!give")) return;

        contentRaw = contentRaw.split("\\s+")[1];
        if (contentRaw.equals("domains")) {
           event.getAuthor().openPrivateChannel()
                    .queue((privateChannel -> {
                        try {
                            String jsonResponse = objectMapper.writeValueAsString(goodSiteRepository.findAll());
                            privateChannel.sendMessage("`" + jsonResponse + "`").queue();
                        } catch (JsonProcessingException e) {
                            privateChannel.sendMessage("Error processing JSON").queue();
                        }
                    }));

        } else if (contentRaw.equals("subdomains")) {
            event.getAuthor().openPrivateChannel()
                    .queue((privateChannel -> {
                        try {
                            String jsonResponse = objectMapper.writeValueAsString(subDomainRepository.findAll());
                            privateChannel.sendMessage("`" + jsonResponse + "`").queue();
                        } catch (JsonProcessingException e) {
                            privateChannel.sendMessage("Error processing JSON").queue();
                        }
                    }));
        }
    }
}
