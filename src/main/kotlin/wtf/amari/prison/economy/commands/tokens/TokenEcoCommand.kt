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

fun add(executor: Player, targetName: String?, amountStr: String) {
    val amount = amountStr.toIntOrNull()
    if (amount == null) {
        executor.sendMessage("&cInvalid amount: $amountStr".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    findPlayer(targetName, executor)?.let {
        val playerCurrency = dao.getPlayerCurrency(it.uniqueId.toString())
        val currentTokens = playerCurrency?.get("tokens") as? Int ?: 0
        dao.updatePlayerCurrency(it.uniqueId.toString(), currentTokens + amount, null, null)
        executor.sendMessage("&aAdded ${amount.shorthand()} &ato &c${it.name}'s &atokens".mm())
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
        val currentTokens = playerCurrency?.get("tokens") as? Int ?: 0
        if (currentTokens < amount) {
            executor.sendMessage("&cCannot remove &a${amount.shorthand()} &cfrom &c${it.name}'s &ctokens. Insufficient funds.".mm())
        } else {
            dao.updatePlayerCurrency(it.uniqueId.toString(), currentTokens - amount, null, null)
            executor.sendMessage("&aRemoved ${amount.shorthand()} &afrom &c${it.name}'s &atokens".mm())
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