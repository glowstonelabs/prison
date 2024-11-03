package wtf.amari.prison.pickaxe.menus

import de.tr7zw.nbtapi.NBTItem
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import wtf.amari.prison.Prison
import wtf.amari.prison.pickaxe.PickaxeManager
import wtf.amari.prison.utils.mm

fun createEnchantGUI(player: Player, pickaxeManager: PickaxeManager): GUI {
    return gui(
        plugin = Prison.instance,
        title = "<#36393F>&lEnchants".mm(),
        type = GUIType.Chest(rows = 3)
    ) {
        all { item = item(Material.BLACK_STAINED_GLASS_PANE) { name = " ".mm() } }
        slot(14, 1) {
            item = item(Material.BELL) {
                name = "<#7289DA>&lFortune".mm()
                lore = listOf(" ".mm(), "<#F5F5F5>FORT ENCHANT".mm())
                glowing = true
                onClick {
                    it.closeInventory()
                    player.openGUI(fortuneUpgradeGUI(player, pickaxeManager))
                }
            }
        }
    }
}

fun fortuneUpgradeGUI(player: Player, pickaxeManager: PickaxeManager): GUI {
    return gui(
        plugin = Prison.instance,
        title = "<#36393F>&lUpgrade".mm(),
        type = GUIType.Chest(rows = 3)
    ) {
        all { item = item(Material.BLACK_STAINED_GLASS_PANE) { name = " ".mm() } }
        slot(14, 1) {
            item = item(Material.BELL) {
                name = "<#7289DA>&l1 Level".mm()
                lore = listOf(" ".mm(), "<#F5F5F5>Test UPGRADE".mm())
                glowing = true
                onClick {
                    val itemInHand = player.inventory.itemInMainHand
                    if (isPrisonPickaxe(itemInHand)) {
                        val nbt = NBTItem(itemInHand)
                        val fortune = nbt.getInteger("fortune") + 1
                        pickaxeManager.updatePickaxeNBT(itemInHand, "fortune", fortune)
                        pickaxeManager.updatePickaxeMeta(player, itemInHand)
                        player.sendMessage("&aFortune Level $fortune".mm()) // Feedback to the player
                    } else {
                        player.sendMessage("You are not holding a prison pickaxe.".mm())
                    }
                    it.closeInventory()
                }
            }
        }
    }
}

// Helper function to check if the item is a prison pickaxe
private fun isPrisonPickaxe(item: ItemStack): Boolean {
    val nbt = NBTItem(item)
    return nbt.getBoolean("isPrisonPickaxe")
}