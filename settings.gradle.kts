pluginManagement {
    repositories {
        google {
            content {
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                includeGroupAndSubgroups("androidx")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // 👑 核心修复：把 JitPack 仓库地址引入进来，这样系统就能顺利下载 MPAndroidChart 图表库了！
        maven { url = java.net.URI("https://jitpack.io") }
    }
}

rootProject.name = "myCF"
include(":app")