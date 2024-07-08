package bg.mck.sentinel.listeners.commands;

import bg.mck.sentinel.service.GoodSiteService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bg.mck.sentinel.constants.ImportantConstants.*;
import static bg.mck.sentinel.constants.Replies.DOMAIN_DOES_NOT_EXIST;
import static bg.mck.sentinel.constants.Replies.REMOVE_DOMAIN_SUCCESS;

@Component
public class RemoveGoodSiteSlashCommand extends ListenerAdapter {

    private final GoodSiteService goodSiteService;

    @Autowired
    public RemoveGoodSiteSlashCommand(GoodSiteService goodSiteService) {
        this.goodSiteService = goodSiteService;
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals(REMOVE_SITE_COMMAND)) return;

        OptionMapping domain = event.getOption(OPTION_DOMAIN);


        if (goodSiteService.findByDomain(domain.getAsString()) == null) {
            event.reply(String.format(DOMAIN_DOES_NOT_EXIST, domain.getAsString())).queue();
            return;
        }

        goodSiteService.remove(domain.getAsString());
        event.reply(String.format(REMOVE_DOMAIN_SUCCESS, domain.getAsString())).queue();
    }
}