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
 * Retrieves and displays the tokens of the executor.
 *
 * @param executor The player executing the command.
 */
fun tokens(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val tokens = dao.getPlayerCurrency(executor.uniqueId.toString())?.get("tokens") as? Int ?: 0

    executor.sendMessage("&f${executor.name}'s &atokens are ${tokens.shorthand()}".mm())
}

/**
 * Retrieves and displays the tokens of the specified player or the executor if no player is specified.
 *
 * @param executor The player executing the command.
 * @param targetName The name of the target player.
 */
fun tokens(executor: Player, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: executor
    if (target == null) {
        executor.sendMessage("&cPlayer not found.".mm())
        return
    }

    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val tokens = dao.getPlayerCurrency(target.uniqueId.toString())?.get("tokens") as? Int ?: 0

    executor.sendMessage("&c${target.name}'s &atokens are ${tokens.shorthand()}".mm())
}