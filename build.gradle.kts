plugins {
    // IntelliJ
    idea

    // Java
    java

    // Kotlin JVM
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)

    // Kotlinter
    alias(libs.plugins.kotlinter)

    // Spring
    alias(libs.plugins.spring.boot)

    // Test coverage
    jacoco
}

apply(plugin = "io.spring.dependency-management")

group = "com.hiddewieringa"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // In-memory H2 database
    implementation("com.h2database:h2")

    // Axon
    implementation(libs.axon.spring.boot) {
        exclude(group = "org.axonframework", module = "axon-server-connector")
    }
    testImplementation(libs.axon.test)

    // JUnit
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("junit:junit")
}

tasks {
    jacoco {
        toolVersion = "0.8.8"
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
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
