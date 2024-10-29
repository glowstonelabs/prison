package wtf.amari.prison.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

val mm = MiniMessage.miniMessage()

object Colors {
    const val PRIMARY = "#1db4d5"
    const val SECONDARY = "#ffffff"
    const val ERROR = "#ff4747"
    const val SUCCESS = "#47ff47"
    const val WARNING = "#ffd747"
    const val INFO = "#1db4d5"
    const val BROADCAST = "#1db4d5"
    const val SYSTEM = "#1db4d5"
}

// Logging function with timestamp
fun log(message: Component) {
    val timeStamp = System.currentTimeMillis()
    Bukkit.getConsoleSender().sendMessage("[$timeStamp] $message")
}

fun String.mm(): Component = mm.deserialize(this.convertLegacyColors())
    .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)

// Gradient text
fun String.mainGradient(): Component = "<gradient:#b8ecff:#77daff>${this}</gradient>".mm()

fun String.convertLegacyColors(): String = this
    .replace("&0", "&r<black>")
    .replace("&1", "&r<dark_blue>")
    .replace("&2", "&r<dark_green>")
    .replace("&3", "&r<dark_aqua>")
    .replace("&4", "&r<dark_red>")
    .replace("&5", "&r<dark_purple>")
    .replace("&6", "&r<gold>")
    .replace("&7", "&r<gray>")
    .replace("&8", "&r<dark_gray>")
    .replace("&9", "&r<blue>")
    .replace("&a", "&r<green>")
    .replace("&b", "&r<aqua>")
    .replace("&c", "&r<red>")
    .replace("&d", "&r<light_purple>")
    .replace("&e", "&r<yellow>")
    .replace("&f", "&r<white>")
    .replace("&k", "<obf>")
    .replace("&l", "<b>")
    .replace("&m", "<st>")
    .replace("&n", "<u>")
    .replace("&o", "<i>")
    .replace("&r", "<reset>")

fun String.convertNewColors(): String = this
    .replace("&0", "§0")
    .replace("&1", "§1")
    .replace("&2", "§2")
    .replace("&3", "§3")
    .replace("&4", "§4")
    .replace("&5", "§5")
    .replace("&6", "§6")
    .replace("&7", "§7")
    .replace("&8", "§8")
    .replace("&9", "§9")
    .replace("&a", "§a")
    .replace("&b", "§b")
    .replace("&c", "§c")
    .replace("&d", "§d")
    .replace("&e", "§e")
    .replace("&f", "§f")
    .replace("&k", "§k")
    .replace("&l", "§l")
    .replace("&m", "§m")
    .replace("&n", "§n")
    .replace("&o", "§o")
    .replace("&r", "§r")

fun String.gradientText(vararg colors: String): Component {
    val gradient = colors.joinToString(":") { it }
    return "<gradient:$gradient>${this}</gradient>".mm()
}

// Rainbow gradient text
fun String.rainbow(): Component = "<rainbow>${this}</rainbow>".mm()

// Prison-style title with custom colors
fun String.prisonTitle(primary: String = Colors.PRIMARY, secondary: String = Colors.SECONDARY): Component =
    "<$primary>✦ <$secondary>${this}".mm()

// Hoverable text with tooltip
fun String.withHover(hoverText: String): Component =
    "<hover:show_text:'${hoverText.replace("'", "\\'")}'>${this}</hover>".mm()

// Clickable text for commands
fun String.withCommand(command: String): Component =
    "<click:run_command:'${command.replace("'", "\\'")}'>${this}</click>".mm()

// Suggestion text (puts text in chat)
fun String.withSuggest(suggestion: String): Component =
    "<click:suggest_command:'${suggestion.replace("'", "\\'")}'>${this}</click>".mm()

// URL text (opens link)
fun String.withUrl(url: String): Component =
    "<click:open_url:'${url.replace("'", "\\'")}'>${this}</click>".mm()

// Progress bar (useful for pickaxe levels etc)
fun createProgressBar(
    current: Int, max: Int, length: Int = 10,
    filledColor: String = Colors.PRIMARY, emptyColor: String = "gray"
): Component {
    val progress = (current.toFloat() / max * length).toInt()
    return "<$filledColor>${"■".repeat(progress)}<$emptyColor>${"■".repeat(length - progress)}".mm()
}

// Common prison prefixes
object Prefix {
    val ERROR = "<${Colors.ERROR}>✖ ".mm()
    val SUCCESS = "<${Colors.SUCCESS}>✔ ".mm()
    val INFO = "<${Colors.INFO}>✦ ".mm()
    val WARNING = "<${Colors.WARNING}>⚠ ".mm()
    val ADMIN = "<${Colors.ERROR}>⚡ ".mm()
    val BROADCAST = "<${Colors.BROADCAST}>» ".mm()
    val SYSTEM = "<${Colors.SYSTEM}>✦ ".mm()
}

// Prison-style list item
fun String.listItem(bullet: String = "▪", bulletColor: String = "gray", textColor: String = "white"): Component =
    "<$bulletColor>${bullet} <$textColor>${this}".mm()

// Stats display line
fun createStatsLine(
    label: String, value: String,
    labelColor: String = "gray", valueColor: String = "white"
): Component =
    "<$labelColor>${label}: <$valueColor>${value}".mm()

// Fading text (good for titles)
fun String.fade(fromColor: String = Colors.PRIMARY, toColor: String = Colors.SECONDARY): Component =
    "<gradient:${fromColor}:${toColor}>${this}</gradient>".mm()

// Boxed message (good for announcements)
fun String.box(borderColor: String = Colors.PRIMARY, textColor: String = "white"): Component = """
     
    <$borderColor>┌────────────────────────┐
    <$borderColor>│ <$textColor>${this.center(20)}<$borderColor> │
    <$borderColor>└────────────────────────┘
     
""".trimIndent().mm()

// Center text utility
private fun String.center(width: Int): String {
    if (this.length >= width) return this
    val leftPad = (width - this.length) / 2
    val rightPad = width - this.length - leftPad
    return " ".repeat(leftPad) + this + " ".repeat(rightPad)
}

// Create formatted number (e.g., 1000 -> 1,000)
fun Number.format(): String = String.format("%,d", this)

// Shorthand number format (e.g., 1000 -> 1K, 1000000 -> 1M)
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

// Create a bordered line
fun createLine(color: String = Colors.PRIMARY, symbol: String = "▬"): Component =
    "<$color>${symbol.repeat(40)}".mm()

// Money format
fun Double.money(): String = String.format("$%,.2f", this)

// Example usage for your reference:
/*
player.sendMessage("Welcome!".rainbow())
player.sendMessage("LEVEL UP!".prisonTitle())
player.sendMessage("Click me!".withHover("Shows more info"))
player.sendMessage("Spawn".withCommand("/spawn"))
player.sendMessage(createProgressBar(50, 100))
player.sendMessage("Blocks: ${1000000.shorthand()}")
player.sendMessage(Prefix.ERROR.append("No permission!".mm()))
player.sendMessage("ANNOUNCEMENT".box())
player.sendMessage(createLine())
*/
