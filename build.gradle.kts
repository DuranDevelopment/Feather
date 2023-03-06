import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version ("7.1.0")
    id("io.freefair.lombok") version "6.6.2"
}

group = "cc.ddev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
//    implementation("com.github.Minestom:Minestom:-SNAPSHOT")
    implementation("com.github.Minestom.Minestom:Minestom:1.19.3-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.github.simplix-softworks:simplixstorage:3.2.5")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    implementation("com.github.Mindgamesnl:storm:prod125")
    implementation("mysql:mysql-connector-java:8.0.32") /* Adding drivers for MySQL and SQLite  */
    implementation("org.xerial:sqlite-jdbc:3.40.1.0")
    compileOnly("junit:junit:4.13.2")
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("me.hsgamer:hscore-minestom-gui:4.2.10")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("feather")
        archiveFileName.set("feather-${project.version}.jar")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "cc.ddev.feather.Server", "Multi-Release" to "true"))
        }

        fun reloc(pkg: String) = relocate(pkg, "cc.ddev.shaded.$pkg")
        reloc("de.leonhard.storage")
        reloc("com.zaxxer.hikari")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}