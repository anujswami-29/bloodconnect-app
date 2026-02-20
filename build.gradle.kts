plugins {
    // Keep root build lightweight; Android plugin is applied in :app
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

