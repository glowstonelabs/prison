package wtf.amari.prison.databases

import org.bukkit.plugin.java.JavaPlugin
import wtf.amari.prison.utils.fancyLog
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseManager {
    private var connection: Connection? = null

    /**
     * Initializes the database connection and creates the necessary tables.
     *
     * @param plugin The plugin instance.
     */
    fun initialize(plugin: JavaPlugin) {
        val dbFile = File(plugin.dataFolder, "playerdata.db")
        val dbUrl = "jdbc:sqlite:${dbFile.absolutePath}"

        try {
            connection = DriverManager.getConnection(dbUrl)
            fancyLog("Connected to SQLite database.", "SUCCESS")
            createTable()
        } catch (e: SQLException) {
            e.printStackTrace()
            fancyLog("Failed to connect to SQLite database: ${e.message}", "ERROR")
        }
    }

    /**
     * Creates the player_data table if it does not exist.
     */
    private fun createTable() {
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS player_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_name TEXT NOT NULL,
                balance INTEGER NOT NULL
            );
        """.trimIndent()

        try {
            connection?.createStatement()?.use { statement ->
                statement.execute(createTableSQL)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            fancyLog("Failed to create table: ${e.message}", "ERROR")
        }
    }

    /**
     * Retrieves the current database connection.
     *
     * @return The current database connection, or null if not connected.
     */
    fun getConnection(): Connection? = connection

    /**
     * Closes the database connection.
     */
    fun close() {
        try {
            connection?.close()
            fancyLog("Database connection closed.", "ERROR")
        } catch (e: SQLException) {
            e.printStackTrace()
            fancyLog("Failed to close database connection: ${e.message}", "ERROR")
        }
    }
}