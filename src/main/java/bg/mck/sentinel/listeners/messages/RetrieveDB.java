package bg.mck.sentinel.listeners.messages;

import bg.mck.sentinel.reposotories.GoodSiteRepository;
import bg.mck.sentinel.reposotories.SubDomainRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetrieveDB extends ListenerAdapter {

    private final GoodSiteRepository goodSiteRepository;
    private final SubDomainRepository subDomainRepository;

    @Autowired
    public RetrieveDB(GoodSiteRepository goodSiteRepository, SubDomainRepository subDomainRepository) {
        this.goodSiteRepository = goodSiteRepository;
        this.subDomainRepository = subDomainRepository;
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String contentRaw = event.getMessage().getContentRaw();
        if (!contentRaw.startsWith("!give")) return;

        contentRaw = contentRaw.split("\\s+")[1];
        if (contentRaw.equals("domains")) {
           event.getAuthor().openPrivateChannel()
                    .queue((privateChannel -> {
                        privateChannel.sendMessage("`" + goodSiteRepository.findAll() + "`");
                    }));

        } else if (contentRaw.equals("subdomains")) {
            event.getAuthor().openPrivateChannel()
                    .queue((privateChannel -> {
                        privateChannel.sendMessage("`" + subDomainRepository.findAll() + "`");
                    }));
        }
    }
}
