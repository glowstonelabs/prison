package wtf.amari.prison.events

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import wtf.amari.prison.Prison
import wtf.amari.prison.utils.mm

class AutoAnnouncements {

    private val announcements = mapOf(
        "HELP" to listOf(
            "<#1d90d5>│ <#1db4d5>&lARE YOU STUCK?&r",
            "<#1d90d5>│ &rIf you need help with anything be sure to check",
            "<#1d90d5>│ &rour help menu for a list of our features!",
            "<#1d90d5>│ &r",
            "<#1d90d5>│ &rOpen using <#1db4d5>&l&n/help",


            ),
//        "DISCORD" to listOf(
//            "Join our Discord for more updates!",
//            "Chat with us on Discord!"
//        ),
//        "STUCK" to listOf(
//            "Stuck? Use /spawn to get back to the main area!",
//            "Contact an admin if you're stuck!"
//        ),
//        "ETC" to listOf(
//            "Remember to follow the rules!",
//            "Have fun and enjoy your stay!"
//        )
    )

    init {
        startAnnouncements()
    }

    private fun startAnnouncements() {
        object : BukkitRunnable() {
            override fun run() {
                val randomCategory = announcements.keys.random()
                val randomAnnouncement = announcements[randomCategory]?.joinToString("\n")
                randomAnnouncement?.let { Bukkit.broadcast(it.mm()) }
            }
        }.runTaskTimer(Prison.instance, 0L, 6000L) // 6000L ticks = 5 minutes
    }
}