package io.github.mayo8432.luckypillars.game

import de.c4vxl.gamemanager.gma.event.game.GameStartedEvent
import de.c4vxl.gamemanager.gma.event.player.GamePlayerJoinedEvent
import de.c4vxl.gamemanager.gma.event.player.GamePlayerRespawnEvent
import de.c4vxl.gamemanager.gma.game.Game
import io.github.mayo8432.luckypillars.Main
import io.github.mayo8432.luckypillars.userinterface.UIManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameHandler : Listener {

    // Registering the event in the plugin manager (standard procedure)
    init {
        Bukkit.getPluginManager().registerEvents(this, Main.Companion.instance)
    }

    // @param: A list of all games in the starting stage
    companion object {
        val startingGames = mutableListOf<Game>()
    }

    // This eliminates the player on respawn to dodge creating a weird ghost state to the player where he is not alive nor dead (If you'd do it on the Death Event)
    @EventHandler
    fun onRespawn(event: GamePlayerRespawnEvent) {

        // Using the event.killer to implement the GMA stats system
        event.player.eliminate(event.killer)
    }

    @EventHandler
    fun onGameStart(event: GameStartedEvent) {

        // Adds the current game to the starting game phase
        startingGames.add(event.game)

        //Gives each player a countdown
        UIManager.sendStartingGameCountdown(event.game, 5) {
            ItemSpawnHandler.startGameCycle(event.game, 6) // or 7
        }
    }

    @EventHandler
    fun onPlayerJoin(event: GamePlayerJoinedEvent) {

    }
}