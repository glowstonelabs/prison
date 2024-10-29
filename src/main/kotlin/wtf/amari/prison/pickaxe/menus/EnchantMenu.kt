package wtf.amari.prison.pickaxe.menus

import de.tr7zw.nbtapi.NBTItem
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Bukkit.broadcast
import org.bukkit.Material
import org.bukkit.entity.Player
import wtf.amari.prison.Prison
import wtf.amari.prison.pickaxe.events.PickaxeListener
import wtf.amari.prison.utils.mm

fun createEnchantGUI(player: Player): GUI {
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
                    player.openGUI(fortuneUpgradeGUI(player))
                }
            }
        }
    }
}

fun fortuneUpgradeGUI(player: Player): GUI {
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
                    if (itemInHand.type == Material.DIAMOND_PICKAXE) {
                        val nbt = NBTItem(itemInHand)
                        if (nbt.getBoolean("isPrisonPickaxe")) {
                            val fortune = nbt.getInteger("fortune") + 1
                            PickaxeListener().updatePickaxeNBT(itemInHand, "fortune", fortune)
                            PickaxeListener().updatePickaxeMeta(player, itemInHand)
                            broadcast("&aFortune Level $fortune".mm())
                        } else {
                            player.sendMessage("This is not a prison pickaxe.".mm())
                        }
                    } else {
                        player.sendMessage("You are not holding a pickaxe.".mm())
                    }
                    it.closeInventory()
                }
            }
        }
    }
}