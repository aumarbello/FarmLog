@file:Suppress("SpellCheckingInspection", "unused")

object Versions {
    const val kotlin = "1.3.41"
    const val gradlePlugin = "3.4.2"
    const val minSdk = 21
    const val targetSdk = 29
    const val compileSdk = 29
    const val versionCode = 1
    const val versionName = "1.0"
    const val buildTool = "29.0.2"
    const val appCompat = "1.0.2"
    const val coreKtx = "1.0.2"
    const val fragment = "1.2.3"
    const val material = "1.2.0-alpha05"
    const val constraintLayout = "2.0.0-beta1"
    const val testRunner = "1.2.0"
    const val testExt = "1.1.0"
    const val junit = "4.12"
    const val espressoCore = "3.2.0"
    const val espressoIntents = "3.1.0"
    const val dagger = "2.24"
    const val lifecycle = "2.2.0-alpha01"
    const val mockito = "2.23.0"
    const val viewmodelKtx = "2.2.0-alpha05"
    const val navigation = "2.2.1"
    const val timber = "4.5.1"
    const val couroutines = "1.3.2"
    const val room = "2.2.5"
    const val architectureCore = "2.0.1"
    const val dataBinding = "3.6.1"
    const val multiDex = "2.0.1"
    const val graphView = "v3.1.0"
    const val location = "17.0.0"
    const val mapBox = "9.1.0"
    const val mapBoxAnnotations = "0.8.0"
}

object Deps {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val architectureCore = "androidx.arch.core:core-common:${Versions.architectureCore}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val daggerRuntime = "com.google.dagger:dagger:${Versions.dagger}"
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle}"
    const val lifeCycleCommon = "androidx.lifecycle:lifecycle-common:${Versions.lifecycle}"
    const val liveDataCoreKtx = "androidx.lifecycle:lifecycle-livedata-core-ktx:${Versions.lifecycle}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodelKtx}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.couroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.couroutines}"
    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val dataBinding = "androidx.databinding:databinding-compiler:${Versions.dataBinding}"
    const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val multiDex = "androidx.multidex:multidex:${Versions.multiDex}"
    const val graphView = "com.github.PhilJay:MPAndroidChart:${Versions.graphView}"
    const val location = "com.google.android.gms:play-services-location:${Versions.location}"
    const val mapBox = "com.mapbox.mapboxsdk:mapbox-android-sdk:${Versions.mapBox}"
    const val mapBoxAnnotation = "com.mapbox.mapboxsdk:mapbox-android-plugin-annotation-v9:${Versions.mapBoxAnnotations}"
}

object TestDeps {
    const val junit = "junit:junit:${Versions.junit}"
    const val testArchCore = "androidx.arch.core:core-testing:${Versions.architectureCore}"
    const val archCore = "androidx.arch.core:core-runtime:${Versions.architectureCore}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val testRules = "androidx.test:rules:${Versions.testRunner}"
    const val testExt = "androidx.test.ext:junit:${Versions.testExt}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espressoIntents}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockitoAndroid = "org.mockito:mockito-android:${Versions.mockito}"
    const val couroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.couroutines}"
    const val fragment = "androidx.fragment:fragment-testing:${Versions.fragment}"
    const val room = "androidx.room:room-testing:${Versions.room}"
}
