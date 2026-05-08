import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // the "javax.activation" library is needed to get the project to Gradle Sync successfully with version 9 of the Android Gradle Plugin. See https://github.com/jreleaser/jreleaser/discussions/2059
        classpath(libs.javax.activation)
    }
}

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.jreleaser) apply false
}

tasks.wrapper {
    gradleVersion = "9.5.0"
}

// Alter the default behaviour of the "com.github.ben-manes.versions" plugin
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf { isNonStable(candidate.version) }
}

/**
 * @param version The version name to evaluate.
 * @return true if and only if the given version name represents a non-stable version.
 */
fun isNonStable(version: String): Boolean {
    val versionInLowercase = version.lowercase()

    return setOf("-alpha", "-beta", "-rc").any { versionInLowercase.contains(it) }
}
