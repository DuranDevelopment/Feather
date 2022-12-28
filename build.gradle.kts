import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version ("7.1.0")
    id("io.freefair.lombok") version "6.6.1"
}

group = "cc.ddev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.Minestom:Minestom:-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.github.simplix-softworks:simplixstorage:3.2.5")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly("junit:junit:4.13.2")
    implementation("org.slf4j:slf4j-api:2.0.6")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "cc.ddev.feather.Main", "Multi-Release" to "true"))
        }

        fun reloc(pkg: String) = relocate(pkg, "cc.ddev.shaded.$pkg")
        reloc("de.leonhard.storage")
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}