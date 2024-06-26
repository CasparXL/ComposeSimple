import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.get().pluginId)
    id(libs.plugins.hilt.get().pluginId)
    id(libs.plugins.compose.get().pluginId)
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    signingConfigs {
        create("release") {
            keyAlias = "key0"
            keyPassword = "123456"
            storeFile = file("../test.jks")
            storePassword = "123456"
        }
    }
    namespace = libs.versions.applicationId.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false   //开启混淆
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    //parsed using java 11 syntax
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    //kotlin的java11支持
    kotlinOptions {
        jvmTarget = "17"
    }
    javaToolchains {
        version = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0"
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    //apk输出格式
    applicationVariants.all {
        val dtfInput = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.getDefault())
        val format = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(dtfInput)
        // Apk 输出配置
        val buildType = buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                outputFileName = if (buildType == "release") {
                    "${rootProject.name}_v${versionCode}_${format}_release.apk"
                } else {
                    "${rootProject.name}_v${versionCode}_${format}_debug.apk"
                }
            }
        }
    }
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.kotlin.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.hilt.navigation)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.paging)
    implementation(libs.gson)
    implementation(libs.coil)
    implementation(libs.mmkv)
    implementation(libs.bundles.immersionbar)
    kapt(libs.bundles.kapt)
    testImplementation(libs.test.impl)
    androidTestImplementation(libs.bundles.android.test.impl)
    debugImplementation(libs.bundles.debug.impl)
}