@file:Command(
    "givepickaxe",
    description = "givepickaxe",
    usage = "/givepickaxe (player)",
    permission = "prison.admin.pickaxe",
    permissionMessage = "You need prison.admin.pickaxe to do that!"
)

package wtf.amari.prison.pickaxe

import de.tr7zw.nbtapi.NBTItem
import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit.getPlayer
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import wtf.amari.prison.Prison
import wtf.amari.prison.utils.mm

fun givepickaxe(executor: Player, targetName: String?) {
    val target = targetName?.let { getPlayer(it) }
    if (target != null) {
        val pickaxe = ItemStack(Material.DIAMOND_PICKAXE).apply {
            val meta = itemMeta
            val instance = Prison.instance
            val config = instance.config
            val nbt = NBTItem(this)

            nbt.setInteger("pickaxeLevel", 1)
            nbt.setInteger("fortune", 1)
            nbt.setInteger("tokenFinder", 2)
            nbt.setBoolean("isPrisonPickaxe", true)


            meta.displayName(
                config.getString("pickaxe.name")?.replace("%pickaxelevel%", nbt.getInteger("pickaxeLevel").toString())
                    ?.mm()
            )

            meta.lore(config.getStringList("pickaxe.lore").map { loreLine ->
                loreLine
                    .replace("%fortune%", nbt.getInteger("fortune").toString())
                    .replace("%tokenfinder%", nbt.getInteger("tokenFinder").toString())
            }.map { it.mm() })


            itemMeta = meta

            meta.addEnchant(org.bukkit.enchantments.Enchantment.EFFICIENCY, 100, true)
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS)
            meta.isUnbreakable = true
        }

        target.inventory.addItem(pickaxe)
        target.playSound(target.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
    } else {
        executor.sendMessage("&cPlayer not found!".mm())
    }
}
