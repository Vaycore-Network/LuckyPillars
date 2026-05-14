package io.github.mayo8432.luckypillars

import de.c4vxl.gamemanager.language.Language
import de.c4vxl.gamemanager.utils.ResourceUtils
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIPaperConfig
import io.github.mayo8432.luckypillars.commands.TestCommands
import io.github.mayo8432.luckypillars.game.GameHandler
import io.github.mayo8432.luckypillars.handler.MovementHandler
import io.github.mayo8432.luckypillars.handler.QueueHandler
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
    }

    override fun onLoad() {
        instance = this

        CommandAPI.onLoad(
            CommandAPIPaperConfig(this)
                .silentLogs(true)
                .verboseOutput(false)
        )
    }

    override fun onEnable() {
        CommandAPI.onEnable()

        logger.info("[+] ${this.name} has been enabled!")

        // Save config
        saveResource("allowedmaterials.yml", false)

        // Register language extensions
        ResourceUtils.readResource("langs", Main::class.java).split("\n")
            .forEach { langName ->
                Language.provideLanguageExtension(
                    "lucky-pillars",
                    langName,
                    ResourceUtils.readResource("lang/$langName.yml", Main::class.java)
                )
            }

        // Introducing Listeners
        GameHandler()
        MovementHandler()
        QueueHandler()

        // Introducing Commands
        // TestCommands()
    }

    override fun onDisable() {
        CommandAPI.onDisable()

        logger.info("[-] ${this.name} has been disabled!")
    }
}