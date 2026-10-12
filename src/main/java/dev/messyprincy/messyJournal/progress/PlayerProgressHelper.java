package dev.messyprincy.messyJournal.progress;

import dev.messyprincy.messyJournal.MessyJournal;
import dev.messyprincy.messyJournal.enums.DiscoverType;
import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerProgressHelper {
    private final PersistentDataContainer pdc;
    private final Set<String> unlockedSet;

    public PlayerProgressHelper(Player player) {
        this.pdc = player.getPersistentDataContainer();
        this.unlockedSet = new HashSet<>();
        readUnlocks();
    }

    private void readUnlocks() {
        List<String> unlocks = pdc.getOrDefault(ProgressKeys.getPlayerKey(), PersistentDataType.LIST.strings(), List.of());

        unlockedSet.addAll(unlocks);
    }

    public String createIdentifier(JournalItem item) {
        String category = item.getParent().getId();
        String id = item.getId();

        return category + "." + id;
    }

    public boolean isUnlocked(JournalItem item) {
        if (item.isUnlockedByDefault()) {
            return true;
        }

        return isUnlocked(createIdentifier(item));
    }

    public boolean isUnlocked(String identifier) {
        return unlockedSet.contains(identifier);
    }

    public boolean unlockItem(JournalItem item) {
        readUnlocks();
        if (isUnlocked(item)) {
            return false;
        }

        unlockedSet.add(createIdentifier(item));
        pdc.set(ProgressKeys.getPlayerKey(), PersistentDataType.LIST.strings(), unlockedSet.stream().toList());
        return true;
    }

    public boolean unlockInteractItem(JournalItem item) {
        if (item.getDiscoverType() != DiscoverType.INTERACT) {
            return false;
        }

        return unlockItem(item);
    }
}
