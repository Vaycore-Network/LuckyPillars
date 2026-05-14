package io.github.mayo8432.luckypillars.handler

import de.c4vxl.gamemanager.gma.GMA
import de.c4vxl.gamemanager.gma.event.player.GamePlayerQuitEvent
import de.c4vxl.gamemanager.gma.event.player.GamePlayerSpectateEndEvent
import de.c4vxl.gamemanager.gma.player.GMAPlayer
import de.c4vxl.gamemanager.gma.player.GMAPlayer.Companion.gma
import io.github.mayo8432.luckypillars.Main
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Handles game queuing
 */
class QueueHandler : Listener {
    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)

        var i = 0
        Bukkit.getScheduler().runTaskTimer(Main.instance, Runnable {
            GMA.registeredGames.forEach { game ->
                if (!game.isQueuing)
                    return@forEach

                game.players.forEach {
                    val language = it.language.child("lucky-pillars")

                        it.bukkitPlayer.sendActionBar(language.getCmp("queue.msg.queuing",
                            ".".repeat(i + 1), (game.players.size - 1).toString(), (game.size.maxPlayers - 1).toString()
                        ))
                }
            }

            i = if (i < 2) i+1 else 0
        }, 0, 20)
    }

    private fun send(player: GMAPlayer) {
        Bukkit.getScheduler().callSyncMethod(Main.instance) {
            player.join(GMA.getOrCreate(12, 1))
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {
            send(event.player.gma)
        }, 20)
    }

    @EventHandler
    fun onGameLeave(event: GamePlayerQuitEvent) {
        send(event.player)
    }

    @EventHandler
    fun onSpectatorLeave(event: GamePlayerSpectateEndEvent) {
        send(event.player)
    }
}