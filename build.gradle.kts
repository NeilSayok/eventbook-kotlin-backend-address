import java.util.*

val applicationGroup: String by project
val appVersion: String by project


plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = applicationGroup
version = appVersion

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
}

sourceSets {
    main {
        kotlin {
            srcDir(layout.buildDirectory.dir("generated/source/buildConfig"))
        }
    }
}

val generateBuildConfig by tasks.registering {
    val outputDir =
        layout.buildDirectory.dir("generated/source/buildConfig/${applicationGroup.replace(".", "/")}").get().asFile
    val propertiesFile = file("local.properties")

    inputs.file(propertiesFile)
    outputs.dir(outputDir)

    doLast {
        val properties = Properties().apply { load(propertiesFile.inputStream()) }

        val buildConfigContent = buildString {
            appendLine("package $applicationGroup")
            appendLine("object BuildConfig {")
            properties.forEach { (key, value) ->
                appendLine("    const val ${key.toString().uppercase()} = \"${value}\"")
            }
            appendLine("    const val APPLICATION_NAME = \"${applicationGroup}\"")

            appendLine("}")
        }

        outputDir.mkdirs()
        val buildConfigFile = File(outputDir, "BuildConfig.kt")
        buildConfigFile.writeText(buildConfigContent)
    }
}

dependencies {
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.rate.limiting)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)

    implementation(libs.ktor.cio.jvm)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.content.negotiation)

    implementation(libs.ktor.jackson.jvm)



    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}

tasks.named("compileKotlin") {
    dependsOn(generateBuildConfig)
}

tasks.named("run") {
    dependsOn(generateBuildConfig)
}
tasks.named("build") {
    dependsOn(generateBuildConfig)
}

val renamePackage by tasks.registering {
    val oldPackageName = "com.eventbook"
    val newPackageName = applicationGroup
    val sourceDirs = listOf("src/main/kotlin", "src/main/resources", "src/test/kotlin")

    doLast {
        val oldPackagePath = oldPackageName.replace(".", "/")
        val newPackagePath = newPackageName.replace(".", "/")

        sourceDirs.forEach { sourceDir ->
            fileTree(sourceDir) {
                include("**/*.*")
            }.forEach { file ->
                val content = file.readText()
                val newContent = content.replace(oldPackageName, newPackageName)
                file.writeText(newContent)
            }

            fileTree(sourceDir) {
                include("**/$oldPackagePath/**")
            }.forEach { file ->
                val newFilePath = file.absolutePath.replace(oldPackagePath, newPackagePath)
                val newFile = File(newFilePath)
                newFile.parentFile.mkdirs()
                file.renameTo(newFile)
            }
        }
    }
}