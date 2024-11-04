@file:Command(
    "gems",
    description = "Shows the player's gems.",
    usage = "Invalid usage. /gems, /gems <player>",
    permission = "zela.gems",
    permissionMessage = "You need zela.gems to do that!"
)

package wtf.amari.prison.economy.commands.gems

import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import wtf.amari.prison.databases.DatabaseManager
import wtf.amari.prison.databases.PlayerCurrencyDAO
import wtf.amari.prison.utils.mm
import wtf.amari.prison.utils.shorthand

fun gems(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(executor.uniqueId.toString())
    val gems = playerCurrency?.get("gems") as? Int ?: 0

    executor.sendMessage("&f${executor.name}'s &agems are ${gems.shorthand()}".mm())
}

fun gems(executor: Player, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: executor
    if (target == null) {
        executor.sendMessage("&cPlayer not found.".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(target.uniqueId.toString())
    val gems = playerCurrency?.get("gems") as? Int ?: 0

    executor.sendMessage("&c${target.name}'s &agems are ${gems.shorthand()}".mm())
}