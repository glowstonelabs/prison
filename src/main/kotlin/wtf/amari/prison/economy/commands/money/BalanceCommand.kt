@file:Command(
    "balance",
    description = "Shows the player's balance.",
    usage = "Invalid usage. /balance, /balance <player>",
    permission = "zela.balance",
    permissionMessage = "You need zela.balance to do that!"
)

package wtf.amari.prison.economy.commands.money

import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import wtf.amari.prison.databases.DatabaseManager
import wtf.amari.prison.databases.PlayerCurrencyDAO
import wtf.amari.prison.utils.mm
import wtf.amari.prison.utils.shorthand

/**
 * Retrieves and displays the balance of the executor.
 *
 * @param executor The player executing the command.
 */
fun balance(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val money = dao.getPlayerCurrency(executor.uniqueId.toString())?.get("money") as? Int ?: 0

    executor.sendMessage("&f${executor.name}'s &abalance is ${money.shorthand()}".mm())
}

/**
 * Retrieves and displays the balance of the specified player or the executor if no player is specified.
 *
 * @param executor The player executing the command.
 * @param targetName The name of the target player.
 */
fun balance(executor: Player, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: executor
    if (target == null) {
        executor.sendMessage("&cPlayer not found.".mm())
        return
    }

    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val money = dao.getPlayerCurrency(target.uniqueId.toString())?.get("money") as? Int ?: 0

    executor.sendMessage("&c${target.name}'s &abalance is ${money.shorthand()}".mm())
}