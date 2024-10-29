package wtf.amari.prison.pickaxe

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import wtf.amari.prison.Prison
import wtf.amari.prison.utils.mm

class PickaxeManager {
    fun givePickaxe(target: Player) {
        val pickaxe = ItemStack(Material.DIAMOND_PICKAXE)
        val nbt = NBTItem(pickaxe).apply {
            setInteger("pickaxeLevel", 1)
            setInteger("fortune", 1)
            setInteger("tokenFinder", 2)
            setBoolean("isPrisonPickaxe", true)
            applyNBT(pickaxe)
        }

        val config = Prison.instance.config
        val meta = pickaxe.itemMeta ?: return

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

        meta.addEnchant(org.bukkit.enchantments.Enchantment.EFFICIENCY, 100, true)
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS)
        meta.isUnbreakable = true
        pickaxe.itemMeta = meta

        target.inventory.addItem(pickaxe)
        target.playSound(target.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
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
