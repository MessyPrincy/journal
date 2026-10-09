package dev.messyprincy.messyJournal.inventory;

import dev.messyprincy.messyJournal.interfaces.Displayable;
import dev.messyprincy.messyJournal.inventory.displayables.JournalCategory;
import dev.messyprincy.messyJournal.logging.LoggerManager;
import dev.messyprincy.messyJournal.interfaces.JournalEntry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static org.bukkit.Bukkit.createInventory;

public class InventoryGui implements InventoryHolder {
    private final Inventory inventory;
    private final InventoryGui parentInventory;
    private final Map<Integer, JournalEntry> items;
    private static final LoggerManager LOGGER = new LoggerManager();
    private static final int EXIT_SLOT = 8;

    public InventoryGui(String name, int size) {
        this(name, size, null);
    }

    public InventoryGui(String name, int size, InventoryGui parentInventory) {
        this.inventory = createInventory(this, size, LegacyComponentSerializer.legacyAmpersand().deserialize(name));
        this.items = new HashMap<>();
        this.parentInventory = parentInventory;
        setExitItem();
    }

    public void fillInventory(Collection<? extends Displayable> entries) {
        for (Displayable entry : entries) {
            if (entry.getSlot() == EXIT_SLOT) {
                LOGGER.errorLog("Tried to overwrite the exit item. Skipping entry");
                continue;
            }

            inventory.setItem(entry.getSlot(), createItemStack(entry.getMaterial(), entry.getName(), entry.getLore()));

            if (entry instanceof JournalEntry journalEntry) {
                items.put(entry.getSlot(), journalEntry);
            }
        }
    }

    public JournalEntry getEntry(int key) {return items.get(key);}

    public InventoryGui getParentInventoryGui() {return parentInventory;}

    public boolean hasParent() {return parentInventory != null;}

    @Override
    public Inventory getInventory() {return inventory;}

    private void setExitItem() {
        Material material = Material.RED_STAINED_GLASS_PANE;
        String displayName = hasParent() ? "&c&l< Back" : "&c&lClose";
        List<String> lore = List.of(
                hasParent() ? "&7Click to return to the previous page." : "&7Click to close"
        );

        JournalEntry entry = new JournalCategory("exit", material.name(), displayName, lore, EXIT_SLOT);

        inventory.setItem(EXIT_SLOT, createItemStack(entry.getMaterial(), entry.getName(), entry.getLore()));
        items.put(EXIT_SLOT, entry);
    }

    private ItemStack createItemStack(String material, String name, List<String> lore) {
        Material itemMaterial = Material.matchMaterial(material);

        if (itemMaterial == null) {
            LOGGER.errorLog(material + " is not a valid material. Falling back to default");
            itemMaterial = Material.DIRT;
        }

        ItemStack item = new ItemStack(itemMaterial);
        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            Component displayName = LegacyComponentSerializer.legacyAmpersand().deserialize(name);
            meta.customName(displayName);
        }

        if (lore != null && !lore.isEmpty()) {
            List<Component> loreText = new ArrayList<>();
            for (String line : lore) {
                Component loreLine = LegacyComponentSerializer.legacyAmpersand().deserialize(line);
                loreText.add(loreLine);
            }

            meta.lore(loreText);
        }

        item.setItemMeta(meta);
        return item;
    }
}
