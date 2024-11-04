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

fun add(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val playerCurrency = dao.getPlayerCurrency(it.uniqueId.toString())
        val currentGems = playerCurrency?.get("gems") as? Int ?: 0
        dao.updatePlayerCurrency(it.uniqueId.toString(), null, null, currentGems + amount)
        executor.sendMessage("&aAdded ${amount.shorthand()} &ato &c${it.name}'s &agems".mm())
    }
}

fun remove(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val playerCurrency = dao.getPlayerCurrency(it.uniqueId.toString())
        val currentGems = playerCurrency?.get("gems") as? Int ?: 0
        if (currentGems < amount) {
            executor.sendMessage("&cCannot remove &a${amount.shorthand()} &cfrom &c${it.name}'s &cgems. Insufficient funds.".mm())
        } else {
            dao.updatePlayerCurrency(it.uniqueId.toString(), null, null, currentGems - amount)
            executor.sendMessage("&aRemoved ${amount.shorthand()} &afrom &c${it.name}'s &agems".mm())
        }
    }
}

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