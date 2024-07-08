package bg.mck.sentinel.listeners;

import bg.mck.sentinel.entities.GoodSite;
import bg.mck.sentinel.service.GoodSiteService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bg.mck.sentinel.constants.ImportantID.LOG_CHANNEL_ID;
import static bg.mck.sentinel.constants.ImportantID.OWNER_ID;

@Component
public class AddGoodSiteSlashCommand extends ListenerAdapter {

    private final GoodSiteService goodSiteService;

    @Autowired
    public AddGoodSiteSlashCommand(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("add-site")) return;

        OptionMapping name = event.getOption("domain");

        if (goodSiteService.findByDomain(name.getAsString()) != null) {
            event.reply("This domain is already in the list!").queue();
            return;
        }

        GoodSite goodSite = GoodSite.builder().domain(name.getAsString()).build();
        goodSiteService.save(goodSite);
        event.reply("Successfully added " + name.getAsString()).queue();
        event.getJDA().getTextChannelById(LOG_CHANNEL_ID).sendMessage("Hey guys, " + event.getMember().getAsMention() + " ADDED " + name.getAsString() + " to the good sites list!").queue();
    }
}
