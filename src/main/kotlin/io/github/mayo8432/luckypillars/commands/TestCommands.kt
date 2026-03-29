package io.github.mayo8432.luckypillars.commands

import de.c4vxl.gamemanager.gma.player.GMAPlayer.Companion.gma
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.longArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import io.github.mayo8432.luckypillars.userinterface.UIManager

class TestCommands {
    val command = commandTree("test") {
        withUsage("/test")
        withAliases("sg")

        literalArgument("sendStartingGameCountdown") {

            playerExecutor { player, args ->

                val game = player.gma.game ?: run {
                    player.sendMessage("Du bist in keinem Game.")
                    return@playerExecutor
                }

                UIManager.sendStartingGameCountdown(game, 5)
            }
        }
        literalArgument("startProgressbar") {
            playerExecutor { player, args ->

                val game = player.gma.game ?: run {
                    player.sendMessage("Du bist in keinem Game.")
                    return@playerExecutor
                }

                UIManager.startItemProgressBar(game, 5, 60) {}
            }
        }
    }
}