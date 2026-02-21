package io.github.mayo8432.luckypillars

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIPaperConfig
import io.github.mayo8432.luckypillars.handler.GameHandler
import io.github.mayo8432.luckypillars.handler.MovementHandler
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
        GameHandler()
        MovementHandler()
    }

    override fun onDisable() {
        CommandAPI.onDisable()

        logger.info("[-] ${this.name} has been disabled!")
    }
}