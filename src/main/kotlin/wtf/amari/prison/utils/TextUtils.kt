package wtf.amari.prison.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit.getConsoleSender

val mm = MiniMessage.miniMessage()

object Colors {
    private val colors = mapOf(
        "PRIMARY" to "#3498db",    // Blue
        "SECONDARY" to "#2ecc71",  // Green
        "ERROR" to "#e74c3c",      // Red
        "SUCCESS" to "#2ecc71",    // Green
        "WARNING" to "#f1c40f",    // Yellow
        "INFO" to "#3498db",       // Blue
        "BROADCAST" to "#9b59b6",  // Purple
        "SYSTEM" to "#34495e"      // Dark Blue
    )

    fun getColor(name: String): String = colors[name] ?: "#ffffff" // default to white if not found
}

fun fancyLog(message: String, level: String) {
    val color = Colors.getColor(level)
    val boxedMessage = message.box(borderColor = color, textColor = color)
    getConsoleSender().sendMessage(boxedMessage)
}

fun String.mm(): Component = mm.deserialize(this.convertLegacyColors())
    .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)

fun String.mainGradient(): Component = "<gradient:#b8ecff:#77daff>${this}</gradient>".mm()

fun String.convertLegacyColors(): String = this
    .replace("&0", "&r<black>").replace("&1", "&r<dark_blue>")
    .replace("&2", "&r<dark_green>").replace("&3", "&r<dark_aqua>")
    .replace("&4", "&r<dark_red>").replace("&5", "&r<dark_purple>")
    .replace("&6", "&r<gold>").replace("&7", "&r<gray>")
    .replace("&8", "&r<dark_gray>").replace("&9", "&r<blue>")
    .replace("&a", "&r<green>").replace("&b", "&r<aqua>")
    .replace("&c", "&r<red>").replace("&d", "&r<light_purple>")
    .replace("&e", "&r<yellow>").replace("&f", "&r<white>")
    .replace("&k", "<obf>").replace("&l", "<b>")
    .replace("&m", "<st>").replace("&n", "<u>")
    .replace("&o", "<i>").replace("&r", "<reset>")

fun String.convertNewColors(): String = this
    .replace("&0", "§0").replace("&1", "§1")
    .replace("&2", "§2").replace("&3", "§3")
    .replace("&4", "§4").replace("&5", "§5")
    .replace("&6", "§6").replace("&7", "§7")
    .replace("&8", "§8").replace("&9", "§9")
    .replace("&a", "§a").replace("&b", "§b")
    .replace("&c", "§c").replace("&d", "§d")
    .replace("&e", "§e").replace("&f", "§f")
    .replace("&k", "§k").replace("&l", "§l")
    .replace("&m", "§m").replace("&n", "§n")
    .replace("&o", "§o").replace("&r", "§r")

fun String.complexGradient(vararg colors: String): Component {
    val gradient = colors.joinToString(":")
    return "<gradient:$gradient>${this}</gradient>".mm()
}

fun String.withEmoji(emoji: String): Component = "${this} $emoji".mm()

fun String.gradientText(vararg colors: String): Component {
    val gradient = colors.joinToString(":")
    return "<gradient:$gradient>${this}</gradient>".mm()
}

fun String.rainbow(): Component = "<rainbow>${this}</rainbow>".mm()

fun String.prisonTitle(
    primary: String = Colors.getColor("PRIMARY"),
    secondary: String = Colors.getColor("SECONDARY")
): Component = "<$primary>✦ <$secondary>${this}".mm()

fun String.withHover(hoverText: String): Component =
    "<hover:show_text:'${hoverText.replace("'", "\\'")}'>${this}</hover>".mm()

fun String.withCommand(command: String): Component =
    "<click:run_command:'${command.replace("'", "\\'")}'>${this}</click>".mm()

fun String.withSuggest(suggestion: String): Component =
    "<click:suggest_command:'${suggestion.replace("'", "\\'")}'>${this}</click>".mm()

fun String.withUrl(url: String): Component =
    "<click:open_url:'${url.replace("'", "\\'")}'>${this}</click>".mm()

fun createProgressBar(
    current: Int, max: Int, length: Int = 10,
    filledSymbol: String = "■", emptySymbol: String = "□",
    filledColor: String = Colors.getColor("PRIMARY"), emptyColor: String = "gray"
): Component {
    val progress = (current.toFloat() / max * length).toInt()
    return "<$filledColor>${filledSymbol.repeat(progress)}<$emptyColor>${emptySymbol.repeat(length - progress)}".mm()
}

object Prefix {
    val ERROR = "<${Colors.getColor("ERROR")}>✖ ".mm()
    val SUCCESS = "<${Colors.getColor("SUCCESS")}>✔ ".mm()
    val INFO = "<${Colors.getColor("INFO")}>✦ ".mm()
    val WARNING = "<${Colors.getColor("WARNING")}>⚠ ".mm()
    val ADMIN = "<${Colors.getColor("ERROR")}>⚡ ".mm()
    val BROADCAST = "<${Colors.getColor("BROADCAST")}>» ".mm()
    val SYSTEM = "<${Colors.getColor("SYSTEM")}>✦ ".mm()
}

fun String.listItem(bullet: String = "▪", bulletColor: String = "gray", textColor: String = "white"): Component =
    "<$bulletColor>${bullet} <$textColor>${this}".mm()

fun createStatsLine(
    label: String, value: String,
    labelColor: String = "gray", valueColor: String = "white"
): Component = "<$labelColor>${label}: <$valueColor>${value}".mm()

fun String.fade(
    fromColor: String = Colors.getColor("PRIMARY"),
    toColor: String = Colors.getColor("SECONDARY")
): Component = "<gradient:${fromColor}:${toColor}>${this}</gradient>".mm()

fun String.box(
    borderColor: String = Colors.getColor("PRIMARY"),
    textColor: String = "white",
    padding: Int = 2
): Component {
    val width = this.length + 4 + padding * 2 // Add padding for the box and extra spaces
    val topBottomBorder = "─".repeat(width)
    val newLine = System.lineSeparator()
    return """
$newLine<$borderColor>┌$topBottomBorder┐
<$textColor>${" ".repeat(padding)}${this.center(width - padding * 2)}${" ".repeat(padding)}
<$borderColor>└$topBottomBorder┘
    """.trimIndent().mm()
}

private fun String.center(width: Int): String {
    if (this.length >= width) return this
    val padding = (width - this.length) / 2
    return " ".repeat(padding) + this + " ".repeat(width - this.length - padding)
}

fun Number.format(): String = String.format("%,.2f", this.toDouble())

fun Number.shorthand(): String {
    val number = this.toLong()
    return when {
        number >= 1_000_000_000_000 -> String.format("%.1fT", number / 1_000_000_000_000.0)
        number >= 1_000_000_000 -> String.format("%.1fB", number / 1_000_000_000.0)
        number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}

fun createLine(color: String = Colors.getColor("PRIMARY"), symbol: String = "▬"): Component =
    "<$color>${symbol.repeat(40)}".mm()

fun Double.money(): String = String.format("$%,.2f", this)