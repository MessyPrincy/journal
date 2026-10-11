package dev.messyprincy.messyJournal.progress;

import dev.messyprincy.messyJournal.MessyJournal;
import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProgressHelper {
    private final NamespacedKey key = new NamespacedKey(MessyJournal.instance, "progress");;
    private final PersistentDataContainer pdc;
    private final Set<String> unlockedSet;

    public ProgressHelper(Player player) {
        this.pdc = player.getPersistentDataContainer();
        this.unlockedSet = new HashSet<>();
        readUnlocks();
    }

    private void readUnlocks() {
        List<String> unlocks = pdc.getOrDefault(key, PersistentDataType.LIST.strings(), List.of());

        unlockedSet.addAll(unlocks);
    }

    private String createIdentifier(JournalItem item) {
        String category = item.getParentId();
        String id = item.getId();

        return category + "." + id;
    }

    public boolean isUnlocked(JournalItem item) {
        if (item.isUnlockedByDefault()) {
            return true;
        }

        String identifier = createIdentifier(item);

        return unlockedSet.contains(identifier);
    }

    public void unlockItem(JournalItem item) {
        readUnlocks();
        if (isUnlocked(item)) {
            return;
        }

        String identifier = createIdentifier(item);

        unlockedSet.add(identifier);

        pdc.set(key, PersistentDataType.LIST.strings(), unlockedSet.stream().toList());
    }
}
