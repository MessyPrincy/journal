package dev.messyprincy.messyJournal.inventory.config;

import dev.messyprincy.messyJournal.MessyJournal;
import dev.messyprincy.messyJournal.enums.DiscoverType;
import dev.messyprincy.messyJournal.enums.EntryType;
import dev.messyprincy.messyJournal.logging.LoggerManager;
import org.bukkit.NamespacedKey;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public final class ConfigHelper {
    private static final String CATEGORY_FOLDER = "categories";
    private static final String FILE_SUFFIX = ".yml";

    public static Optional<Path> categoryFilePath(String categoryId) {
        File dataFolder = MessyJournal.instance.getDataFolder();
        Path dataFolderPath = dataFolder.toPath();
        Path categoryFolderPath = dataFolderPath.resolve(CATEGORY_FOLDER);
        Path categoryFilePath = categoryFolderPath.resolve(categoryId + FILE_SUFFIX).normalize();

        if (!(categoryFilePath.startsWith(categoryFolderPath.normalize()))) {
            return Optional.empty();
        }

        return Optional.of(categoryFilePath);
    }

    public static String readString(String verified, Object object) {
        if (object == null) {
            LoggerManager.errorLog(verified + " is null");
            return null;
        }

        return String.valueOf(object);
    }

    public static String readStringStrict(String verified, Object object) {
        String string = readString(verified, object);
        if  (string == null) {
            return null;
        }

        string = string.trim();

        if (string.isEmpty()) {
            LoggerManager.errorLog(verified + " is empty");
            return null;
        }

        return string;
    }

    public static DiscoverType readDiscoverType(String verified, Object object) {
        String string = readStringStrict(verified, object);
        if (string == null) {
            return null;
        }

        string = string.toUpperCase(Locale.ROOT);

        try {
            return DiscoverType.valueOf(string);
        } catch (IllegalArgumentException _) {
            LoggerManager.errorLog(verified + ": '" + string + "' is not a valid DiscoverType");
            return null;
        }
    }

    public static EntryType readEntryType(String verified, Object object) {
        String string = readStringStrict(verified, object);

        if (string == null) {
            return null;
        }

        string = string.toUpperCase(Locale.ROOT);

        try {
            return EntryType.valueOf(string);
        } catch (IllegalArgumentException _) {
            LoggerManager.errorLog(verified + " is not a valid EntryType");
            return null;
        }
    }

    public static List<String> readList(String verified, Object object) {
        if (!(object instanceof List<?> objectList)) {
            return List.of();
        }

        List<String> list = new ArrayList<>();

        for (Object entry : objectList) {
            String string = readStringStrict(verified, entry);

            if (string == null) {
                continue;
            }

            list.add(string);
        }

        return List.copyOf(list);
    }

    public static Map<?, ?> readSection(Object object) {
        if (object instanceof Map<?, ?> map) {
            return map;
        }

        return Map.of();
    }

    public static List<Integer> readSlots(String verified, Object object) {
        if (!(object instanceof List<?> objectList)) {
            return List.of();
        }

        List<Integer> list = new ArrayList<>();

        for (Object entry : objectList) {
            String string = readStringStrict(verified, entry);

            if (string == null) {
                continue;
            }

            try {
                int integer = Integer.parseInt(string);

                if (integer > 0 && integer < 53) {
                    continue;
                }

                list.add(integer);
            } catch (NumberFormatException _) {
            }

        }

        return List.copyOf(list);
    }

    public static boolean readBoolean(Object object) {
        if (!(object instanceof Boolean bool)) {
            return false;
        }

        return bool;
    }

    public static NamespacedKey readNamespacedKey(String verified, Object object) {
        String string = readStringStrict(verified, object);
        if (string == null) {
            return null;
        }

        string = string.toUpperCase(Locale.ROOT);
        NamespacedKey key = NamespacedKey.fromString(string);
        if (key == null) {
            LoggerManager.errorLog(verified + ": '" + string + "' is not a valid namespaced key");
        }
        return key;
    }

    public static ParsedEntry validateEntry(Map<?, ?> entry, String entryString) {
        String material = ConfigHelper.readStringStrict(entryString, entry.get("material"));
        if (material == null) {
            return null;
        }

        String name = ConfigHelper.readString(entryString, entry.get("name"));

        List<String> lore = ConfigHelper.readList(entryString, entry.get("lore"));

        List<Integer> slots = ConfigHelper.readSlots(entryString, entry.get("slots"));
        if (slots.isEmpty()) {
            return null;
        }

        return new ParsedEntry(material, name, lore, slots);
    }
}
