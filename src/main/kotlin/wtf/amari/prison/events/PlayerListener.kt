package wtf.amari.prison.events

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit.broadcast
import org.bukkit.Bukkit.getScheduler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import wtf.amari.prison.Prison
import wtf.amari.prison.Prison.Companion.config
import wtf.amari.prison.utils.mm

class PlayerListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val scheduler = getScheduler()
        val instance = Prison.instance
        val config = instance.config

        if (!player.hasPlayedBefore()) {
            scheduler.runTaskLater(instance, Runnable {
                broadcast(" ".mm())
                broadcast("<#1db4d5>│ &rWelcome <#1db4d5>${player.name} &fto <#1db4d5>&l&nZelaMC&r for the very first time!".mm())
            }, 20L)
        } else {
            val joinmessage = config.getString("messages.joinmessage")
            if (joinmessage != null) {
                event.joinMessage(joinmessage.replace("%player%", player.name).mm())
            }
        }

        val joinMessages = listOf(
            " ",
            " &fWelcome to <#1db4d5>&lZelaMC&r!",
            " ",
            "<#1d90d5>│ &rIP: <#1db4d5>play.Zelamc.net",
            "<#1d90d5>│ &rStore: <#1db4d5>store.Zelamc.net",
            "<#1d90d5>│ &rDiscord: <#1db4d5>discord.Zelamc.net",
            " "
        )

        scheduler.runTaskLater(instance, Runnable {
            joinMessages.forEach { message -> player.sendMessage(message.mm()) }
        }, 20L)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val leavemessage = config.getString("messages.joinmessage")
        if (leavemessage != null) {
            event.quitMessage(leavemessage.replace("%player%", player.name).mm())
        }
    }
}