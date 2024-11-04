plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    implementation(libs.dagger.hilt.javax)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockK)
    testImplementation(libs.kotlinx.coroutines.test)

    // Paging (without Android dependencies)
    implementation(libs.androidx.paging.common)
}