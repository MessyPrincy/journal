package dev.messyprincy.messyJournal.interfaces;

import java.util.List;

public interface JournalEntry {
    String getId();
    int getSlot();
    String getMaterial();
    String getName();
    List<String> getLore();
}
