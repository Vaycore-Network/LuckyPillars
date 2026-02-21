plugins {
    kotlin("jvm") version "2.1.10"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "io.github.mayo8432"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://mvn.c4vxl.de/gma/")
}

dependencies {
    // Kotlin standard-lib
    implementation(kotlin("stdlib"))
    implementation("de.c4vxl:gamemanagementapi:1.0.0")

    // Paper API
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")

    // CommandAPI
    implementation("dev.jorel:commandapi-paper-shade:11.1.0")
    implementation("dev.jorel:commandapi-kotlin-paper:11.1.0")
}

kotlin {
    jvmToolchain(21)
}

paperweight {
    reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

bukkit {
    name = "Vaycore-LuckyPillars"
    main = "io.github.mayo8432.luckypillars.Main"
    version = project.version.toString()
    apiVersion = "1.14"
    authors = listOf("mayo8432")
    description = "The LuckyPillars minigame for the Vaycore-Network"
    softDepend = listOf("GameManager")

    libraries = listOf(
        "org.jetbrains.kotlin:kotlin-stdlib:2.1.10",
        "dev.jorel:commandapi-paper-shade:11.1.0",
        "dev.jorel:commandapi-kotlin-paper:11.1.0"
    )
}