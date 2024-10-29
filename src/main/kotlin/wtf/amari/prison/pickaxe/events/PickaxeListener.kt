package wtf.amari.prison.pickaxe.events

import de.tr7zw.nbtapi.NBTItem
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Bukkit.getLogger
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import wtf.amari.prison.Prison
import wtf.amari.prison.pickaxe.menus.createEnchantGUI
import wtf.amari.prison.utils.mm

class PickaxeListener : Listener {
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
            getLogger().info("Interacted with item: ${nbt.asNBTString()}")

            if (nbt.hasTag("isPrisonPickaxe")) {
                player.openGUI(createEnchantGUI(player))
            }
        }
    }

    fun updatePickaxeNBT(item: ItemStack, key: String, value: Int) {
        NBTItem(item).apply {
            setInteger(key, value)
            applyNBT(item)
        }
    }

    fun updatePickaxeMeta(player: Player, item: ItemStack) {
        if (item.type == Material.DIAMOND_PICKAXE) {
            val nbt = NBTItem(item)
            if (nbt.getBoolean("isPrisonPickaxe")) {
                val config = Prison.instance.config
                val meta = item.itemMeta ?: return

                meta.displayName(
                    config.getString("pickaxe.name")
                        ?.replace("%pickaxelevel%", nbt.getInteger("pickaxeLevel").toString())
                        ?.mm() ?: "Prison Pickaxe".mm()
                )

                meta.lore(config.getStringList("pickaxe.lore").map {
                    it.replace("%fortune%", nbt.getInteger("fortune").toString())
                        .replace("%tokenfinder%", nbt.getInteger("tokenFinder").toString())
                        .mm()
                })

                item.itemMeta = meta
                player.inventory.setItemInMainHand(item)
            }
        } else {
            player.sendMessage("You are not holding a pickaxe.".mm())
        }
    }
}