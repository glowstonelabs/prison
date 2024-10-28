package wtf.amari.prison

import dev.dejvokep.boostedyaml.YamlDocument
import kotlinx.coroutines.runBlocking
import me.honkling.commando.spigot.SpigotCommandManager
import org.bukkit.plugin.java.JavaPlugin
import wtf.amari.prison.events.PlayerListener
import java.io.File


class Prison : JavaPlugin() {

    companion object {
        lateinit var instance: Prison
            private set

        lateinit var config: YamlDocument
            private set
    }


    override fun onEnable() {
        instance = this
        runBlocking {
            registerCommands()
            registerEvents()
            setupConfig()
        }
        logger.info("prison has been enabled successfully!")
    }

    override fun onDisable() {
        logger.info("prison has been disabled.")
    }

    private suspend fun registerCommands() {
        try {
            SpigotCommandManager(this).apply {
                registerCommands("wtf.amari.prison.commands")
                registerCommands("wtf.amari.prison.pickaxe")
            }
            logger.info("Commands registered successfully.")
        } catch (e: Exception) {
            logger.severe("Failed to register commands: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun registerEvents() {
        val events = listOf(
            PlayerListener(),
        )
        events.forEach { server.pluginManager.registerEvents(it, this) }
        logger.info("Events registered successfully.")
    }

    private fun setupConfig() {
        val config = YamlDocument.create(File(dataFolder, "config.yml"), getResource("config.yml")!!)
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
            saveDefaultConfig()
        }
        logger.info("Config loaded successfully.")
    }
}