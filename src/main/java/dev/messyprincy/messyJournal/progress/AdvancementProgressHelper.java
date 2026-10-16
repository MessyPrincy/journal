package dev.messyprincy.messyJournal.progress;

import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AdvancementProgressHelper {
    private static final Map<NamespacedKey, List<JournalItem>> advancementItems = new HashMap<>();

    private AdvancementProgressHelper() {}

    public static void register(NamespacedKey key, JournalItem item) {
        advancementItems.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
    }

    public static List<JournalItem> getItems(NamespacedKey key) {
        return advancementItems.getOrDefault(key, List.of());
    }

    public static boolean hasItem(NamespacedKey key) {
        return advancementItems.containsKey(key);
    }
}
