// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.compose) apply false
}

allprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                if (requested.name.startsWith("kotlin-stdlib")) {
                    useVersion("2.0.0")
                }
                if (requested.name.startsWith("kotlin-reflect")) {
                    useVersion("2.0.0")
                }
            }
        }
    }
    tasks.withType(JavaCompile::class.java) {
        options.encoding = "UTF-8"
        version = 17
    }

    tasks.withType(Javadoc::class.java) {
        options {
            encoding("UTF-8")
            charset("UTF-8")
            description = "http://docs.oracle.com/javase/11/docs/api"
        }
    }
    buildDir = File(rootDir, "build/${path.replace(':', '/')}")
}
