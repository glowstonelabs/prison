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

fun tokens(executor: Player) {
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(executor.uniqueId.toString())
    val tokens = playerCurrency?.get("tokens") as? Int ?: 0

    executor.sendMessage("&f${executor.name}'s &atokens are ${tokens.shorthand()}".mm())
}

fun tokens(executor: Player, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: executor
    if (target == null) {
        executor.sendMessage("&cPlayer not found.".mm())
        return
    }
    val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
    val playerCurrency = dao.getPlayerCurrency(target.uniqueId.toString())
    val tokens = playerCurrency?.get("tokens") as? Int ?: 0

    executor.sendMessage("&c${target.name}'s &atokens are ${tokens.shorthand()}".mm())
}