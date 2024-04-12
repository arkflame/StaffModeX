package com.arkflame.staffmodex.player;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.menus.items.PlayerNotesItem;
import com.arkflame.staffmodex.modernlib.menus.Menu;

public class StaffPendingNote {
    private StaffNotes notes;
    private boolean isWriting = false;
    private UUID writingUUID = null;
    private String writingName = null;
    private PlayerNotesItem oldItem = null;

    public StaffPendingNote(StaffNotes notes) {
        this.notes = notes;
    }

    public boolean isWritingNote() {
        return isWriting;
    }

    public StaffNote writeNote(String message) {
        StaffNote note = new StaffNote(writingUUID, writingName, message);
        isWriting = false;
        writingUUID = null;
        writingName = null;
        oldItem = null;
        notes.setNote(note.getUuid(), message);
        return note;
    }

    public void startWritingNote(PlayerNotesItem item, UUID uuid, String name) {
        isWriting = true;
        writingUUID = uuid;
        writingName = name;
        oldItem = item;
    }

    public PlayerNotesItem getNotesItem() {
        return oldItem;
    }

    public void openWriteMenu(Player player, String notes) {
        PlayerNotesItem notesItem = getNotesItem();
        if (notesItem == null) {
            return;
        }
        Menu menu = notesItem.getMenu();
    
        if (menu != null) {
            notesItem.updateLore(notes);
            menu.openInventory(player);
        }
    }
}
