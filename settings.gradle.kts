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
    // 👑 允许子模块混合私有仓库，解决 Flutter 内部加载问题
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // 确保你的图表库可以顺利下载
        maven { url = java.net.URI("https://jitpack.io") }
    }
}

rootProject.name = "myCF"
include(":app")

// 👑 标准手动申明：直接把本地的 flutter 子项目引入，不走隐式加载
include(":my_flutter_module")
project(":my_flutter_module").projectDir = file("../my_flutter_module")