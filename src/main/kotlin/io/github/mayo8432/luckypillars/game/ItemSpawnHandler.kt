package io.github.mayo8432.luckypillars.game

import de.c4vxl.gamemanager.gma.game.Game
import de.c4vxl.gamemanager.utils.ItemBuilder
import io.github.mayo8432.luckypillars.Main
import io.github.mayo8432.luckypillars.userinterface.UIManager
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration

object ItemSpawnHandler {
    // Config for allowed materials
    private val materialConfig: YamlConfiguration
        get() = YamlConfiguration.loadConfiguration(Main.instance.dataFolder.resolve("allowedmaterials.yml"))

    private val creativeOnly = setOf(
        Material.COMMAND_BLOCK,
        Material.CHAIN_COMMAND_BLOCK,
        Material.REPEATING_COMMAND_BLOCK,
        Material.STRUCTURE_BLOCK,
        Material.JIGSAW,
        Material.BARRIER,
        Material.LIGHT,
        Material.DEBUG_STICK,
        Material.STRUCTURE_VOID,
        Material.TEST_BLOCK,
        Material.TEST_INSTANCE_BLOCK
    )

    /**
     * Returns a map of Materials mapped to a weight according to user config
     */
    private val weightedMaterials: Map<List<Material>, Int>
        get() =
            materialConfig.getKeys(false).associate {
                Pair(
                    Material.entries.filter { material ->
                        material.name.contains("_$it")
                                && material.isItem
                                && !material.isLegacy
                                && !material.isAir
                                && !creativeOnly.contains(material)
                                            },

                    materialConfig.getInt(it)
                )
            }

    /**
     * Returns a random material
     */
    private fun getRandomMaterial(): Material {
        val weighted = weightedMaterials.flatMap { (materials, weight) -> materials.map { it to weight } }

        val total = weighted.sumOf { it.second }
        val random = (0 until total).random()

        var current = 0
        for ((material, weight) in weighted) {
            current += weight
            if (random < current)
                return material
        }

        error("Failed to select a random material. Check your allowedmaterials.yml configuration file!")
    }

    fun startGameCycle(game : Game, timeInSeconds : Long) {
        UIManager.startItemProgressBar(game , 15, timeInSeconds * 20) {
            game.playerManager.alivePlayers.forEach { player ->
                player.bukkitPlayer.inventory.addItem(
                    ItemBuilder(getRandomMaterial())
                        .build()
                )
            }
        }
    }
}