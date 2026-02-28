package io.github.mayo8432.luckypillars.handler

import de.c4vxl.gamemanager.gma.event.game.GameStartedEvent
import de.c4vxl.gamemanager.gma.event.player.GamePlayerDeathEvent
import de.c4vxl.gamemanager.gma.event.player.GamePlayerJoinedEvent
import de.c4vxl.gamemanager.gma.event.player.GamePlayerRespawnEvent
import io.github.mayo8432.luckypillars.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

class GameHandler : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)
    }

    @EventHandler
    fun onRespawn(event: GamePlayerRespawnEvent) {
        event.player.eliminate()
    }

    @EventHandler
    fun onGameStart(event: GameStartedEvent) {

        MovementHandler.startingGames.add(event.game)

        Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {
            MovementHandler.startingGames.remove(event.game)
            event.game.playerManager.alivePlayers.forEach {
                it.bukkitPlayer.sendTitlePart(TitlePart.TITLE, Component.text("3.."))
            }
            Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {
                event.game.playerManager.alivePlayers.forEach {
                    it.bukkitPlayer.sendTitlePart(TitlePart.TITLE, Component.text("2.."))
                }
                Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {
                    event.game.playerManager.alivePlayers.forEach {
                        it.bukkitPlayer.sendTitlePart(TitlePart.TITLE, Component.text("1.."))
                    }
                }, 1L * 20L)
            },1L * 20L)

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

                )
                    || it.name.contains("ARMOR_TRIM")
                    || it.name.contains("BANNER_PATTERN")
                    || it.name.contains("MUSIC_DISC")
                    || it.name.contains("NAUTILUS_ARMOR")
                    || it.name.contains("POTTERY_SHERD")
                    || it.name.contains("HARNESS")
                    || it.name.contains("DYE")}


            var allowedMaterials = Material.entries.filter { it.isItem && it !in blockedMaterials }

            Bukkit.getScheduler().runTaskTimer(Main.instance, Runnable {
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