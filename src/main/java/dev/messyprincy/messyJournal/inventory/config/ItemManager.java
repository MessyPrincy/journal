package dev.messyprincy.messyJournal.inventory.config;

import dev.messyprincy.messyJournal.interfaces.Displayable;
import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {
    private static Map<String, Map<String, Displayable>> entries;

    public static Map<String, Displayable> get(String parent) {
        if (entries == null) {
            load();
        }
        return entries.getOrDefault(parent, Map.of());
    }

    public static void load() {
        entries = new HashMap<>();

        JournalItem update1 = new JournalItem(
                "update_1",
                "PAPER",
                "Update 1",
                List.of("The first update contains the datapack"),
                0,
                "patch_note"
        );

        JournalItem update2 = new JournalItem(
                "update_2",
                "PAPER",
                "Update 2",
                List.of("The second update contains the journal"),
                1,
                "patch_note"
        );

        entries.computeIfAbsent(update1.getParentId(), parent -> new HashMap<>())
                .put(update1.getId(), update1);

        entries.computeIfAbsent(update2.getParentId(), parent -> new HashMap<>())
                .put(update2.getId(), update2);
    }
}
