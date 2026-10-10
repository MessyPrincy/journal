package dev.messyprincy.messyJournal.inventory.config;

import dev.messyprincy.messyJournal.MessyJournal;
import dev.messyprincy.messyJournal.enums.EntryType;
import dev.messyprincy.messyJournal.interfaces.JournalEntry;
import dev.messyprincy.messyJournal.inventory.displayables.JournalCategory;
import dev.messyprincy.messyJournal.inventory.displayables.JournalFill;
import dev.messyprincy.messyJournal.logging.LoggerManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class JournalConfigManager {
    private static volatile JournalConfig journalConfig = new JournalConfig(List.of(), Map.of(), Map.of());
    private static final LoggerManager LOGGER = new LoggerManager();

    public static JournalConfig get() {
        return journalConfig;
    }

    public static CategoryConfig getCategoryConfig(String parentId) {
        return journalConfig.categoriesConfigs().getOrDefault(parentId, new CategoryConfig(List.of(), Map.of()));
    }

    public static void load() {
        File file = new File(MessyJournal.instance.getDataFolder(), MessyJournal.JOURNAL);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        List<Map<?, ?>> entries = yaml.getMapList("entries");

        if (entries.isEmpty()) {
            LOGGER.errorLog("Could not find entries in journal.yml");
            return;
        }

        List<JournalFill> localFills = new ArrayList<>();
        Map<String, JournalEntry> localCategory = new HashMap<>();

        for (int i = 0; i < entries.size(); i++) {
            Map<?, ?> entry = entries.get(i);
            String entryString = ("Entry #" + (i+1));

            EntryType entryType = ConfigHelper.readType(entry.get("type"));
            if (entryType == null) {
                LOGGER.errorLog(entryString + " error in type");
                continue;
            }

            if (entryType == EntryType.ITEM) {
                LOGGER.errorLog(entryString + " type is invalid");
                continue;
            }

            ParsedEntry parsedEntry = ConfigHelper.validateEntry(entry, entryString);
            if (parsedEntry == null) {
                continue;
            }

            if (entryType.equals(EntryType.FILL)) {
                for (int slot : parsedEntry.slots()) {
                    localFills.add(new JournalFill(parsedEntry.material(), parsedEntry.name(), parsedEntry.lore(), slot));
                }
            } else if (entryType.equals(EntryType.CATEGORY)) {
                String id = ConfigHelper.readStringStrict(entry.get("id"));

                if (id == null) {
                    LOGGER.errorLog(entryString + " error in id");
                    continue;
                }

                id = id.toLowerCase(Locale.ROOT);

                if (id.equals("exit")) {
                    LOGGER.errorLog(entryString + " id is not allowed to be exit");
                    continue;
                }

                if (journalConfig.categories().containsKey(id)) {
                    LOGGER.errorLog(entryString + " this id already exists");
                    continue;
                }

                localCategory.put(id, new JournalCategory(id, parsedEntry.material(), parsedEntry.name(), parsedEntry.lore(), parsedEntry.slots().getFirst()));
            }
        }

        Map<String, CategoryConfig> categoriesConfigs = CategoryConfigLoader.loadAll(localCategory.keySet());

        journalConfig = new JournalConfig(localFills, localCategory, categoriesConfigs);
    }
}
