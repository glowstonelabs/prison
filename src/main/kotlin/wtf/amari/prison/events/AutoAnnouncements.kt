package wtf.amari.prison.events

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import wtf.amari.prison.Prison
import wtf.amari.prison.utils.mm

class AutoAnnouncements {

    // Map of announcement categories to their respective messages
    private val announcements = mapOf(
        "HELP" to listOf(
            "<#1d90d5>│ <#1db4d5>&lARE YOU STUCK?&r",
            "<#1d90d5>│ &rIf you need help with anything be sure to check",
            "<#1d90d5>│ &rour help menu for a list of our features!",
            "<#1d90d5>│ &r",
            "<#1d90d5>│ &rOpen using <#1db4d5>&l&n/help"
        )
        // Add more categories and messages as needed
    )

    init {
        startAnnouncements()
    }

    /**
     * Starts the announcement task that broadcasts a random announcement
     * from a random category every 5 minutes.
     */
    private fun startAnnouncements() {
        object : BukkitRunnable() {
            override fun run() {
                // Select a random category and its corresponding messages
                val randomCategory = announcements.keys.randomOrNull()
                val randomAnnouncement = randomCategory?.let { announcements[it]?.joinToString("\n") }

                // Broadcast the announcement if it exists
                randomAnnouncement?.let { Bukkit.broadcast(it.mm()) }
            }
        }.runTaskTimer(Prison.instance, 0L, 6000L) // 6000L ticks = 5 minutes
    }
}