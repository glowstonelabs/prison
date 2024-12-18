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
 * Shows the gems of the executing player.
 * @param executor The player executing the command.
 */
fun gems(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(executor.uniqueId.toString())
    val gems = playerCurrency?.get("gems") as? Int ?: 0

    executor.sendMessage("&f${executor.name}'s &agems are ${gems.shorthand()}".mm())
}

/**
 * Shows the gems of the specified player or the executing player if no target is specified.
 * @param executor The player executing the command.
 * @param targetName The target player's name.
 */
fun gems(executor: Player, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: executor
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(target.uniqueId.toString())
    val gems = playerCurrency?.get("gems") as? Int ?: 0

    executor.sendMessage("&c${target.name}'s &agems are ${gems.shorthand()}".mm())
}