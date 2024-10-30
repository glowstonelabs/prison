@file:Command(
    "givepickaxe",
    description = "Gives a custom prison pickaxe to a player.",
    usage = "/givepickaxe <player>",
    permission = "prison.admin.pickaxe",
    permissionMessage = "You need prison.admin.pickaxe to execute this command!"
)

package wtf.amari.prison.pickaxe.commands

import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import wtf.amari.prison.Prison
import wtf.amari.prison.pickaxe.PickaxeManager
import wtf.amari.prison.utils.mm


fun givePickaxe(executor: Player, targetName: String?) {
    // Check if the command executor provided a target player name
    if (targetName == null) {
        executor.sendMessage("&cPlease specify a player.".mm())
        return
    }

    val target = Bukkit.getPlayer(targetName)
    if (target != null) {
        // Pass the plugin instance to the PickaxeManager constructor
        val pickaxeManager = PickaxeManager(Prison.instance)
        pickaxeManager.givePickaxe(target)
        executor.sendMessage("&aSuccessfully gave a custom pickaxe to ${target.name}.".mm())
    } else {
        executor.sendMessage("&cPlayer not found!".mm())
    }
}

