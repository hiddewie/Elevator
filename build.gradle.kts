buildscript {
    val kotlinVersion = "1.6.21"
    val springBootVersion = "2.6.7"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    }
}

plugins {
    val kotlinVersion = "1.6.21"
    val springBootVersion = "2.6.7"
    val kotlinterVersion = "3.10.0"

    // IntelliJ
    idea

    // Java
    java

    // Kotlin JVM
    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion

    // Kotlinter
    id("org.jmailen.kotlinter") version kotlinterVersion

    // Spring
    id("org.springframework.boot") version springBootVersion

    // Test coverage
    jacoco
}

apply(plugin = "io.spring.dependency-management")

val kotlinVersion = "1.3.50"
val axonVersion = "4.2"
val javassistVersion = "3.26.0-GA"

repositories {
    mavenCentral()
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Java 11 Javassist
    implementation("org.javassist:javassist:$javassistVersion")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // In-memory H2 database
    implementation("com.h2database:h2")

    // Axon
    implementation("org.axonframework:axon-spring-boot-starter:$axonVersion") {
        exclude(group = "org.axonframework", module = "axon-server-connector")
    }
    testImplementation("org.axonframework:axon-test:${axonVersion}")

    // JUnit
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("junit:junit")
}

group = "com.hiddewieringa"
version = "0.1.0"

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    jacoco {
        toolVersion = "0.8.7"
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.isEnabled = true
        }
    }
}

kotlinter {
    ignoreFailures = false
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = arrayOf(
        "filename",
        "import-ordering"
    )
}
