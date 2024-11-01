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

        if (pickaxeManager.isPrisonPickaxe(item)) {
            val fortuneLevel = pickaxeManager.getFortuneLevel(item)

            val baseChance = 0.1
            val totalChance = (baseChance * fortuneLevel).coerceAtMost(1.0) // Cap at 100%

            if (Random.nextDouble() < totalChance) {
                val scalingFactor = 0.3
                val extraDrops = floor(fortuneLevel.toDouble().pow(scalingFactor)).toInt()

                val maxExtraDrops = 100
                val dropsToGenerate = extraDrops.coerceAtMost(maxExtraDrops)

                for (i in 0 until dropsToGenerate) {
                    event.block.world.dropItemNaturally(event.block.location, event.block.drops.first())
                }

                player.sendMessage("&aFortune activated with level $fortuneLevel! Extra drops: $dropsToGenerate".mm())
            }
        }
    }
}
