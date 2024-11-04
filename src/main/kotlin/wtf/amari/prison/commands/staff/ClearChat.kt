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

/**
 * Clears the chat for all players and sends a confirmation message.
 *
 * @param executor The player executing the command.
 */
fun clearChat(executor: Player) {
    // Clear the chat by broadcasting 100 empty lines
    repeat(100) { broadcast(" ".mm()) }

    // Send a confirmation message to all players
    broadcast("&cChat has been cleared by &e${executor.name}".mm())
}