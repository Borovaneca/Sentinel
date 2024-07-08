package bg.mck.sentinel.listeners;

import bg.mck.sentinel.service.GoodSiteService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bg.mck.sentinel.constants.ImportantID.LOG_CHANNEL_ID;
import static bg.mck.sentinel.constants.ImportantID.OWNER_ID;

@Component
public class RemoveGoodSiteSlashCommand extends ListenerAdapter {

    private final GoodSiteService goodSiteService;

    @Autowired
    public RemoveGoodSiteSlashCommand(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("remove-site")) return;
        if (event.getMember().getRoles().stream().noneMatch(role -> role.getId().equals(OWNER_ID))) return;


        OptionMapping domain = event.getOption("domain");

        if (domain == null) {
            event.reply("Please provide a domain!").queue();
            return;
        }

        if (goodSiteService.findByDomain(domain.getAsString()) == null) {
            event.reply("This domain is not in the list!").queue();
            return;
        }

        goodSiteService.remove(domain.getAsString());
        event.reply("Successfully removed " + domain.getAsString()).queue();
        event.getJDA().getTextChannelById(LOG_CHANNEL_ID).sendMessage("Hey guys, " + event.getMember().getAsMention() + " REMOVED " + domain.getAsString() + " from the good sites list!").queue();
    }
}
