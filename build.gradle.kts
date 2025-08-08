plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "com.github.theprogmatheus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"))
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:7.0.0")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    relocate("com.zaxxer.hikari", "com.github.theprogmatheus.devroom.treasurehunt.lib.hikari")
}