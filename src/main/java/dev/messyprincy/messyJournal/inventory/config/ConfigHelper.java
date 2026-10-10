package dev.messyprincy.messyJournal.inventory.config;

import dev.messyprincy.messyJournal.MessyJournal;
import dev.messyprincy.messyJournal.enums.EntryType;
import dev.messyprincy.messyJournal.inventory.displayables.JournalFill;
import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;
import dev.messyprincy.messyJournal.logging.LoggerManager;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public final class ConfigHelper {
    private static final String CATEGORY_FOLDER = "categories";
    private static final String FILE_SUFFIX = ".yml";
    private static final LoggerManager LOGGER = new LoggerManager();

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

    public static String readString(Object object) {
        if (object == null) {
            return null;
        }

        return String.valueOf(object);
    }

    public static String readStringStrict(Object object) {
        if (object == null) {
            return null;
        }

        String string = String.valueOf(object);
        string = string.trim();

        if (string.isEmpty()) {
            return null;
        }

        return string;
    }

    public static EntryType readType(Object object) {
        String string = readStringStrict(object);

        if (string == null) {
            return null;
        }

        string = string.toUpperCase(Locale.ROOT);

        try {
            return EntryType.valueOf(string);
        } catch (IllegalArgumentException _) {
            return null;
        }
    }

    public static List<String> readLore(Object object) {
        if (!(object instanceof List<?> objectList)) {
            return List.of();
        }

        List<String> list = new ArrayList<>();

        for (Object entry : objectList) {
            String string = readStringStrict(entry);

            if (string == null) {
                continue;
            }

            list.add(string);
        }

        return List.copyOf(list);
    }

    public static List<Integer> readSlots(Object object) {
        if (!(object instanceof List<?> objectList)) {
            return List.of();
        }

        List<Integer> list = new ArrayList<>();

        for (Object entry : objectList) {
            String string = readStringStrict(entry);

            if (string == null) {
                continue;
            }

            try {
                int integer = Integer.parseInt(string);

                if ((integer < 0 || integer > 54)) {
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

    public static ParsedEntry validateEntry(Map<?, ?> entry, String entryString) {
        String material = ConfigHelper.readStringStrict(entry.get("material"));
        if (material == null) {
            LOGGER.errorLog(entryString + " is missing a material");
            return null;
        }

        String name = ConfigHelper.readString(entry.get("name"));
        if (name == null) {
            LOGGER.errorLog(entryString + " error in name");
        }

        List<String> lore = ConfigHelper.readLore(entry.get("lore"));

        List<Integer> slots = ConfigHelper.readSlots(entry.get("slots"));
        if (slots.isEmpty()) {
            LOGGER.errorLog(entryString + " error in slots");
            return null;
        }

        return new ParsedEntry(material, name, lore, slots);
    }
}
