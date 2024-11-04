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

/**
 * Clears the inventory of the executor or the specified target player.
 */
object ClearInventory {

    /**
     * Clears the executor's inventory and sends a confirmation message.
     *
     * @param executor The player executing the command.
     */
    fun clear(executor: Player) {
        executor.inventory.clear()
        executor.sendMessage("&aYou have cleared your inventory.".mm())
    }

    /**
     * Clears the inventory of the specified target player and sends confirmation messages.
     *
     * @param executor The player executing the command.
     * @param targetName The name of the target player whose inventory is to be cleared.
     */
    fun clear(executor: Player, targetName: String?) {
        val target = targetName?.let { Bukkit.getPlayer(it) }
        if (target == null) {
            executor.sendMessage("&cPlayer not found.".mm())
            return
        }
        target.inventory.clear()
        executor.sendMessage("&aCleared &c${target.name}'s &ainventory.".mm())
    }
}