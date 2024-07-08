package bg.mck.sentinel.utils;

import bg.mck.sentinel.listeners.AddGoodSiteSlashCommand;
import bg.mck.sentinel.listeners.GoodSiteListener;
import bg.mck.sentinel.listeners.RemoveGoodSiteSlashCommand;
import bg.mck.sentinel.listeners.StartingUpCommands;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Adapters {

    private static AddGoodSiteSlashCommand addGoodSiteSlashCommand;
    private static RemoveGoodSiteSlashCommand removeGoodSiteSlashCommand;
    private static StartingUpCommands startingUpCommands;
    private static GoodSiteListener goodSiteListener;

    @Autowired
    public Adapters(AddGoodSiteSlashCommand addGoodSiteSlashCommand, RemoveGoodSiteSlashCommand removeGoodSiteSlashCommand,
                    StartingUpCommands startingUpCommands, GoodSiteListener goodSiteListener) {

        Adapters.addGoodSiteSlashCommand = addGoodSiteSlashCommand;
        Adapters.removeGoodSiteSlashCommand = removeGoodSiteSlashCommand;
        Adapters.startingUpCommands = startingUpCommands;
        Adapters.goodSiteListener = goodSiteListener;

    }

    public static List<ListenerAdapter> getAdapterListeners() {
        return List.of(addGoodSiteSlashCommand,
                removeGoodSiteSlashCommand,
                startingUpCommands,
                goodSiteListener);
    }
}
