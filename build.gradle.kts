plugins {
    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    kotlin("android") version "1.7.10" apply false
    id("org.jetbrains.kotlin.jvm") version "1.7.10" apply false
    id("com.flaviofaria.catalog") version "0.1" apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.7.10"))
    }
}

subprojects {
    apply(plugin = "com.flaviofaria.catalog")
}


allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xcontext-receivers")
        }
    }
}