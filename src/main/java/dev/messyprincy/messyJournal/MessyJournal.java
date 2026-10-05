package dev.messyprincy.messyJournal;

import dev.messyprincy.messyJournal.commands.JournalCommands;
import dev.messyprincy.messyJournal.inventory.managers.ItemManager;
import dev.messyprincy.messyJournal.listeners.InventoryListener;
import dev.messyprincy.messyJournal.inventory.managers.CategoryManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;


public final class MessyJournal extends JavaPlugin {
    public static MessyJournal instance;

    @Override
    public void onEnable() {
        instance = this;
        CategoryManager.load();
        ItemManager.load();

        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(new JournalCommands().build());
        });
        getLogger().info("Meßy's Journal was enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Meßy's Journal was disabled");
    }
}
