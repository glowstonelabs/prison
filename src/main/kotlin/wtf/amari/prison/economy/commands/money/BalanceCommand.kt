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

fun balance(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(executor.uniqueId.toString())
    val money = playerCurrency?.get("money") as? Int ?: 0

    executor.sendMessage("&f${executor.name}'s &abalance is ${money.shorthand()}".mm())
}

fun balance(executor: Player, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: executor
    if (target == null) {
        executor.sendMessage("&cPlayer not found.".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(target.uniqueId.toString())
    val money = playerCurrency?.get("money") as? Int ?: 0

    executor.sendMessage("&c${target.name}'s &abalance is ${money.shorthand()}".mm())
}