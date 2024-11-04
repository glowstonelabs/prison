@file:Command(
    "gemeco",
    description = "Modify the player's gems",
    usage = "Invalid usage. /gemeco set <player> <amount>, /gemeco add <player> <amount>, /gemeco remove <player> <amount>",
    permission = "prison.gemeco.modify",
    permissionMessage = "You need prison.gemeco.modify to do that!"
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
 * Sets the specified player's gems to the given amount.
 *
 * @param executor The player executing the command.
 * @param targetName The name of the target player.
 * @param amountStr The amount to set the gems to.
 */
fun set(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        dao.updatePlayerCurrency(it.uniqueId.toString(), null, null, amount)
        executor.sendMessage("&aSet &c${it.name}'s &agems to ${amount.shorthand()}".mm())
    }
}

/**
 * Adds the specified amount of gems to the target player's balance.
 *
 * @param executor The player executing the command.
 * @param targetName The name of the target player.
 * @param amountStr The amount to add to the gems.
 */
fun add(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val currentGems = dao.getPlayerCurrency(it.uniqueId.toString())?.get("gems") as? Int ?: 0
        dao.updatePlayerCurrency(it.uniqueId.toString(), null, null, currentGems + amount)
        executor.sendMessage("&aAdded ${amount.shorthand()} &ato &c${it.name}'s &agems".mm())
    }
}

/**
 * Removes the specified amount of gems from the target player's balance.
 *
 * @param executor The player executing the command.
 * @param targetName The name of the target player.
 * @param amountStr The amount to remove from the gems.
 */
fun remove(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val currentGems = dao.getPlayerCurrency(it.uniqueId.toString())?.get("gems") as? Int ?: 0
        if (currentGems < amount) {
            executor.sendMessage("&cCannot remove &a${amount.shorthand()} &cfrom &c${it.name}'s &cgems. Insufficient funds.".mm())
        } else {
            dao.updatePlayerCurrency(it.uniqueId.toString(), null, null, currentGems - amount)
            executor.sendMessage("&aRemoved ${amount.shorthand()} &afrom &c${it.name}'s &agems".mm())
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