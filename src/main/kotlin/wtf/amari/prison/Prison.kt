package wtf.amari.prison

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.honkling.commando.spigot.SpigotCommandManager
import org.bukkit.plugin.java.JavaPlugin
import wtf.amari.prison.databases.DatabaseManager
import wtf.amari.prison.events.AutoAnnouncements
import wtf.amari.prison.events.PlayerListener
import wtf.amari.prison.pickaxe.events.PickaxeListener
import wtf.amari.prison.utils.PlaceHolders
import wtf.amari.prison.utils.fancyLog
import java.sql.Connection

class Prison : JavaPlugin() {
    companion object {
        lateinit var instance: Prison
            private set

        private val scope = CoroutineScope(Dispatchers.Default)
    }

    var databaseConnection: Connection? = null

    private val commandPackages = listOf(
        "wtf.amari.prison.economy.commands",
        "wtf.amari.prison.pickaxe.commands",
        "wtf.amari.prison.commands"
    )

    private val listeners = listOf(
        { PlayerListener() },
        { PickaxeListener() }
    )

    override fun onEnable() {
        instance = this
        initializePlugin()
        fancyLog("Prison plugin has been enabled successfully!", "SUCCESS")
    }

    override fun onDisable() {
        fancyLog("Prison plugin has been disabled.", "ERROR")
        DatabaseManager.close()
    }

    /**
     * Initializes the plugin by setting up configuration, commands, events, placeholders, and database connection.
     */
    private fun initializePlugin() {
        setupConfig()
        registerCommands()
        registerEvents()
        registerPlaceholders()
        AutoAnnouncements()
        DatabaseManager.initialize(this)
        databaseConnection = DatabaseManager.getConnection()
    }

    /**
     * Registers commands using the SpigotCommandManager.
     */
    private fun registerCommands() {
        val commandManager = SpigotCommandManager(this)
        try {
            commandPackages.forEach { commandManager.registerCommands(it) }
            fancyLog("Commands registered successfully.", "SUCCESS")
        } catch (e: Exception) {
            fancyLog("Failed to register commands: ${e.message}", "ERROR")
        }
    }

    /**
     * Registers event listeners.
     */
    private fun registerEvents() {
        listeners.forEach { listenerSupplier ->
            try {
                val listener = listenerSupplier()
                server.pluginManager.registerEvents(listener, this)
                fancyLog("${listener::class.simpleName} registered successfully.", "SUCCESS")
            } catch (e: Exception) {
                fancyLog("Failed to register listener: ${e.message}", "ERROR")
            }
        }
    }

    /**
     * Sets up the plugin configuration.
     */
    private fun setupConfig() {
        dataFolder.mkdirs()
        saveDefaultConfig()
        reloadConfig()
        fancyLog("Config loaded successfully.", "SUCCESS")
    }

    /**
     * Registers PlaceholderAPI placeholders if the plugin is available.
     */
    private fun registerPlaceholders() {
        server.pluginManager.getPlugin("PlaceholderAPI")?.let {
            PlaceHolders().register()
            fancyLog("PlaceholderAPI placeholders registered successfully.", "SUCCESS")
        }
    }
}