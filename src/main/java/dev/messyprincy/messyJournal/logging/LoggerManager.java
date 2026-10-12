package dev.messyprincy.messyJournal.logging;

import dev.messyprincy.messyJournal.MessyJournal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class LoggerManager {
    private static final TextComponent introText = Component.text("[")
            .color(TextColor.color(4634587))
            .append(Component.text("Meßy's Journal"))
            .color(TextColor.color(14238360))
            .append(Component.text("] "))
            .color(TextColor.color(4634587));

    public static void sendSuccessMessage(CommandSender sender, String text) {
        if (sender instanceof Player player) {
            sendSuccessMessage(player, text);
            return;
        }

        successLog(text);
    }

    public static void sendErrorMessage(CommandSender sender, String text) {
        if (sender instanceof Player player) {
            sendSuccessMessage(player, text);
            return;
        }

        errorLog(text);
    }

    public static void sendSuccessMessage(Player player, String text) {
        player.sendMessage(introText + text);
    }

    public static void sendErrorMessage(Player player, String text) {
        player.sendMessage(introText
                .append(Component.text(text))
                    .color(NamedTextColor.RED));
    }

    public static void successLog(String text) {
        MessyJournal.instance.getLogger().info("[Meßy's Journal] " + text);
    }

    public static void errorLog(String text) {
        MessyJournal.instance.getLogger().warning("[Meßy's Journal] " + text);
    }
}