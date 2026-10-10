package dev.messyprincy.messyJournal.inventory.config;

import dev.messyprincy.messyJournal.MessyJournal;
import dev.messyprincy.messyJournal.enums.EntryType;
import dev.messyprincy.messyJournal.interfaces.JournalEntry;
import dev.messyprincy.messyJournal.inventory.displayables.JournalFill;
import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;
import dev.messyprincy.messyJournal.logging.LoggerManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class CategoryConfigLoader {
    private static final LoggerManager LOGGER = new LoggerManager();

    public static Map<String, CategoryConfig> loadAll(Collection<String> ids) {
        Map<String, CategoryConfig> categoryConfigMap = new HashMap<>();
        for (String id : ids) {
            Optional<Path> filePath = ensureCategoryFile(id);
            if (filePath.isEmpty()) {
                categoryConfigMap.put(id, new CategoryConfig(List.of(), Map.of()));
                continue;
            }

            CategoryConfig categoryConfig = createCategoryConfig(filePath.get(), id);

            if (categoryConfig == null) {
                categoryConfigMap.put(id, new CategoryConfig(List.of(), Map.of()));
                continue;
            }

            categoryConfigMap.put(id, categoryConfig);
        }

        return categoryConfigMap;
    }

    private static Optional<Path> ensureCategoryFile(String id) {
        Optional<Path> filePath = ConfigHelper.categoryFilePath(id);

        if (filePath.isEmpty()) {
            LOGGER.errorLog("Error with creating the file path for " + id);
            return Optional.empty();
        }

        try {
            Files.createDirectories(filePath.get().getParent());
        } catch (IOException e) {
            LOGGER.errorLog("Could not create directory for categories: " + e.getMessage());
            return Optional.empty();
        }


        if (!(Files.exists(filePath.get()))) {
            try (InputStream template = MessyJournal.instance.getResource(MessyJournal.CATEGORY_TEMPLATE)) {
                if (template == null) {
                    LOGGER.errorLog("Category template could not be copied");
                    return Optional.empty();
                }
                Files.copy(template, filePath.get());
            } catch (IOException e) {
                LOGGER.errorLog("Could not create file for category " + id + " at " + filePath.get() + ": " + e.getMessage());
                return Optional.empty();
            }
        }

        return filePath;
    }

    private static CategoryConfig createCategoryConfig(Path filePath, String parentId) {
        File file = new File(filePath.toFile().toURI());
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        List<Map<?, ?>> entries = yaml.getMapList("entries");

        if (entries.isEmpty()) {
            LOGGER.errorLog("Could not find any entries in " + filePath);
            return null;
        }

        List<JournalFill> localFills = new ArrayList<>();
        Map<String, JournalEntry> localItems = new HashMap<>();

        for (int i = 0; i < entries.size(); i++) {
            Map<?, ?> entry = entries.get(i);
            String entryString = ("Entry #" + (i + 1) + " in " + parentId);

            EntryType entryType = ConfigHelper.readType(entry.get("type"));
            if (entryType == null) {
                LOGGER.errorLog(entryString + " error in type");
                continue;
            }

            if (entryType == EntryType.CATEGORY) {
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
            } else if (entryType.equals(EntryType.ITEM)) {
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

                if (localItems.containsKey(id) || parentId.equals(id)) {
                    LOGGER.errorLog(entryString + " this id already exists");
                    continue;
                }

                boolean unlockedByDefault = ConfigHelper.readBoolean(entry.get("unlockedByDefault"));

                localItems.put(id, new JournalItem(id, parsedEntry.material(), parsedEntry.name(), parsedEntry.lore(), parsedEntry.slots().getFirst(), parentId, unlockedByDefault));
            }

        }
        return new CategoryConfig(localFills, localItems);
    }
}
