import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val properties = Properties().apply { load(FileInputStream(rootProject.file("local.properties"))) }

android {
    namespace = "com.team.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.team.data.TestRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Enable room auto-migrations
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        buildConfigField("String", "FLIP_SERVER_URL", gradleLocalProperties(rootDir).getProperty("FLIP_SERVER_URL"))
        buildConfigField("String", "FLIP_MOCK_SERVER_URL", gradleLocalProperties(rootDir).getProperty("FLIP_MOCK_SERVER_URL"))
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
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

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(project(":domain"))

    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // Testing
    kspTest(libs.hilt.android.compiler)
    testImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.hilt.android.testing)

    // Coroutine
    implementation(libs.kotlinx.coroutines.android)
    // Testing
    testImplementation(libs.kotlinx.coroutines.test)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit & OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Paging3
    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.androidx.paging.common)

    // Testing
    testImplementation(libs.retrofit.mock)
    testImplementation(libs.okhttp.mockWebServer)

    // Local tests: JUnit, Coroutines Test, Android runner, Mockito
    implementation(libs.androidx.test.runner)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.mockK)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.robolectric)

    // DataStore
    implementation(libs.androidx.dataStore.preference)
}