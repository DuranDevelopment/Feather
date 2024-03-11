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
    mavenLocal()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("dev.hollowcube:minestom-ce:9d6752c86f")
    implementation("com.github.DuranDevelopment:instanceguard:1c50091bc9") {
        exclude(group = "dev.hollowcube", module = "minestom-ce")
    }

    implementation("com.github.Minestom.VanillaReimplementation:core:de143342b9") {
        exclude(group = "dev.hollowcube", module = "minestom-ce")
    }

    /* Local development
    implementation("cc.ddev:instanceguard:1.0-SNAPSHOT") {
        exclude(group = "com.github.Minestom", module = "Minestom")
    }
     */

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.github.simplix-softworks:simplixstorage:3.2.7")
    implementation("net.kyori:adventure-text-minimessage:4.15.0")
    implementation("com.github.Mindgamesnl:storm:e1f961b480")
    implementation("mysql:mysql-connector-java:8.0.33") /* Adding drivers for MySQL and SQLite  */
    implementation("org.xerial:sqlite-jdbc:3.45.0.0")
    compileOnly("junit:junit:4.13.2")
    implementation("org.slf4j:slf4j-api:2.0.11")
    implementation("me.hsgamer:hscore-minestom-gui:4.3.30")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("com.google.guava:guava:33.0.0-jre")
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