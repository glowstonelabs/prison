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
     * @param plugin The JavaPlugin instance.
     */
    fun initialize(plugin: JavaPlugin) {
        val dbFile = File(plugin.dataFolder, "playerdata.db")
        val dbUrl = "jdbc:sqlite:${dbFile.absolutePath}"

        try {
            connection = DriverManager.getConnection(dbUrl)
            fancyLog("Connected to SQLite database.", "SUCCESS")
            createTable()
        } catch (e: SQLException) {
            fancyLog("Failed to connect to SQLite database: ${e.message}", "ERROR")
            e.printStackTrace()
        }
    }

    /**
     * Creates the player_data table if it does not already exist.
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
                fancyLog("Table player_data created successfully.", "SUCCESS")
            }
        } catch (e: SQLException) {
            fancyLog("Failed to create table player_data: ${e.message}", "ERROR")
            e.printStackTrace()
        }
    }

    /**
     * Returns the current database connection.
     * @return The current Connection object or null if not connected.
     */
    fun getConnection(): Connection? = connection

    /**
     * Closes the database connection.
     */
    fun close() {
        try {
            connection?.close()
            fancyLog("Database connection closed.", "SUCCESS")
        } catch (e: SQLException) {
            fancyLog("Failed to close database connection: ${e.message}", "ERROR")
            e.printStackTrace()
        }
    }
}