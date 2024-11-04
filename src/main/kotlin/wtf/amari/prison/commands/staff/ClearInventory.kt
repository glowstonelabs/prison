@file:Command(
    "clear",
    "ci",
    description = "Clear inventory",
    usage = "Invalid usage. /clear <player>",
    permission = "prison.admin.clear",
    permissionMessage = "You need prison.admin.clear to do that!"
)

package wtf.amari.prison.commands.staff

import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import wtf.amari.prison.utils.mm

fun clear(executor: Player) {
    executor.inventory.clear()
    executor.sendMessage("&aYou have cleared your inventory".mm())
}

fun clear(executor: Player, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: run {
        executor.sendMessage("&cPlayer not found.".mm())
        return
    }
    target.inventory.clear()
    executor.sendMessage("&aCleared &c${target.name}'s &aInventory".mm())
}