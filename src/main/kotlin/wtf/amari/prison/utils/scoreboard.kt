package wtf.amari.prison.utils

import net.kyori.adventure.text.Component
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar
import org.bukkit.entity.Player
import wtf.amari.prison.Prison

fun createSidebarForPlayer(player: Player) {
    val scoreboardLibrary = Prison.instance.scoreboardLibrary ?: return

    val sidebar: Sidebar = scoreboardLibrary.createSidebar()

    sidebar.title(Component.text("Sidebar Title"))
    sidebar.line(0, Component.empty())
    sidebar.line(1, Component.text("Line 1"))
    sidebar.line(2, Component.text("Line 2"))
    sidebar.line(3, Component.empty())
    sidebar.line(4, Component.text("epicserver.net"))

    sidebar.addPlayer(player) // Add the player to the sidebar

    // Don't forget to call sidebar.close() once you don't need it anymore!
}