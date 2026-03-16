package io.github.mayo8432.luckypillars.userinterface

import de.c4vxl.gamemanager.gma.game.Game
import io.github.mayo8432.luckypillars.Main
import io.github.mayo8432.luckypillars.game.GameHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit

class UIManager {

    // @param: all running Tasks for the game instance
    private val runningTasks = mutableMapOf<Game, Int>()

    fun sendStartingGameCountdown(game: Game, timeInSeconds: Int) {

        // Sends a countdown to the player
        for (i in timeInSeconds downTo 1) {

            // Creates value:timeInSeconds Bukkit runTaskLater's with increasing time. Like this: Runnable1: 3sec, Runnable 2: 2sec, ... and so on
            Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {

                // Sends for example: 5.., 4.., 3.., and so on
                game.playerManager.alivePlayers.forEach {

                    // Sending a Title Part to the player: 5.., 4.., 3.. and so on
                    it.bukkitPlayer.sendTitlePart(TitlePart.TITLE, Component.text("$i.."))
                }
            }, (timeInSeconds - i) * 20L)   // *20L due to 20 Minecraft Ticks being 1 second
        }

        // This runTaskLater removes the UI Title Countdown and removes the starting state from the game
        Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {

            // Removes the game from the starting phase
            GameHandler.startingGames.remove(game)

            game.playerManager.alivePlayers.forEach {

                // Clears the Title Part off the player
                it.bukkitPlayer.clearTitle()
            }
        }, timeInSeconds * 20L)     // *20L due to 20 Minecraft Ticks being 1 second
    }

    fun startItemProgressBar(game: Game, ticksPerStep: Long, ticksForProgressBarCompletion: Long) {

        // If a task is already running -> don't start another one
        if (runningTasks.containsKey(game)) return

        // @param: Time passed is safed in this parameter
        var elapsedTicks = 0L

        val taskId = Bukkit.getScheduler().runTaskTimer(Main.instance, Runnable {

            // TODO:
            if (!game.isRunning) {

                // TODO:
                stopItemProgressBar(game)
                return@Runnable
            }

            val completion = (ticksForProgressBarCompletion/ticksPerStep).toInt()

            // Generating progress-bar
            val bar = buildUnicodeProgressBar(completion, elapsedTicks.toInt())

            // Send to all players
            game.playerManager.alivePlayers.forEach {
                it.bukkitPlayer.sendActionBar(bar)
            }

            // Increase Ticks
            elapsedTicks += 1

            // If done - stop tasks
            if (elapsedTicks >= completion) {
                elapsedTicks = 0L
            }

        },0L, ticksPerStep).taskId
    }

    fun stopItemProgressBar(game: Game) {

        // TODO:
        runningTasks.remove(game)?.let { Bukkit.getScheduler().cancelTask(it) }
    }

    fun buildUnicodeProgressBar(completion: Int, progress: Int): Component {
        val total = completion.coerceAtLeast(1)
        val done = progress.coerceIn(0, total)

        val green = "█".repeat(done)
        val gray = "░".repeat(total - done)

        return Component.text(green, NamedTextColor.GREEN)
            .append(Component.text(gray, NamedTextColor.GRAY))
    }
}