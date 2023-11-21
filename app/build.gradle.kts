plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.quanlyquanan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quanlyquanan"
        minSdk = 19
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    //noinspection GradleCompatible
//    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("de.hdodenhof:circleimageview:2.1.0") // circle img

    implementation("androidx.cardview:cardview:1.0.0")

    implementation ("com.squareup.picasso:picasso:2.71828") // load img
    implementation ("com.squareup.retrofit2:retrofit:2.5.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.0.2") // create chart

    implementation ("com.github.ybq:Android-SpinKit:1.4.0") // custom progressbar

}