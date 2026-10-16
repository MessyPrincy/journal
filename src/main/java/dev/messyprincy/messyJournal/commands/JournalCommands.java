package dev.messyprincy.messyJournal.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.messyprincy.messyJournal.inventory.InventoryGui;
import dev.messyprincy.messyJournal.inventory.config.JournalConfigManager;
import dev.messyprincy.messyJournal.inventory.displayables.JournalItem;
import dev.messyprincy.messyJournal.logging.LoggerManager;
import dev.messyprincy.messyJournal.progress.PlayerProgressHelper;
import dev.messyprincy.messyJournal.progress.ProgressUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class JournalCommands {
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("journal")
                .requires(source -> source.getSender().isOp())
                .executes(context -> {
                    CommandSender sender = context.getSource().getSender();

                    Entity executor = context.getSource().getExecutor();

                    if (!(executor instanceof Player player)) {
                        sender.sendPlainMessage("Only players can open their journal!");

                        return Command.SINGLE_SUCCESS;
                    }

                    InventoryGui journal = new InventoryGui("Journal", 54, player);

                    journal.fillInventory(JournalConfigManager.get().fillItems());
                    journal.fillInventory(JournalConfigManager.get().categories().values());
                    player.openInventory(journal.getInventory());

                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("unlock")
                        .then(Commands.argument("targets", ArgumentTypes.player())
                                .then(Commands.argument("category", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            JournalConfigManager.get().categories().keySet().stream()
                                                    .filter(category -> category.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                                                    .forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .then(Commands.argument("entry", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    JournalConfigManager.getCategoryConfig(context.getArgument("category", String.class)).items().keySet().stream()
                                                            .filter(item -> item.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                                                            .forEach(builder::suggest);
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    PlayerSelectorArgumentResolver resolver =
                                                            context.getArgument("target", PlayerSelectorArgumentResolver.class);
                                                    List<Player> targets = resolver.resolve(context.getSource());
                                                    String category = context.getArgument("category", String.class);
                                                    String entry = context.getArgument("entry", String.class);

                                                    CommandSender sender = context.getSource().getSender();

                                                    if (!(JournalConfigManager.getCategoryConfig(category).items().get(entry) instanceof JournalItem item)) {
                                                        LoggerManager.sendErrorMessage(sender, "Error finding the entry");

                                                        return Command.SINGLE_SUCCESS;
                                                    }

                                                    for (Player targetPlayer : targets) {
                                                        PlayerProgressHelper progress = new PlayerProgressHelper(targetPlayer);
                                                        progress.unlockItem(item);

                                                        LoggerManager.sendUnlockMessage(
                                                                targetPlayer,
                                                                item.getName(),
                                                                item.getParent().getName()
                                                        );
                                                    }

                                                    LoggerManager.sendSuccessMessage(sender, "Successfully unlocked " + ProgressUtils.createIdentifier(item) + " for " + targets.size() + " players");
                                                    return Command.SINGLE_SUCCESS;
                                                }))
                                ))
                )
                .build();
    }
}
