package wtf.amari.prison.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

val mm = MiniMessage.miniMessage()

fun log(message: String) = log(message.mm())

fun log(message: Component) {
    Bukkit.getConsoleSender().sendMessage(message)
}

fun String.mm(): Component = mm.deserialize(this.convertLegacyColors())
    .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)

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

fun String.gradientText(startColor: String, endColor: String): Component =
    "<gradient:$startColor:$endColor><bold>${this}</bold></gradient>".mm()

// Rainbow gradient text
fun String.rainbow(): Component =
    "<rainbow>${this}</rainbow>".mm()

// Prison-style title with custom colors
fun String.prisonTitle(primary: String = "#1db4d5", secondary: String = "#ffffff"): Component =
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
    filledColor: String = "#1db4d5", emptyColor: String = "gray"
): Component {
    val progress = (current.toFloat() / max * length).toInt()
    return "<$filledColor>${"■".repeat(progress)}<$emptyColor>${"■".repeat(length - progress)}".mm()
}

// Common prison prefixes
object Prefix {
    val ERROR = "<#ff4747>✖ ".mm()
    val SUCCESS = "<#47ff47>✔ ".mm()
    val INFO = "<#1db4d5>✦ ".mm()
    val WARNING = "<#ffd747>⚠ ".mm()
    val ADMIN = "<#ff4747>⚡ ".mm()
    val BROADCAST = "<#1db4d5>» ".mm()
    val SYSTEM = "<#1db4d5>✦ ".mm()
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
fun String.fade(fromColor: String = "#1db4d5", toColor: String = "#ffffff"): Component =
    "<gradient:${fromColor}:${toColor}>${this}</gradient>".mm()

// Boxed message (good for announcements)
fun String.box(borderColor: String = "#1db4d5", textColor: String = "white"): Component = """
     
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
fun createLine(color: String = "#1db4d5", symbol: String = "▬"): Component =
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