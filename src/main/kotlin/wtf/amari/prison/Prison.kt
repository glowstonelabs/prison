package wtf.amari.prison

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.honkling.commando.spigot.SpigotCommandManager
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary
import org.bukkit.plugin.java.JavaPlugin
import wtf.amari.prison.databases.DatabaseManager
import wtf.amari.prison.events.PlayerListener
import wtf.amari.prison.pickaxe.events.PickaxeListener
import wtf.amari.prison.utils.fancyLog

class Prison : JavaPlugin() {
    companion object {
        lateinit var instance: Prison
            private set

        private val scope = CoroutineScope(Dispatchers.Default)
    }

    var scoreboardLibrary: ScoreboardLibrary? = null

    private val commandPackages = listOf(
        "wtf.amari.prison.economy.commands",
        "wtf.amari.prison.pickaxe.commands"
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
        fancyLog("Prison plugin has been disabled.", "INFO")
        DatabaseManager.close()
        scoreboardLibrary?.close()
    }

    private fun initializePlugin() {
        setupConfig()
        registerCommands()
        registerEvents()
        DatabaseManager.initialize(this)
        initializeScoreboardLibrary()
    }

    private fun registerCommands() {
        val commandManager = SpigotCommandManager(this)
        try {
            commandPackages.forEach { commandManager.registerCommands(it) }
            fancyLog("Commands registered successfully.", "INFO")
        } catch (e: Exception) {
            fancyLog("Failed to register commands: ${e.message}", "ERROR")
        }
    }

    private fun registerEvents() {
        listeners.forEach { listenerSupplier ->
            try {
                val listener = listenerSupplier()
                server.pluginManager.registerEvents(listener, this)
                fancyLog("${listener::class.simpleName} registered successfully.", "INFO")
            } catch (e: Exception) {
                fancyLog("Failed to register listener: ${e.message}", "ERROR")
            }
        }
    }

    private fun setupConfig() {
        dataFolder.mkdirs()
        saveDefaultConfig()
        reloadConfig()
        fancyLog("Config loaded successfully.", "INFO")
    }

    private fun initializeScoreboardLibrary() {
        try {
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(this)
        } catch (e: NoPacketAdapterAvailableException) {
            scoreboardLibrary = NoopScoreboardLibrary()
            fancyLog("No scoreboard packet adapter available!", "ERROR")
        }
    }
}