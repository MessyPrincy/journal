package dev.messyprincy.messyJournal.listeners;

import dev.messyprincy.messyJournal.inventory.InventoryGui;
import dev.messyprincy.messyJournal.interfaces.JournalEntry;
import dev.messyprincy.messyJournal.inventory.config.JournalConfigManager;
import dev.messyprincy.messyJournal.inventory.config.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        Inventory inventory = e.getInventory();

        if (!(inventory.getHolder(false) instanceof InventoryGui)) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();

        if (!(inventory.getHolder(false) instanceof InventoryGui journalInventory)) {
            return;
        }

        if (!(e.getWhoClicked() instanceof Player player)) {
            return;
        }

        e.setCancelled(true);

        if (!(Objects.equals(e.getClickedInventory(), journalInventory.getInventory()))) {
            return;
        }

        JournalEntry entry = journalInventory.getEntry(e.getSlot());
        if (entry == null) {
            return;
        }

        if (entry.getId().equals("exit")) {
            if (journalInventory.hasParent()) {
                openInventory(player, journalInventory.getParentInventoryGui().getInventory());
            } else {
                player.closeInventory();
            }
            return;
        }

        if (JournalConfigManager.get().categories().containsKey(entry.getId())) {
            openInventory(player, createInventoryGui(entry, journalInventory).getInventory());
        }

    }

    private InventoryGui createInventoryGui(JournalEntry category, InventoryGui parent) {
        InventoryGui inventoryGui = new InventoryGui(category.getName(), 54, parent);
        inventoryGui.fillInventory(ItemManager.get(category.getId()).values());

        return inventoryGui;
    }

    private void openInventory(Player player, Inventory inventory) {
        player.openInventory(inventory);
    }
}
