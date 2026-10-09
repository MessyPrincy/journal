package dev.messyprincy.messyJournal.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.messyprincy.messyJournal.inventory.InventoryGui;
import dev.messyprincy.messyJournal.inventory.config.JournalConfigManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class JournalCommands {
   public LiteralCommandNode<CommandSourceStack> build() {
       return Commands.literal("journal")
               .executes(context -> {
                   CommandSender sender = context.getSource().getSender();

                   Entity executor = context.getSource().getExecutor();

                   if (!(executor instanceof Player player)) {
                       sender.sendPlainMessage("Only players can open their journal!");

                       return Command.SINGLE_SUCCESS;
                   }

                   InventoryGui journal = new InventoryGui("Journal", 54);

                   journal.fillInventory(JournalConfigManager.get().fillItems());
                   journal.fillInventory(JournalConfigManager.get().categories().values());
                   player.openInventory(journal.getInventory());

                   return Command.SINGLE_SUCCESS;
               })
               .build();
   }
}
