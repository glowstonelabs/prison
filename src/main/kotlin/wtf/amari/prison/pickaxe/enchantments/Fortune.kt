package wtf.amari.prison.pickaxe.enchantments

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import wtf.amari.prison.pickaxe.PickaxeManager
import wtf.amari.prison.utils.mm
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random

class Fortune(private val pickaxeManager: PickaxeManager) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        // Check if the item is a custom prison pickaxe
        if (pickaxeManager.isPrisonPickaxe(item)) {
            val fortuneLevel = pickaxeManager.getFortuneLevel(item)
            val baseChance = 0.1
            val totalChance = (baseChance * fortuneLevel).coerceAtMost(1.0) // Cap at 100%

            // Check if the fortune effect should activate
            if (Random.nextDouble() < totalChance) {
                val scalingFactor = 0.3
                val extraDrops = floor(fortuneLevel.toDouble().pow(scalingFactor)).toInt()
                val maxExtraDrops = 100
                val dropsToGenerate = extraDrops.coerceAtMost(maxExtraDrops)

                // Drop extra items
                repeat(dropsToGenerate) {
                    event.block.world.dropItemNaturally(event.block.location, event.block.drops.first())
                }

                // Inform the player about the fortune activation
                player.sendMessage("&aFortune activated with level $fortuneLevel! Extra drops: $dropsToGenerate".mm())
            }
        }
    }
}