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
 * Shows the balance of the executing player.
 * @param executor The player executing the command.
 */
fun balance(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(executor.uniqueId.toString())
    val money = playerCurrency?.get("money") as? Int ?: 0

    executor.sendMessage("&f${executor.name}'s &abalance is ${money.shorthand()}".mm())
}

/**
 * Shows the balance of the specified player or the executing player if no target is specified.
 * @param executor The player executing the command.
 * @param targetName The target player's name.
 */
fun balance(executor: Player, targetName: String?) {
    val target = findPlayer(targetName, executor) ?: return
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(target.uniqueId.toString())
    val money = playerCurrency?.get("money") as? Int ?: 0

    executor.sendMessage("&c${target.name}'s &abalance is ${money.shorthand()}".mm())
}

/**
 * Finds a player by their name.
 * @param targetName The target player's name.
 * @param executor The player executing the command.
 * @return The Player object if found, null otherwise.
 */
private fun findPlayer(targetName: String?, executor: Player): Player? {
    if (targetName == null) {
        executor.sendMessage("&cYou must specify a player.".mm())
        return null
    }
    return Bukkit.getPlayer(targetName) ?: run {
        executor.sendMessage("&cPlayer not found.".mm())
        null
    }
}