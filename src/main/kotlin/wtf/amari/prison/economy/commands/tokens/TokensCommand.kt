@file:Command(
    "tokens",
    description = "Shows the player's tokens.",
    usage = "Invalid usage. /tokens, /tokens <player>",
    permission = "zela.tokens",
    permissionMessage = "You need zela.tokens to do that!"
)

package wtf.amari.prison.economy.commands.tokens

import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import wtf.amari.prison.databases.DatabaseManager
import wtf.amari.prison.databases.PlayerCurrencyDAO
import wtf.amari.prison.utils.mm
import wtf.amari.prison.utils.shorthand

/**
 * Shows the tokens of the executing player.
 * @param executor The player executing the command.
 */
fun tokens(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(executor.uniqueId.toString())
    val tokens = playerCurrency?.get("tokens") as? Int ?: 0

    executor.sendMessage("&f${executor.name}'s &atokens are ${tokens.shorthand()}".mm())
}

/**
 * Shows the tokens of the specified player or the executing player if no target is specified.
 * @param executor The player executing the command.
 * @param targetName The target player's name.
 */
fun tokens(executor: Player, targetName: String?) {
    val target = findPlayer(targetName, executor) ?: return
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(target.uniqueId.toString())
    val tokens = playerCurrency?.get("tokens") as? Int ?: 0

    executor.sendMessage("&c${target.name}'s &atokens are ${tokens.shorthand()}".mm())
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