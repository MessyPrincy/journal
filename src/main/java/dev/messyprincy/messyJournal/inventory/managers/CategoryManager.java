package dev.messyprincy.messyJournal.inventory.managers;

import dev.messyprincy.messyJournal.interfaces.JournalEntry;
import dev.messyprincy.messyJournal.inventory.JournalCategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryManager {
    private static Map<String, JournalEntry> categories;

    public static Map<String, JournalEntry> get() {
        if (categories == null) {
            load();
        }
        return categories;
    }

    public static void load() {
        categories = new HashMap<>();

        JournalCategory patchNotes = new JournalCategory(
                "patch_note",
                "PAPER",
                "&r&6&bPatch Notes",
                List.of("Stay up to date with all the latest updates of the server"),
                11
        );
        JournalCategory world = new JournalCategory(
                "world",
                "GRASS_BLOCK",
                "&r&6&bWorld",
                List.of("Information about the world lore"),
                13
        );
        JournalCategory booksClues = new JournalCategory(
                "books_clues",
                "BOOK",
                "&r&6&bBooks & Clues",
                List.of("The books and clues gathered throughout your journey"),
                15
        );

        categories.put(patchNotes.getId(), patchNotes);
        categories.put(world.getId(), world);
        categories.put(booksClues.getId(), booksClues);
    }
}
