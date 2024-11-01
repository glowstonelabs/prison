import org.bukkit.plugin.java.JavaPlugin
import wtf.amari.prison.utils.fancyLog
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {
    private var connection: Connection? = null

    fun initialize(plugin: JavaPlugin) {
        val dbFile = File(plugin.dataFolder, "playerdata.db")
        val dbUrl = "jdbc:sqlite:${dbFile.absolutePath}"

        try {
            connection = DriverManager.getConnection(dbUrl)
            fancyLog("Connected to SQLite database.", "SUCCESS")
            createTable()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
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

    fun close() {
        try {
            connection?.close()
            fancyLog("Database connection closed.", "ERROR")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}