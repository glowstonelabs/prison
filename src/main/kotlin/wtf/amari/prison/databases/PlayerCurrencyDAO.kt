package wtf.amari.prison.databases

import java.sql.Connection
import java.sql.SQLException

class PlayerCurrencyDAO(private val connection: Connection?) {

    init {
        createTable()
    }

    private fun createTable() {
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS player_currency (
                uuid TEXT PRIMARY KEY,
                tokens INTEGER DEFAULT 0,
                money REAL DEFAULT 0.0,
                gems INTEGER DEFAULT 0
            );
        """.trimIndent()

        try {
            connection?.createStatement()?.use { statement ->
                statement.execute(createTableSQL)
                println("Table created or already exists.")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun updatePlayerCurrency(uuid: String, tokens: Long, money: Double, gems: Int) {
        val sql = """
            INSERT INTO player_currency (uuid, tokens, money, gems) VALUES (?, ?, ?, ?)
            ON CONFLICT(uuid) DO UPDATE SET
                tokens = excluded.tokens,
                money = excluded.money,
                gems = excluded.gems;
        """.trimIndent()

        try {
            connection?.prepareStatement(sql)?.use { statement ->
                statement.setString(1, uuid)
                statement.setLong(2, tokens)
                statement.setDouble(3, money)
                statement.setInt(4, gems)
                statement.executeUpdate()
                println("Updated player currency for UUID: $uuid")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getPlayerCurrency(uuid: String): Map<String, Any>? {
        val sql = "SELECT tokens, money, gems FROM player_currency WHERE uuid = ?"
        return try {
            connection?.prepareStatement(sql)?.use { statement ->
                statement.setString(1, uuid)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) {
                        mapOf(
                            "tokens" to resultSet.getInt("tokens"),
                            "money" to resultSet.getDouble("money"),
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

    fun getBalance(uuid: String): Double? {
        val sql = "SELECT money FROM player_currency WHERE uuid = ?"
        return try {
            connection?.prepareStatement(sql)?.use { statement ->
                statement.setString(1, uuid)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) {
                        resultSet.getDouble("money")
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

    fun setInitialBalance(uuid: String, initialBalance: Double) {
        val sql = """
            INSERT INTO player_currency (uuid, tokens, money, gems) VALUES (?, 0, ?, 0)
            ON CONFLICT(uuid) DO NOTHING;
        """.trimIndent()

        try {
            connection?.prepareStatement(sql)?.use { statement ->
                statement.setString(1, uuid)
                statement.setDouble(2, initialBalance)
                statement.executeUpdate()
                println("Set initial balance for UUID: $uuid")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
