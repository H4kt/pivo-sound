import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {

    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)

    alias(libs.plugins.kordex)
    alias(libs.plugins.shadow)

}

group = "dev.h4kt"
version = "2.0.0"

repositories {

    mavenCentral()

    maven("https://jitpack.io")
    maven("https://maven.lavalink.dev/releases")
    maven("https://maven.lavalink.dev/snapshots")

}

dependencies {

    implementation(libs.kotlinx.serialization.hocon)
    implementation(libs.lavaplayer)
    implementation(libs.lavalink.youtube.v2)
    implementation(libs.slf4j)

    implementation(libs.koin.annotatins)
    ksp(libs.koin.compiler)

}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

kordEx {

    kordExVersion = "2.3.0-SNAPSHOT"
    kordVersion = "0.16.0-SNAPSHOT"

    i18n {
        classPackage = "dev.h4kt.pivosound.generated.i18n"
        translationBundle = "english.strings"
    }

    bot {
        mainClass = "dev.h4kt.pivosound.AppKt"
        voice = true
    }

}

tasks.withType<ShadowJar> {
    archiveFileName.set("pivo-sound-all.jar")
}

afterEvaluate {
    tasks["run"].apply {
        (this as JavaExec).workingDir = File("run")
    }
}
