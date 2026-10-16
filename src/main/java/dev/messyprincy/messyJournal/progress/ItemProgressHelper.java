package dev.messyprincy.messyJournal.progress;

import dev.messyprincy.messyJournal.interfaces.JournalEntry;
import dev.messyprincy.messyJournal.inventory.config.JournalConfigManager;
import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemProgressHelper {
    private final PersistentDataContainerView pdcView;

    public ItemProgressHelper(ItemStack item) {
        this.pdcView = item.getPersistentDataContainer();
    }

    public String getIdentifier() {
        return pdcView.getOrDefault(ProgressUtils.getItemKey(), PersistentDataType.STRING, "");
    }

    public JournalItem getJournalItem() {
        String identifier = getIdentifier();

        if (identifier.isEmpty()) {
            return null;
        }

        String[] identifiers = identifier.split("[.]");

        if (identifiers.length != 2) {
            return null;
        }

        JournalEntry entry = JournalConfigManager.getCategoryConfig(identifiers[0]).items().get(identifiers[1]);

        if (!(entry instanceof JournalItem journalItem)) {
            return null;
        }

        return journalItem;
    }
}
