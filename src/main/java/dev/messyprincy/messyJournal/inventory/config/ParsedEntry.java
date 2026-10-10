package dev.messyprincy.messyJournal.inventory.config;

import java.util.List;

public record ParsedEntry(String material, String name, List<String> lore, List<Integer> slots) {
}
