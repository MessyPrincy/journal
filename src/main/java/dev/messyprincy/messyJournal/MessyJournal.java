package dev.messyprincy.messyJournal;

import dev.messyprincy.messyJournal.commands.JournalCommands;
import dev.messyprincy.messyJournal.listeners.AdvancementListener;
import dev.messyprincy.messyJournal.listeners.InventoryListener;
import dev.messyprincy.messyJournal.inventory.config.JournalConfigManager;
import dev.messyprincy.messyJournal.listeners.PlayerInteractListener;
import dev.messyprincy.messyJournal.progress.ProgressUtils;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;


public final class MessyJournal extends JavaPlugin {
    public static MessyJournal instance;
    public static final String JOURNAL = "journal.yml";
    public static final String CATEGORY_TEMPLATE = "categories/_template.yml";

    @Override
    public void onEnable() {
        instance = this;

        ProgressUtils.init();

        saveResource(JOURNAL, false);
        saveResource(CATEGORY_TEMPLATE, false);

        JournalConfigManager.load();

        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new AdvancementListener(), this);
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
