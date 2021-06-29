plugins {
    kotlin("js")
}

group = "me.andrew"
version = "1.0-SNAPSHOT"

val kotlinxHtmlVersion = project.property("kotlinx.html.version") as String
val kotlinxSerializationVersion = project.property("kotlinx.serialization.version") as String
val kotlinxCoroutinesVersion = project.property("kotlinx.coroutines.version") as String
val kotlinWrappersSuffix = project.property("kotlin.wrappers.suffix") as String

kotlin {
    js(LEGACY) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            binaries.executable()
        }
    }

    dependencies {
        implementation("org.jetbrains:kotlin-react:17.0.1-pre.148-kotlin-1.4.30")
        implementation("org.jetbrains:kotlin-react-dom:17.0.1-pre.148-kotlin-1.4.30")
        implementation("org.jetbrains:kotlin-styled:5.2.1-pre.150-kotlin-1.4.31")
        implementation("io.github.samgarasx:kotlin-antd:4.8.6-pre.7-kotlin-1.4.30")
        implementation(npm("antd", "4.8.6"))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-js")

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-js:$kotlinxSerializationVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$kotlinxCoroutinesVersion")

        implementation(project(":shared"))

        implementation(npm("core-js", "2.6.5"))
        implementation(npm("svg-inline-loader", "0.8.2"))
        implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinxHtmlVersion")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-$kotlinWrappersSuffix")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-$kotlinWrappersSuffix")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.0-$kotlinWrappersSuffix")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-extensions:1.0.1-$kotlinWrappersSuffix")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-$kotlinWrappersSuffix")
        implementation(npm("inline-style-prefixer", "6.0.0"))
        implementation(npm("url-loader", "4.1.1"))

        testImplementation("org.jetbrains.kotlin:kotlin-test-js")
        testImplementation(npm("enzyme", "3.11.0"))
        testImplementation(npm("enzyme-adapter-react-16", "1.12.1"))
    }

    tasks.getByName<KotlinWebpack>("jsBrowserDevelopmentWebpack") {
        outputFileName = "js.js"
    }

    tasks.named("run") {
        dependsOn(":server:prepareDevServer")
    }

    val browserDist by configurations.creating {
        isCanBeConsumed = true
        isCanBeResolved = false
    }

    artifacts {
        add(browserDist.name, tasks.named("browserDistribution").map { it.outputs.files.files.single() })
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-reflect") {
                useVersion("1.5.0-RC")
            }
        }
    }
}