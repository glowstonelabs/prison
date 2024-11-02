package wtf.amari.prison.databases

import org.bukkit.plugin.java.JavaPlugin
import wtf.amari.prison.utils.fancyLog
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

object DatabaseManager {
    private var connection: Connection? = null

    fun initialize(plugin: JavaPlugin) {
        val dbFile = File(plugin.dataFolder, "playerdata.db")
        val dbUrl = "jdbc:sqlite:${dbFile.absolutePath}"

        try {
            connection = DriverManager.getConnection(dbUrl)
            fancyLog("Connected to SQLite database.", "SUCCESS")
            createTable() // Create the table during initialization
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun createTable() {
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS player_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_name TEXT NOT NULL,
                balance INTEGER NOT NULL
            );
        """.trimIndent()

        try {
            connection?.createStatement()?.use { statement: Statement ->
                statement.execute(createTableSQL)
                fancyLog("Table created or already exists.", "INFO")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getConnection(): Connection? {
        return connection
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