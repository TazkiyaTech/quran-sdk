import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.kotlin.android) apply false
}

tasks.wrapper {
    gradleVersion = "8.14.3"
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
