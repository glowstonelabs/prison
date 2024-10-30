package wtf.amari.prison.pickaxe.enchantments

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import wtf.amari.prison.pickaxe.PickaxeManager
import wtf.amari.prison.utils.mm

class Fortune(private val pickaxeManager: PickaxeManager) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if (pickaxeManager.isPrisonPickaxe(item)) {
            val fortuneLevel = pickaxeManager.getFortuneLevel(item)

            // Example logic to increase drop count based on fortune level
            val extraDrops = (1..fortuneLevel).random()
            for (i in 0 until extraDrops) {
                event.block.world.dropItemNaturally(event.block.location, event.block.drops.first())
            }

            player.sendMessage("&aFortune activated with level $fortuneLevel!".mm())
        }
    }
}
