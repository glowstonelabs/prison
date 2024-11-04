package wtf.amari.prison.utils

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import wtf.amari.prison.databases.DatabaseManager
import wtf.amari.prison.databases.PlayerCurrencyDAO

class PlaceHolders : PlaceholderExpansion() {

    override fun getIdentifier() = "prison"
    override fun getAuthor() = "Amari"
    override fun getVersion() = "1.0"

    override fun onPlaceholderRequest(player: Player?, identifier: String): String {
        if (player == null) return ""

        // Use a single DAO instance to avoid multiple database connections
        val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
        val playerCurrency = dao.getPlayerCurrency(player.uniqueId.toString()) ?: return "0"

        return when (identifier) {
            "balance" -> (playerCurrency.getOrDefault("money", 0) as Number).shorthand()
            "tokens" -> (playerCurrency.getOrDefault("tokens", 0) as Number).shorthand()
            "gems" -> (playerCurrency.getOrDefault("gems", 0) as Number).shorthand()
            else -> ""
        }
    }
}