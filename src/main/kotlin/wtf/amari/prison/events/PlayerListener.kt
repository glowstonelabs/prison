package wtf.amari.prison.events

import org.bukkit.Bukkit.broadcast
import org.bukkit.Bukkit.getScheduler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import wtf.amari.prison.Prison
import wtf.amari.prison.databases.DatabaseManager
import wtf.amari.prison.databases.PlayerCurrencyDAO
import wtf.amari.prison.utils.createSidebarForPlayer
import wtf.amari.prison.utils.mm

class PlayerListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val scheduler = getScheduler()
        val instance = Prison.instance
        val config = instance.config
        val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())

        createSidebarForPlayer(player)

        config.getString("messages.join")?.let {
            event.joinMessage(it.replace("%player%", player.name).mm())
        }

        if (!player.hasPlayedBefore()) {
            dao.setInitialBalance(player.uniqueId.toString(), 1000.0)
            scheduler.runTaskLater(instance, Runnable {
                config.getString("messages.firstjoin")?.let {
                    broadcast("".mm())
                    broadcast(it.replace("%player%", player.name).mm())
                }
            }, 20L)
        }

        scheduler.runTaskLater(instance, Runnable {
            config.getStringList("messages.welcome-messages").forEach { message ->
                player.sendMessage(message.mm())
            }
        }, 20L)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val config = Prison.instance.config
        val quitMessage = config.getString("messages.quit")
        event.quitMessage(
            if (quitMessage.isNullOrEmpty()) null else quitMessage.replace("%player%", event.player.name).mm()
        )
    }
}