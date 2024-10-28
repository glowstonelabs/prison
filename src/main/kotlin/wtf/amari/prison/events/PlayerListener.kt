package wtf.amari.prison.events

import org.bukkit.Bukkit.broadcast
import org.bukkit.Bukkit.getScheduler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import wtf.amari.prison.Prison
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

        val joinMessages = config.getStringList("messages.welcome-messages")

        scheduler.runTaskLater(instance, Runnable {
            joinMessages.forEach { message -> player.sendMessage(message.mm()) }
        }, 20L)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val instance = Prison.instance
        val config = instance.config
        val player = event.player
        val quitmessage = config.getString("messages.quitmessage")
        event.quitMessage(
            if (quitmessage.isNullOrEmpty()) null else quitmessage.replace("%player%", event.player.name).mm()
        )
    }
}
