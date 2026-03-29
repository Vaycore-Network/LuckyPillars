package io.github.mayo8432.luckypillars.handler

import de.c4vxl.gamemanager.gma.game.Game
import de.c4vxl.gamemanager.gma.player.GMAPlayer.Companion.gma
import io.github.mayo8432.luckypillars.Main
import io.github.mayo8432.luckypillars.game.GameHandler.Companion.startingGames
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class MovementHandler : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.player.gma.game !in startingGames )
            return

        event.isCancelled = true
    }
}