package dev.messyprincy.messyJournal.listeners;

import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;
import dev.messyprincy.messyJournal.logging.LoggerManager;
import dev.messyprincy.messyJournal.progress.AdvancementProgressHelper;
import dev.messyprincy.messyJournal.progress.PlayerProgressHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.List;

public class AdvancementListener implements Listener {
    @EventHandler
    public void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent e) {
        List<JournalItem> items = AdvancementProgressHelper.getItems(e.getAdvancement().getKey());
        if (items.isEmpty()) {
            return;
        }

        PlayerProgressHelper progressHelper = new PlayerProgressHelper(e.getPlayer());

        for (JournalItem item : items) {
            if (progressHelper.isUnlocked(item)) {
                continue;
            }
            progressHelper.unlockItem(item);
            LoggerManager.sendUnlockMessage(e.getPlayer(), item.getName(), item.getParent().getName());
        }
    }
}