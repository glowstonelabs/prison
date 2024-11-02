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
import wtf.amari.prison.utils.format
import wtf.amari.prison.utils.mm

fun set(executor: Player, targetName: String?, amount: Double) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        dao.updatePlayerCurrency(it.uniqueId.toString(), 0, amount, 0)
        executor.sendMessage("&aSet &c${it.name}'s &abalance to ${amount.format()}".mm())
    }
}

fun add(executor: Player, targetName: String?, amount: Double) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val currentBalance = dao.getBalance(it.uniqueId.toString()) ?: 0.0
        dao.updatePlayerCurrency(it.uniqueId.toString(), 0, currentBalance + amount, 0)
        executor.sendMessage("&aAdded ${amount.format()} &ato &c${it.name}'s &abalance".mm())
    }
}

fun remove(executor: Player, targetName: String?, amount: Double) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val currentBalance = dao.getBalance(it.uniqueId.toString()) ?: 0.0
        if (currentBalance < amount) {
            executor.sendMessage("&cCannot remove &a${amount.format()} &cfrom &c${it.name}'s &cbalance. Insufficient funds.".mm())
        } else {
            dao.updatePlayerCurrency(it.uniqueId.toString(), 0, currentBalance - amount, 0)
            executor.sendMessage("&aRemoved ${amount.format()} &afrom &c${it.name}'s &abalance".mm())
        }
    }
}

private fun findPlayer(targetName: String?, executor: Player): Player? {
    return if (targetName == null) {
        executor.sendMessage("&cYou must specify a player.".mm())
        null
    } else {
        Bukkit.getPlayer(targetName) ?: run {
            executor.sendMessage("&cPlayer not found.".mm())
            null
        }
    }
}