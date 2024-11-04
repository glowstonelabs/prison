@file:Command(
    "gems",
    description = "Shows the player's gems.",
    usage = "Invalid usage. /gems, /gems <player>",
    permission = "zela.gems",
    permissionMessage = "You need zela.gems to do that!"
)

package wtf.amari.prison.economy.commands.gems

import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import wtf.amari.prison.databases.DatabaseManager
import wtf.amari.prison.databases.PlayerCurrencyDAO
import wtf.amari.prison.utils.mm
import wtf.amari.prison.utils.shorthand

/**
 * Retrieves and displays the gems of the executor.
 *
 * @param executor The player executing the command.
 */
fun gems(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val gems = dao.getPlayerCurrency(executor.uniqueId.toString())?.get("gems") as? Int ?: 0

    executor.sendMessage("&f${executor.name}'s &agems are ${gems.shorthand()}".mm())
}

/**
 * Retrieves and displays the gems of the specified player or the executor if no player is specified.
 *
 * @param executor The player executing the command.
 * @param targetName The name of the target player.
 */
fun gems(executor: Player, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: executor
    if (target == null) {
        executor.sendMessage("&cPlayer not found.".mm())
        return
    }

    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val gems = dao.getPlayerCurrency(target.uniqueId.toString())?.get("gems") as? Int ?: 0

    executor.sendMessage("&c${target.name}'s &agems are ${gems.shorthand()}".mm())
}