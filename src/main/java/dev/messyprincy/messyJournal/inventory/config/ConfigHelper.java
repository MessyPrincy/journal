package dev.messyprincy.messyJournal.inventory.config;

import dev.messyprincy.messyJournal.enums.EntryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ConfigHelper {
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

    public static String readString(Object object) {
        if (object == null) {
            return null;
        }

        return String.valueOf(object);
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

                if ((integer < 0 || integer > 53)) {
                    continue;
                }

                list.add(integer);
            } catch (NumberFormatException _) {
            }

        }

        return List.copyOf(list);
    }
}
