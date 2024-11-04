package wtf.amari.prison.pickaxe.events

import de.tr7zw.nbtapi.NBTItem
import me.tech.mcchestui.utils.openGUI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import wtf.amari.prison.Prison
import wtf.amari.prison.pickaxe.PickaxeManager
import wtf.amari.prison.pickaxe.menus.createEnchantGUI
import wtf.amari.prison.utils.mm

class PickaxeListener : Listener {
    private val pickaxeManager = PickaxeManager(Prison.instance)

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        // Check if the action is a right-click and the item is a pickaxe
        if ((event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) && item.type.name.endsWith(
                "_PICKAXE"
            )
        ) {
            val nbt = NBTItem(item)

            // Check if the pickaxe is a custom prison pickaxe
            if (nbt.getBoolean("isPrisonPickaxe")) {
                player.openGUI(createEnchantGUI(player, pickaxeManager))
            } else {
                player.sendMessage("This is not a custom prison pickaxe.".mm())
            }
        }
    }
}