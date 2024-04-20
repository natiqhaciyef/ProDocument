pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
//        maven { url = uri("http://jcenter.bintray.com") }
//        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "ProDocument"
include(":app")
include(":common")
include(":data")
include(":domain")
