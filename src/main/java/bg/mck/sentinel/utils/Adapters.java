package bg.mck.sentinel.utils;

import bg.mck.sentinel.listeners.commands.AddGoodSiteSlashCommand;
import bg.mck.sentinel.listeners.GoodSiteListener;
import bg.mck.sentinel.listeners.commands.RemoveGoodSiteSlashCommand;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Adapters {

    private static AddGoodSiteSlashCommand addGoodSiteSlashCommand;
    private static RemoveGoodSiteSlashCommand removeGoodSiteSlashCommand;
    private static GoodSiteListener goodSiteListener;

    @Autowired
    public Adapters(AddGoodSiteSlashCommand addGoodSiteSlashCommand, RemoveGoodSiteSlashCommand removeGoodSiteSlashCommand,
                    GoodSiteListener goodSiteListener) {

        Adapters.addGoodSiteSlashCommand = addGoodSiteSlashCommand;
        Adapters.removeGoodSiteSlashCommand = removeGoodSiteSlashCommand;
        Adapters.goodSiteListener = goodSiteListener;

    }

    public static List<ListenerAdapter> getAdapterListeners() {
        return List.of(addGoodSiteSlashCommand,
                removeGoodSiteSlashCommand,
                goodSiteListener);
    }
}
