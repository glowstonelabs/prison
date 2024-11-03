package wtf.amari.prison.pickaxe.enchantments

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import wtf.amari.prison.pickaxe.PickaxeManager

class JackHammer(private val pickaxeManager: PickaxeManager) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if (item.type == Material.DIAMOND_PICKAXE && pickaxeManager.isPrisonPickaxe(item)) {
            val nbt = NBTItem(item)
            val jackhammerLevel = nbt.getInteger("jackhammer")

            if (jackhammerLevel > 0) {
                val block = event.block
                val world = block.world
                val radius = jackhammerLevel

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
            }
        }
    }
}