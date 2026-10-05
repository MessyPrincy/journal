package dev.messyprincy.messyJournal.inventory;

import dev.messyprincy.messyJournal.logging.LoggerManager;
import dev.messyprincy.messyJournal.interfaces.JournalEntry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.createInventory;

public class InventoryGui implements InventoryHolder {
    private final Inventory inventory;
    private Inventory parentInventory = null;
    private final Map<Integer, JournalEntry> items;
    private static final LoggerManager LOGGER = new LoggerManager();

    public InventoryGui(String name, int size) {
        this.inventory = createInventory(this, size, LegacyComponentSerializer.legacyAmpersand().deserialize(name));
        this.items = new HashMap<>();
    }

    public InventoryGui(String name, int size, Inventory parentInventory) {
        this.inventory = createInventory(this, size, LegacyComponentSerializer.legacyAmpersand().deserialize(name));
        this.items = new HashMap<>();
        this.parentInventory = parentInventory;
    }

    public void fillInventory(Map<String, JournalEntry> entries) {
        for (JournalEntry entry : entries.values()) {
            items.put(entry.getSlot(), entry);
            this.inventory.setItem(entry.getSlot(), createItemStack(entry.getMaterial(), entry.getName(), entry.getLore()));
        }
    }

    public JournalEntry getEntry(int key) {
        if (!items.containsKey(key)) {
            return null;
        }

        return items.get(key);
    }

    public Inventory getParentInventory() {return parentInventory;}

    @Override
    public Inventory getInventory() {
        return inventory;
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
