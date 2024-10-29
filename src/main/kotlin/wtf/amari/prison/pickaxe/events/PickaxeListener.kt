package wtf.amari.prison.pickaxe.events

import de.tr7zw.nbtapi.NBTItem
import me.tech.mcchestui.utils.openGUI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import wtf.amari.prison.pickaxe.PickaxeManager
import wtf.amari.prison.pickaxe.menus.createEnchantGUI
import wtf.amari.prison.utils.mm

class PickaxeListener : Listener {
    private val pickaxeManager = PickaxeManager()

    @EventHandler
    private fun onRightClick(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            val player = event.player
            val item = player.inventory.itemInMainHand

            if (item.type.isAir) {
                player.sendMessage("You are not holding any item.".mm())
                return
            }

            val nbt = NBTItem(item)

            if (nbt.hasTag("isPrisonPickaxe")) {
                player.openGUI(createEnchantGUI(player, pickaxeManager))
            }
        }
    }
}
