package wtf.amari.prison.pickaxe.enchantments

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import wtf.amari.prison.pickaxe.PickaxeManager
import wtf.amari.prison.utils.mm

class JackHammer(private val pickaxeManager: PickaxeManager) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        // Validate if the item is a diamond pickaxe and a custom prison pickaxe
        if (item.type == Material.DIAMOND_PICKAXE && pickaxeManager.isPrisonPickaxe(item)) {
            val nbt = NBTItem(item)
            val jackhammerLevel = nbt.getInteger("jackhammer")

            // Perform the jackhammer effect if the level is greater than 0
            if (jackhammerLevel > 0) {
                val block = event.block
                val world = block.world
                val radius = jackhammerLevel

                // Break blocks in a cubic area around the broken block
                for (x in -radius..radius) {
                    for (y in -radius..radius) {
                        for (z in -radius..radius) {
                            val relativeBlock = world.getBlockAt(block.x + x, block.y + y, block.z + z)
                            if (relativeBlock.type != Material.AIR) {
                                relativeBlock.breakNaturally(item)
                            }
                        }
                    }
                }
                player.sendMessage("JackHammer activated! Level: $jackhammerLevel".mm())
            }
        } else {
            player.sendMessage("You need to hold a custom prison pickaxe to use JackHammer.".mm())
        }
    }
}