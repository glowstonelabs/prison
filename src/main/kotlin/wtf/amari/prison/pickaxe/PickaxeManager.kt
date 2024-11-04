package wtf.amari.prison.pickaxe

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import wtf.amari.prison.Prison
import wtf.amari.prison.pickaxe.enchantments.Fortune
import wtf.amari.prison.pickaxe.enchantments.JackHammer
import wtf.amari.prison.utils.mm

class PickaxeManager(private val plugin: Plugin) {

    init {
        registerEnchantListeners()
    }

    /**
     * Registers enchantment listeners.
     */
    private fun registerEnchantListeners() {
        val listeners = listOf(
            Fortune(this),
            JackHammer(this)
        )

        listeners.forEach { listener ->
            Bukkit.getPluginManager().registerEvents(listener, plugin)
        }
    }

    /**
     * Gives a custom pickaxe to the target player.
     */
    fun givePickaxe(target: Player) {
        val pickaxe = createPickaxe()
        target.inventory.addItem(pickaxe)
        target.playSound(target.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        target.sendMessage("You have received a custom pickaxe!".mm())
    }

    /**
     * Creates a custom pickaxe with NBT data and meta.
     */
    private fun createPickaxe(): ItemStack {
        val pickaxe = ItemStack(Material.DIAMOND_PICKAXE)
        val nbt = NBTItem(pickaxe).apply {
            setInteger("pickaxeLevel", 1)
            setInteger("fortune", 1)
            setInteger("tokenFinder", 2)
            setBoolean("isPrisonPickaxe", true)
            applyNBT(pickaxe)
        }

        val config = Prison.instance.config
        val meta = pickaxe.itemMeta ?: return pickaxe

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

        return pickaxe
    }

    /**
     * Updates the NBT data of a pickaxe.
     */
    fun updatePickaxeNBT(item: ItemStack, key: String, value: Int) {
        NBTItem(item).apply {
            setInteger(key, value)
            applyNBT(item)
        }
    }

    /**
     * Updates the meta data of a pickaxe held by the player.
     */
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
                meta.addEnchant(org.bukkit.enchantments.Enchantment.EFFICIENCY, 100, true)
                meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS)
                meta.isUnbreakable = true

                item.itemMeta = meta
                player.inventory.setItemInMainHand(item)
                player.sendMessage("Your pickaxe has been updated!".mm())
            }
        } else {
            player.sendMessage("You are not holding a pickaxe.".mm())
        }
    }

    /**
     * Checks if the item is a custom prison pickaxe.
     */
    fun isPrisonPickaxe(item: ItemStack?): Boolean {
        if (item == null || item.type == Material.AIR || item.amount == 0) {
            return false
        }
        return NBTItem(item).getBoolean("isPrisonPickaxe")
    }

    /**
     * Gets the fortune level of the pickaxe.
     */
    fun getFortuneLevel(item: ItemStack): Int {
        return NBTItem(item).getInteger("fortune")
    }
}