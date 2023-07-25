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
    implementation("com.github.Minestom:Minestom:824ae0a25b")
    implementation("com.github.DuranDevelopment:instanceguard:bf2ee4495f") {
        exclude(group = "com.github.Minestom", module = "Minestom")
    }

    /* Used for local testing
        implementation("cc.ddev:instanceguard:1.0-SNAPSHOT") {
            exclude(group = "com.github.Minestom", module = "Minestom")
        }
    */
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.github.simplix-softworks:simplixstorage:3.2.6")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("com.github.Mindgamesnl:storm:prod125")
    implementation("mysql:mysql-connector-java:8.0.33") /* Adding drivers for MySQL and SQLite  */
    implementation("org.xerial:sqlite-jdbc:3.42.0.0")
    compileOnly("junit:junit:4.13.2")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("me.hsgamer:hscore-minestom-gui:4.3.10")
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