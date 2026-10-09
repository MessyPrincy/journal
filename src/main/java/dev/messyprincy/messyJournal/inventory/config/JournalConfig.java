package dev.messyprincy.messyJournal.inventory.config;

import dev.messyprincy.messyJournal.interfaces.JournalEntry;
import dev.messyprincy.messyJournal.inventory.displayables.JournalFill;

import java.util.List;
import java.util.Map;

public record JournalConfig(List<JournalFill> fillItems, Map<String, JournalEntry> categories) {
    public JournalConfig {
        fillItems = List.copyOf(fillItems);
        categories = Map.copyOf(categories);
    }
}
