@file:Command(
    "givepickaxe",
    description = "givepickaxe",
    usage = "/givepickaxe (player)",
    permission = "prison.admin.pickaxe",
    permissionMessage = "You need prison.admin.pickaxe to do that!"
)

package wtf.amari.prison.pickaxe

import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit.getPlayer
import org.bukkit.entity.Player
import wtf.amari.prison.utils.mm

fun givepickaxe(executor: Player, targetName: String?) {
    val target = targetName?.let { getPlayer(it) }
    if (target != null) {
        val pickaxeManager = PickaxeManager()
        pickaxeManager.givePickaxe(target)
    } else {
        executor.sendMessage("&cPlayer not found!".mm())
    }
}
