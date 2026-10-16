package dev.messyprincy.messyJournal.inventory.displayables;

import dev.messyprincy.messyJournal.enums.DiscoverType;
import dev.messyprincy.messyJournal.interfaces.JournalEntry;

import java.util.List;

public class JournalItem implements JournalEntry {
    private final String id;
    private final int slot;
    private final String material;
    private final String name;
    private final List<String> lore;
    private final JournalEntry parent;
    private final boolean unlockedByDefault;
    private final DiscoverType discoverType;

    public JournalItem(String id, String material, String name, List<String> lore, int slot, JournalEntry parent, boolean unlockedByDefault, DiscoverType discoverType) {
        this.id = id;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.slot = slot;
        this.parent = parent;
        this.unlockedByDefault = unlockedByDefault;
        this.discoverType = discoverType;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public String getMaterial() {
        return material;
    }

    @Override
    public String getName() {
        if (name == null) {
            return id;
        }
        return name;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    public JournalEntry getParent() {
        return parent;
    }

    public boolean isUnlockedByDefault() {
        return unlockedByDefault;
    }

    public DiscoverType getDiscoverType() {
        return discoverType;
    }
}
