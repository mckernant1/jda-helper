/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.12.1/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    kotlin("jvm") version "1.9.0"
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    `maven-publish`
    `java-library`
    signing
}

val projectName = "annotations"

group = "com.mckernant1.jda"
version = "0.0.2"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.2") // Add JDA dependency
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13") // KSP API

    // KotlinPoet (for generating code)
    implementation("com.squareup:kotlinpoet:2.0.0")
    implementation("com.squareup:kotlinpoet-ksp:2.0.0")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}


// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            url = uri("s3://mvn.mckernant1.com/release")
            authentication {
                register("awsIm", AwsImAuthentication::class.java)
            }
        }
    }

    publications {
        create<MavenPublication>("default") {
            artifactId = projectName
            from(components["kotlin"])
            val sourcesJar by tasks.registering(Jar::class) {
                val sourceSets: SourceSetContainer by project
                from(sourceSets["main"].allSource)
                archiveClassifier.set("sources")
            }
            artifact(sourcesJar)
            pom {
                name.set(projectName)
                description.set("")
                url.set("https://github.com/mckernant1/$projectName")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("mckernant1")
                        name.set("Tom McKernan")
                        email.set("tmeaglei@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/mckernant1/$projectName.git")
                    developerConnection.set("scm:git:ssh://github.com/mckernant1/$projectName.git")
                    url.set("https://github.com/mckernant1/$projectName")
                }
            }
        }
    }
}
