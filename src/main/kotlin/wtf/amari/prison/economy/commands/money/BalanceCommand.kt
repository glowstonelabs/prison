@file:Command(
    "balance", "bal",
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
import wtf.amari.prison.utils.format
import wtf.amari.prison.utils.mm

fun balance(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val balance = dao.getBalance(executor.uniqueId.toString())
    if (balance != null) {
        executor.sendMessage("&f${executor.name}'s &abalance is ${balance.format()}".mm())
    } else {
        executor.sendMessage("&cCould not retrieve balance.".mm())
    }
}

fun balance(executor: Player, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: executor
    if (target == null) {
        executor.sendMessage("&cPlayer not found.".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val balance = dao.getBalance(target.uniqueId.toString())
    if (balance != null) {
        executor.sendMessage("&c${target.name}'s &abalance is ${balance.format()}".mm())
    } else {
        executor.sendMessage("&cCould not retrieve balance.".mm())
    }
}