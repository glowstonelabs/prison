@file:Command(
    "gamemode",
    description = "Changes Gamemode.",
    usage = "Invalid usage. /gamemode (adventure|creative|spectator|survival) [player]",
    permission = "prison.gamemode",
    permissionMessage = "You need prison.gamemode to do that!"
)

package wtf.amari.prison.commands.staff

import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import wtf.amari.prison.utils.mm

fun gamemode(executor: Player, mode: String) {
    val gameMode = getGameMode(mode, executor) ?: return
    executor.gameMode = gameMode
    executor.sendMessage("&aGamemode set to &c${gameMode.name.lowercase()}&a!".mm())
}

fun gamemode(executor: Player, mode: String, targetName: String?) {
    val target = targetName?.let { Bukkit.getPlayer(it) } ?: executor
    val gameMode = getGameMode(mode, executor) ?: return
    target?.let {
        it.gameMode = gameMode
        executor.sendMessage("&aSet gamemode of &c${it.name} &ato &c${gameMode.name.lowercase()}&a!".mm())
    } ?: executor.sendMessage("&cPlayer not found.".mm())
}

private fun getGameMode(mode: String, executor: Player): GameMode? {
    return when (mode.lowercase()) {
        "adventure", "a" -> GameMode.ADVENTURE
        "creative", "c" -> GameMode.CREATIVE
        "spectator", "sp" -> GameMode.SPECTATOR
        "survival", "s" -> GameMode.SURVIVAL
        else -> {
            executor.sendMessage("Invalid gamemode.")
            null
        }
    }
}