plugins {
    `java-library`
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.deepdive"
version = "1.1.0"


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar() // Ensure the source JAR is included
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven {
        url = uri("https://maven.citizensnpcs.co/repo")
    }
}

dependencies {
    implementation("io.papermc.paperweight:paperweight-userdev:1.7.2-SNAPSHOT")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    compileOnly(files("libs/Votifier-2.7.3.jar"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-mojangapi:1.20.4-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("com.github.jitpack:gradle-simple:1.0")
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}




publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "com.github.BrianMayMC"
            artifactId = "deep-dive-api"
            version = "1.1.0-dev"

            // Avoid publishing extra JARs like -dev or -all
            artifact(tasks.named("shadowJar"))
        }
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("deep-dive-api")  // Set the base name to match your project
    archiveVersion.set("1.1.0")  // Ensure this pulls directly from your version variable
    archiveClassifier.set("")  // This ensures no classifier (like '-all') is added
}

