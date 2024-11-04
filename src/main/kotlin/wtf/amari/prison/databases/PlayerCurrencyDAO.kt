package wtf.amari.prison.databases

import java.sql.Connection
import java.sql.SQLException

class PlayerCurrencyDAO(private val connection: Connection?) {

    companion object {
        private var isTableCreated = false
    }

    init {
        if (!isTableCreated) {
            createTable()
            isTableCreated = true
        }
    }

    /**
     * Creates the player_currency table if it does not exist.
     */
    private fun createTable() {
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS player_currency (
                uuid TEXT PRIMARY KEY,
                tokens INTEGER DEFAULT 0,
                money INTEGER DEFAULT 0,
                gems INTEGER DEFAULT 0
            );
        """.trimIndent()

        try {
            connection?.createStatement()?.use { statement ->
                statement.execute(createTableSQL)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    /**
     * Updates the player's currency values in the database.
     *
     * @param uuid The player's unique identifier.
     * @param tokens The new token amount, or null to leave unchanged.
     * @param money The new money amount, or null to leave unchanged.
     * @param gems The new gem amount, or null to leave unchanged.
     */
    fun updatePlayerCurrency(uuid: String, tokens: Int? = null, money: Int? = null, gems: Int? = null) {
        val sql = """
            UPDATE player_currency
            SET tokens = COALESCE(?, tokens),
                money = COALESCE(?, money),
                gems = COALESCE(?, gems)
            WHERE uuid = ?;
        """.trimIndent()

        try {
            connection?.prepareStatement(sql)?.use { statement ->
                statement.setObject(1, tokens)
                statement.setObject(2, money)
                statement.setObject(3, gems)
                statement.setString(4, uuid)
                statement.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    /**
     * Retrieves the player's currency values from the database.
     *
     * @param uuid The player's unique identifier.
     * @return A map containing the currency values, or null if the player is not found.
     */
    fun getPlayerCurrency(uuid: String): Map<String, Any>? {
        val sql = "SELECT tokens, money, gems FROM player_currency WHERE uuid = ?"
        return try {
            connection?.prepareStatement(sql)?.use { statement ->
                statement.setString(1, uuid)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) {
                        mapOf(
                            "tokens" to resultSet.getInt("tokens"),
                            "money" to resultSet.getInt("money"),
                            "gems" to resultSet.getInt("gems")
                        )
                    } else {
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Sets the initial balance for a new player.
     *
     * @param uuid The player's unique identifier.
     * @param initialTokens The initial token amount.
     * @param initialMoney The initial money amount.
     */
    fun setInitialBalance(uuid: String, initialTokens: Int, initialMoney: Int) {
        val sql = """
            INSERT INTO player_currency (uuid, tokens, money, gems) VALUES (?, ?, ?, 0)
            ON CONFLICT(uuid) DO NOTHING;
        """.trimIndent()

        try {
            connection?.prepareStatement(sql)?.use { statement ->
                statement.setString(1, uuid)
                statement.setInt(2, initialTokens)
                statement.setInt(3, initialMoney)
                statement.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}