plugins {
    id("com.android.library")
    id("maven-publish")
    kotlin("android")
    id("kotlin-kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 25
        targetSdk = 31
        version = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }

    buildFeatures {
        dataBinding = true
        compose = true
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    // App Compat
    implementation(Deps.appCompat)

    // Compose
    implementation(Deps.composeRuntime)
    implementation(Deps.composeUI)
    implementation(Deps.composeFoundation)
    implementation(Deps.composeFoundationLayout)
    implementation(Deps.composeMaterial)
    implementation(Deps.composeMaterial3)
    implementation(Deps.composeRuntimeLivedata)
    implementation(Deps.composeUITooling)
    implementation(Deps.composeMaterialIcons)
    implementation(Deps.composeActivity)
    implementation(Deps.composeNavigation)

    // Core KTX
    implementation(Deps.core)

    // Material UI
    implementation(Deps.material)

    // Scitalys
    implementation(project(":bp_traits"))

    // Timber
    implementation(Deps.timber)

    testImplementation(TestingDeps.junit)
    androidTestImplementation(TestingDeps.androidJunit)
    androidTestImplementation(TestingDeps.espresso)

}

// Publishing

val env = System.getenv()
val github_usr = env["GITHUB_USR"]
val github_key = env["GITHUB_PACKAGES_KEY"]

fun getVersionName(): String {
    return "1.2.6-alpha02"
}

fun getArtifactId(): String {
    return "ui"
}

publishing {
    publications {
        create<MavenPublication>("gpr") {

            groupId = "com.scitalys"
            artifactId = getArtifactId()
            version = getVersionName()
            artifact("$buildDir/outputs/aar/${getArtifactId()}-release.aar")

            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")

                configurations.api.get().allDependencies.forEach {
                    val dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", it.group)
                    dependencyNode.appendNode("artifactId", it.name)
                    dependencyNode.appendNode("version", it.version)
                }
            }

        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/beneventolorenzo/ui")
            credentials {
                username = github_usr
                password = github_key
            }
        }
    }

}

tasks.register("deleteArtifact", Delete::class.java) {
    delete("$buildDir/outputs/aar/${getArtifactId()}-release.aar")
}