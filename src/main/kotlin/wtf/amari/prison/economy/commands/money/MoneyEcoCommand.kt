@file:Command(
    "moneyeco",
    description = "Modify the player's balance",
    usage = "Invalid usage. /moneyeco set <player> <amount>, /moneyeco add <player> <amount>, /moneyeco remove <player> <amount>",
    permission = "prison.moneyeco.modify",
    permissionMessage = "You need prison.moneyeco.modify to do that!"
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
 * Sets the player's balance to a specified amount.
 * @param executor The player executing the command.
 * @param targetName The target player's name.
 * @param amountStr The amount to set.
 */
fun set(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        dao.updatePlayerCurrency(it.uniqueId.toString(), null, amount, null)
        executor.sendMessage("&aSet &c${it.name}'s &abalance to ${amount.shorthand()}".mm())
    }
}

/**
 * Adds a specified amount to the player's balance.
 * @param executor The player executing the command.
 * @param targetName The target player's name.
 * @param amountStr The amount to add.
 */
fun add(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val currentBalance = dao.getPlayerCurrency(it.uniqueId.toString())?.get("money") as? Int ?: 0
        dao.updatePlayerCurrency(it.uniqueId.toString(), null, currentBalance + amount, null)
        executor.sendMessage("&aAdded ${amount.shorthand()} &ato &c${it.name}'s &abalance".mm())
    }
}

/**
 * Removes a specified amount from the player's balance.
 * @param executor The player executing the command.
 * @param targetName The target player's name.
 * @param amountStr The amount to remove.
 */
fun remove(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val currentBalance = dao.getPlayerCurrency(it.uniqueId.toString())?.get("money") as? Int ?: 0
        if (currentBalance < amount) {
            executor.sendMessage("&cCannot remove &a${amount.shorthand()} &cfrom &c${it.name}'s &cbalance. Insufficient funds.".mm())
        } else {
            dao.updatePlayerCurrency(it.uniqueId.toString(), null, currentBalance - amount, null)
            executor.sendMessage("&aRemoved ${amount.shorthand()} &afrom &c${it.name}'s &abalance".mm())
        }
    }
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