package dev.messyprincy.messyJournal.listeners;

import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;
import dev.messyprincy.messyJournal.logging.LoggerManager;
import dev.messyprincy.messyJournal.progress.ItemProgressHelper;
import dev.messyprincy.messyJournal.progress.PlayerProgressHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (e.getItem() == null) {
            return;
        }

        ItemProgressHelper itemProgressHelper = new ItemProgressHelper(e.getItem());
        String identifier = itemProgressHelper.getIdentifier();
        if (identifier.isEmpty()) {
            return;
        }

        PlayerProgressHelper playerProgressHelper = new PlayerProgressHelper(e.getPlayer());
        if (playerProgressHelper.isUnlocked(identifier)) {
            return;
        }

        JournalItem journalItem = itemProgressHelper.getJournalItem();

        if (journalItem == null) {
            LoggerManager.errorLog("Something went wrong when trying to fetch the journalItem");
            return;
        }

        if (playerProgressHelper.unlockInteractItem(journalItem)) {
            LoggerManager.sendUnlockMessage(
                    e.getPlayer(),
                    journalItem.getName(),
                    journalItem.getParent().getName()
            );
        }
    }
}
