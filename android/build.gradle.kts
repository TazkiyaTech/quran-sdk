import com.android.build.api.dsl.LibraryExtension

private val targetSdk = 36

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jreleaser)
    `maven-publish`
}

configure<LibraryExtension> {
    compileSdk = targetSdk
    namespace = "com.tazkiyatech.quran.sdk"

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    lint {
        abortOnError = true
        targetSdk = targetSdk
    }

    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }

    testOptions {
        animationsDisabled = true
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        targetSdk = targetSdk
    }
}

dependencies {
    implementation(libs.androidx.annotation)

    testRuntimeOnly(libs.junit.platform.launcher)

    testImplementation(libs.hamcrest.library)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.tazkiyatech.test.utils)
    androidTestImplementation(libs.hamcrest.library)

    androidTestUtil(libs.test.orchestrator)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
            }

            groupId = properties["groupId"].toString()
            artifactId = properties["artifactId"].toString()
            version = properties["version"].toString()

            pom {
                name = project.properties["artifactId"].toString()
                description = "An Android library that gives you easy, offline access to verses of the Quran in your Android projects."
                url = "https://github.com/TazkiyaTech/quran-sdk"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                organization {
                    name = "Tazkiya Tech"
                    url = "http://tazkiyatech.com"
                }
                developers {
                    developer {
                        id = "adil-hussain-84"
                        name = "Adil Hussain"
                        email = "adilson05uk@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/TazkiyaTech/quran-sdk.git"
                    developerConnection = "scm:git:ssh://github.com:TazkiyaTech/quran-sdk.git"
                    url = "https://github.com/TazkiyaTech/quran-sdk"
                }
            }
        }
    }

    repositories {
        maven {
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}

// the "JRELEASER_..." properties required by the "jreleaserDeploy" task are defined outside of this project in the "~/.jreleaser/config.toml" file
jreleaser {
    gitRootSearch.set(true)
    signing {
        pgp {
            setActive("ALWAYS")
            armored = true
        }
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    setActive("RELEASE")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")

                    // Setting "verifyPom" to "false" bypasses the "Unknown packaging: aar" error
                    verifyPom = false
                }
            }
        }
    }
}
