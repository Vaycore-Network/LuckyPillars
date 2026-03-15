package io.github.mayo8432.luckypillars.game

import de.c4vxl.gamemanager.gma.event.game.GameStartedEvent
import de.c4vxl.gamemanager.gma.event.player.GamePlayerJoinedEvent
import de.c4vxl.gamemanager.gma.event.player.GamePlayerRespawnEvent
import de.c4vxl.gamemanager.gma.game.Game
import io.github.mayo8432.luckypillars.Main
import io.github.mayo8432.luckypillars.handler.MovementHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

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

        MovementHandler.Companion.startingGames.add(event.game)

        event.game.playerManager.alivePlayers.forEach {
            it.bukkitPlayer.sendTitlePart(TitlePart.TITLE, Component.text("3.."))
        }
        Bukkit.getScheduler().runTaskLater(Main.Companion.instance, Runnable {
            event.game.playerManager.alivePlayers.forEach {
                it.bukkitPlayer.sendTitlePart(TitlePart.TITLE, Component.text("2.."))
            }
            Bukkit.getScheduler().runTaskLater(Main.Companion.instance, Runnable {
                event.game.playerManager.alivePlayers.forEach {
                    it.bukkitPlayer.sendTitlePart(TitlePart.TITLE, Component.text("1.."))
                }
            }, 1L * 20L)
        },1L * 20L)


        Bukkit.getScheduler().runTaskLater(Main.Companion.instance, Runnable {
            MovementHandler.Companion.startingGames.remove(event.game)
            event.game.playerManager.alivePlayers.forEach {
                it.bukkitPlayer.clearTitle()
            }

            val blockedMaterials = Material.entries.filter { it in setOf(
                Material.BARRIER,
                Material.COMMAND_BLOCK,
                Material.CHAIN_COMMAND_BLOCK,
                Material.REPEATING_COMMAND_BLOCK,
                Material.COMMAND_BLOCK_MINECART,
                Material.STRUCTURE_BLOCK,
                Material.STRUCTURE_VOID,
                Material.JIGSAW,
                Material.LIGHT,
                Material.DEBUG_STICK,
                Material.BEDROCK,
                Material.END_PORTAL_FRAME,
                Material.END_PORTAL,
                Material.NETHER_PORTAL,
                Material.ENCHANTED_BOOK,
                Material.WRITTEN_BOOK,
                Material.AIR,
                Material.TEST_BLOCK

                )
                    || it.name.contains("ARMOR_TRIM")
                    || it.name.contains("BANNER_PATTERN")
                    || it.name.contains("MUSIC_DISC")
                    || it.name.contains("NAUTILUS_ARMOR")
                    || it.name.contains("POTTERY_SHERD")
                    || it.name.contains("HARNESS")
                    || it.name.contains("DYE")}


            var allowedMaterials = Material.entries.filter { it.isItem && it !in blockedMaterials }

            Bukkit.getScheduler().runTaskTimer(
                Main.Companion.instance, Runnable {
                event.game.playerManager.alivePlayers.forEach {
                    val randomItem = ItemStack(allowedMaterials.random())
                    it.bukkitPlayer.give(randomItem)
                }
            }, 0L, 8L *20L
            )
        }, 20L * 3L)
    }

    @EventHandler
    fun onPlayerJoin(event: GamePlayerJoinedEvent) {

    }
}