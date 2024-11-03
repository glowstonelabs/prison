package wtf.amari.prison.utils

import net.kyori.adventure.text.Component
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar
import org.bukkit.entity.Player
import wtf.amari.prison.Prison

fun createSidebarForPlayer(player: Player) {
    val scoreboardLibrary = Prison.instance.scoreboardLibrary ?: return

    val sidebar: Sidebar = scoreboardLibrary.createSidebar().apply {
        title(Component.text("Sidebar Title"))
        line(0, Component.empty())
        line(1, Component.text("Line 1"))
        line(2, Component.text("Line 2"))
        line(3, Component.empty())
        line(4, Component.text("epicserver.net"))
        addPlayer(player)
    }

    sidebar.close()
}