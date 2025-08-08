plugins {
    id("java")
}

group = "com.github.theprogmatheus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"))
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}