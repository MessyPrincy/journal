package dev.messyprincy.messyJournal.inventory.config;

import dev.messyprincy.messyJournal.interfaces.JournalEntry;
import dev.messyprincy.messyJournal.inventory.displayables.JournalFill;

import java.util.List;
import java.util.Map;

public record CategoryConfig(List<JournalFill> fillItems, Map<String, JournalEntry> items) {
    public CategoryConfig {
        fillItems = List.copyOf(fillItems);
        items = Map.copyOf(items);
    }
}
