# SpigotPluginAPI [![Build Status](https://api.travis-ci.org/CloudCraftNetwork/SpigotPluginAPI.svg)](https://travis-ci.org/CloudCraftNetwork/SpigotPluginAPI)

## Sample build.gradle
```gradle
apply plugin: 'java'

version = '1.0'
sourceCompatibility = 1.8

repositories {
    mavenCentral()
    maven {
        url "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
    }
    maven {
        url "https://oss.sonatype.org/content/groups/public/"
    }
    maven {
        url = 'http://repo.cloudcraftnetwork.com/maven/repository/'
    }
}

dependencies {
    compile 'com.cloudcraftnetwork.api.plugin.spigot:SpigotPluginAPI:0.1-SNAPSHOT'
}

task pluginJar(type: Jar) {
    baseName = project.name
    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
    with jar
}
```

