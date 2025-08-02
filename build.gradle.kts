plugins {
    java
    idea
}

repositories {
    mavenCentral()

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven(url = "https://mvn.lumine.io/repository/maven-public/")

    mavenCentral()
    maven { url = uri("https://jitpack.io") }

}


dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")
    compileOnly("io.lumine:Mythic-Dist:5.6.1")
    compileOnly("net.luckperms:api:5.4")
    implementation("com.github.PlaceholderAPI:PlaceholderAPI:2.11.6")

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(22))
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

val serverPluginsDir = file("C:/Users/yoyob/Desktop/Solo Leveling Craft/plugins/") // Adjust to match your setup

tasks.register<Copy>("deployJar") {
    dependsOn(tasks.build)
    from(tasks.jar)
    into(serverPluginsDir)
    doLast {
        println("✅ Plugin jar copied to server!")
    }
}

tasks.build {
    finalizedBy("deployJar") // Automatically run after build
}

