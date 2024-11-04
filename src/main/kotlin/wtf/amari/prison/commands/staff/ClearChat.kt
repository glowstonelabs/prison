@file:Command(
    "clearchat",
    "chatclear",
    description = "Clears the chat",
    usage = "Invalid usage. /clearchat",
    permission = "prison.admin.clearchat",
    permissionMessage = "You need prison.admin.clearchat to do that!"
)

package wtf.amari.prison.commands.staff

import me.honkling.commando.common.annotations.Command
import org.bukkit.Bukkit.broadcast
import org.bukkit.entity.Player
import wtf.amari.prison.utils.mm

fun clearChat(executor: Player) {
    repeat(100) { broadcast(" ".mm()) }
    broadcast("&cChat has been cleared by &e${executor.name}".mm())
}