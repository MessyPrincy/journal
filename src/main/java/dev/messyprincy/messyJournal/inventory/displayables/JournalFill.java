package dev.messyprincy.messyJournal.inventory.displayables;

import dev.messyprincy.messyJournal.interfaces.Displayable;

import java.util.List;

public class JournalFill implements Displayable {
    private final int slot;
    private final String material;
    private final String name;
    private final List<String> lore;

    public JournalFill (String material, String name, List<String> lore, int slot) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.slot = slot;
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
}
