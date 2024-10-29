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
                event.joinMessage(config.getString("messages.firstjoin")?.replace("%player%", player.name)?.mm())
                broadcast(" ".mm())
            }, 20L)
        } else {
            config.getString("messages.join")?.let {
                event.joinMessage(it.replace("%player%", player.name).mm())
            }
        }

        scheduler.runTaskLater(instance, Runnable {
            config.getStringList("messages.welcome-message").forEach { message ->
                player.sendMessage(message.mm())
            }
        }, 20L)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val config = Prison.instance.config
        val quitmessage = config.getString("messages.quit")
        event.quitMessage(
            if (quitmessage.isNullOrEmpty()) null else quitmessage.replace("%player%", event.player.name).mm()
        )
    }
}