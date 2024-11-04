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
        return when (identifier) {
            "balance" -> {
                val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
                val playerCurrency = dao.getPlayerCurrency(player.uniqueId.toString())
                val balance = playerCurrency?.get("money") as? Int ?: 0
                balance.shorthand()
            }

            "tokens" -> {
                val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
                val playerCurrency = dao.getPlayerCurrency(player.uniqueId.toString())
                val balance = playerCurrency?.get("tokens") as? Int ?: 0
                balance.shorthand()
            }

            "gems" -> {
                val dao = PlayerCurrencyDAO(DatabaseManager.getConnection())
                val playerCurrency = dao.getPlayerCurrency(player.uniqueId.toString())
                val balance = playerCurrency?.get("gems") as? Int ?: 0
                balance.shorthand()
            }

            else -> ""
        }
    }
}