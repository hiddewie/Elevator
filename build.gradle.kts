buildscript {
    val kotlinVersion = "1.3.21"
    val springBootVersion = "2.1.2.RELEASE"

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
    val kotlinVersion = "1.3.21"
    val springBootVersion = "2.1.2.RELEASE"
    val springDependencyManagementVersion = "1.0.6.RELEASE"

    // IntelliJ
    idea

    // Java
    java

    // Kotlin JVM
    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion

    // Spring
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version springDependencyManagementVersion

    // Test coverage
    jacoco
}

val kotlinVersion = "1.3.21"
val axonVersion = "4.0.4"
val mariaDbVersion = "1.5.7"
val javassistVersion = "3.23.1-GA"

repositories {
    mavenCentral()
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // Spring
    compile("org.springframework.boot:spring-boot-starter-webflux")
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    testCompile("org.springframework.boot:spring-boot-starter-test")

    // Java 11 Javassist
    compile("org.javassist:javassist:$javassistVersion")

    // Kotlin
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlin:kotlin-reflect")

    // MariaDB
    compile("org.mariadb.jdbc:mariadb-java-client:$mariaDbVersion")

    // Axon
    compile("org.axonframework:axon-spring-boot-starter:$axonVersion") {
        exclude(group = "org.axonframework", module = "axon-server-connector")
    }
    testCompile("org.axonframework:axon-test:${axonVersion}")

    // JUnit
    testCompile("org.jetbrains.kotlin:kotlin-test")
    testCompile("org.jetbrains.kotlin:kotlin-test-junit")
    testCompile("junit:junit")
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
        toolVersion = "0.8.2"
    }

    jacocoTestReport {
        reports {
            xml.isEnabled = true
        }
    }
}

//if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
//    val srcModule = "src/main/kotlin"
//    val moduleInfo = file("${project.projectDir}/$srcModule/module-info.java")
//    if (moduleInfo.exists()) {
//        sourceSets {
//            getByName("module").java.srcDirs(srcModule)
//            getByName("module").java.compileClasspath = main.compileClasspath
//            getByName("module").java.sourceCompatibility = JavaVersion.VERSION_1_9
//            getByName("module").java.targetCompatibility = JavaVersion.VERSION_1_9
////                }
////            }
//            getByName("main").kotlin .srcDirs(srcModule)
////        }
////            }
//        }
//
//        compileModuleJava.configure {
//            dependsOn(compileKotlin)
//            destinationDir = compileKotlin.destinationDir
//            doFirst {
//                options.compilerArgs = listOf("--module-path", classpath.asPath)
//                classpath = files()
//            }
//        }
//        jar.dependsOn(compileModuleJava)
//    }
//}
