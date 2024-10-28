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