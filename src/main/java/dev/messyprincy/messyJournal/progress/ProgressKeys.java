package dev.messyprincy.messyJournal.progress;

import dev.messyprincy.messyJournal.MessyJournal;
import org.bukkit.NamespacedKey;

public final class ProgressKeys {
    private static NamespacedKey playerKey;
    private static NamespacedKey itemKey;

    public static void init() {
        playerKey = new NamespacedKey(MessyJournal.instance, "unlockedEntries");
        itemKey = new NamespacedKey(MessyJournal.instance, "journal");
    }

    public static NamespacedKey getPlayerKey() {
        return playerKey;
    }

    public static NamespacedKey getItemKey() {
        return itemKey;
    }
}
