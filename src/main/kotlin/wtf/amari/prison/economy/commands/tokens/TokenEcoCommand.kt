@file:Command(
    "tokeneco",
    description = "Modify the player's tokens",
    usage = "Invalid usage. /tokeneco set <player> <amount>, /tokeneco add <player> <amount>, /tokeneco remove <player> <amount>",
    permission = "prison.tokeneco.modify",
    permissionMessage = "You need prison.tokeneco.modify to do that!"
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
 * Sets the specified player's tokens to the given amount.
 *
 * @param executor The player executing the command.
 * @param targetName The name of the target player.
 * @param amountStr The amount to set the tokens to.
 */
fun set(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        dao.updatePlayerCurrency(it.uniqueId.toString(), amount, null, null)
        executor.sendMessage("&aSet &c${it.name}'s &atokens to ${amount.shorthand()}".mm())
    }
}

/**
 * Adds the specified amount of tokens to the target player's balance.
 *
 * @param executor The player executing the command.
 * @param targetName The name of the target player.
 * @param amountStr The amount to add to the tokens.
 */
fun add(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val currentTokens = dao.getPlayerCurrency(it.uniqueId.toString())?.get("tokens") as? Int ?: 0
        dao.updatePlayerCurrency(it.uniqueId.toString(), currentTokens + amount, null, null)
        executor.sendMessage("&aAdded ${amount.shorthand()} &ato &c${it.name}'s &atokens".mm())
    }
}

/**
 * Removes the specified amount of tokens from the target player's balance.
 *
 * @param executor The player executing the command.
 * @param targetName The name of the target player.
 * @param amountStr The amount to remove from the tokens.
 */
fun remove(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val currentTokens = dao.getPlayerCurrency(it.uniqueId.toString())?.get("tokens") as? Int ?: 0
        if (currentTokens < amount) {
            executor.sendMessage("&cCannot remove &a${amount.shorthand()} &cfrom &c${it.name}'s &ctokens. Insufficient funds.".mm())
        } else {
            dao.updatePlayerCurrency(it.uniqueId.toString(), currentTokens - amount, null, null)
            executor.sendMessage("&aRemoved ${amount.shorthand()} &afrom &c${it.name}'s &atokens".mm())
        }
    }
}

/**
 * Finds the player by name or returns null if not found.
 *
 * @param targetName The name of the target player.
 * @param executor The player executing the command.
 * @return The target player or null if not found.
 */
private fun findPlayer(targetName: String?, executor: Player): Player? {
    if (targetName.isNullOrBlank()) {
        executor.sendMessage("&cYou must specify a player.".mm())
        return null
    }
    return Bukkit.getPlayer(targetName) ?: run {
        executor.sendMessage("&cPlayer not found.".mm())
        null
    }
}