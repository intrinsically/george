import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform") version "1.5.20-RC"
    application
}

group = "me.andrew"
version = "1.0-SNAPSHOT"

val ktor_version: String by project
val logback_version: String by project

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
        withJava()
    }
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.0-RC")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.google.code.gson:gson:2.8.7")
                implementation("io.ktor:ktor-server-netty:$ktor_version")
                implementation("io.ktor:ktor-html-builder:$ktor_version")
                implementation("io.ktor:ktor-websockets:$ktor_version")
                implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
                implementation("ch.qos.logback:logback-classic:$logback_version")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains:kotlin-react:17.0.1-pre.148-kotlin-1.4.30")
                implementation("org.jetbrains:kotlin-react-dom:17.0.1-pre.148-kotlin-1.4.30")
                implementation("org.jetbrains:kotlin-styled:5.2.1-pre.150-kotlin-1.4.31")
                implementation("io.github.samgarasx:kotlin-antd:4.8.6-pre.7-kotlin-1.4.30")
                implementation("io.ktor:ktor-http-cio-js:$ktor_version")
                implementation("io.ktor:ktor-client-websockets:$ktor_version")
                implementation(npm("antd", "4.8.6"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

application {
    mainClassName = "costanza.george.server.ServerKt"
}

tasks.getByName<KotlinWebpack>("jsBrowserDevelopmentWebpack") {
    outputFileName = "js.js"
}

tasks.getByName<Jar>("jvmJar") {
    dependsOn(tasks.getByName("jsBrowserDevelopmentWebpack"))
    val jsBrowserDevelopmentWebpack = tasks.getByName<KotlinWebpack>("jsBrowserDevelopmentWebpack")
    from(File(jsBrowserDevelopmentWebpack.destinationDirectory, jsBrowserDevelopmentWebpack.outputFileName))
}

tasks.getByName<JavaExec>("run") {
    dependsOn(tasks.getByName<Jar>("jvmJar"))
    classpath(tasks.getByName<Jar>("jvmJar"))
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-reflect") {
            useVersion("1.5.20-RC")
        }
    }
}

// avoid horrendous gradle 7 duplicates error bug
gradle.taskGraph.whenReady {
    allTasks
        .filter { it.hasProperty("duplicatesStrategy") } // Because it's some weird decorated wrapper that I can't cast.
        .forEach {
            it.setProperty("duplicatesStrategy", "EXCLUDE")
        }
}