package wtf.amari.prison

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.honkling.commando.spigot.SpigotCommandManager
import org.bukkit.plugin.java.JavaPlugin
import wtf.amari.prison.events.PlayerListener
import wtf.amari.prison.pickaxe.events.PickaxeListener
import wtf.amari.prison.utils.fancyLog

class Prison : JavaPlugin() {

    companion object {
        lateinit var instance: Prison
            private set
        private val scope = CoroutineScope(Dispatchers.Default)
    }

    private val commandPackages = listOf(
        "wtf.amari.prison.commands",
        "wtf.amari.prison.pickaxe"
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
    }

    private fun initializePlugin() {
        setupConfig()
        registerCommands()
        registerEvents()
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
}