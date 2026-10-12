package dev.messyprincy.messyJournal.logging;

import dev.messyprincy.messyJournal.MessyJournal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class LoggerManager {
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();
    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

    private static final int CHAT_WIDTH_PX = 320;
    private static final Component SEPARATOR = Component.text("=".repeat(52), NamedTextColor.DARK_GRAY);

    private static final Component introText = Component.text("[")
            .color(TextColor.color(0x46B7DB))
            .append(Component.text("Meßy's Journal")
                    .color(TextColor.color(0xD94198)))
            .append(Component.text("] ")
                    .color(TextColor.color(0x46B7DB)));

    private static Component center(Component component) {
        int textWidth = PLAIN.serialize(component).length() * 6;
        int spaces = Math.max(0, (CHAT_WIDTH_PX - textWidth) / 2 / 4);

        return Component.text(" ".repeat(spaces)).append(component);
    }

    public static void sendUnlockMessage(Player player, String entryName, String categoryName) {
        Component entry = LEGACY.deserialize(entryName);
        Component category = LEGACY.deserialize(categoryName);

        Component title = Component.text("New Entry Unlocked!", NamedTextColor.GREEN, TextDecoration.BOLD);
        Component subtitle = Component.empty()
                .append(entry)
                .append(Component.text(" in ", NamedTextColor.GRAY))
                .append(category);

        player.sendMessage(Component.join(
                JoinConfiguration.newlines(),
                SEPARATOR,
                center(title),
                center(subtitle),
                SEPARATOR
        ));
    }

    public static void sendSuccessMessage(CommandSender sender, String text) {
        if (sender instanceof Player player) {
            sendSuccessMessage(player, text);
            return;
        }

        successLog(text);
    }

    public static void sendErrorMessage(CommandSender sender, String text) {
        if (sender instanceof Player player) {
            sendErrorMessage(player, text);
            return;
        }

        errorLog(text);
    }

    public static void sendSuccessMessage(Player player, String text) {
        player.sendMessage(introText.append(
                Component.text(text)
        ));
    }

    public static void sendErrorMessage(Player player, String text) {
        player.sendMessage(introText.append(
                Component.text(text).color(NamedTextColor.RED)
        ));
    }

    public static void successLog(String text) {
        MessyJournal.instance.getLogger().info("[Meßy's Journal] " + text);
    }

    public static void errorLog(String text) {
        MessyJournal.instance.getLogger().warning("[Meßy's Journal] " + text);
    }
}