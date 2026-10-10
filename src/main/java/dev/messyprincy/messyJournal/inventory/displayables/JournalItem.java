package dev.messyprincy.messyJournal.inventory.displayables;

import dev.messyprincy.messyJournal.interfaces.JournalEntry;

import java.util.List;

public class JournalItem implements JournalEntry {
    private final String id;
    private final int slot;
    private final String material;
    private final String name;
    private final List<String> lore;
    private final String parentId;
    private final boolean unlockedByDefault;

    public JournalItem(String id, String material, String name, List<String> lore, int slot, String parentId, boolean unlockedByDefault) {
        this.id = id;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.slot = slot;
        this.parentId = parentId;
        this.unlockedByDefault = unlockedByDefault;
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
        return name;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    public String getParentId() {
        return parentId;
    }

    public boolean isUnlockedByDefault() {
        return unlockedByDefault;
    }
}
