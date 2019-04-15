
# Imagemanager

![AndroidStudio 3.3.2](https://img.shields.io/badge/Android_Studio-3.3.2-brightgreen.svg)
![minSDK 16](https://img.shields.io/badge/minSDK-API_16-orange.svg?style=flat)
  ![targetSDK 27](https://img.shields.io/badge/targetSDK-API_27-blue.svg)

`Imagemanager` is a small wrapper for all the things that need to be done just to get a image from camera or gallery.

## Installation

Add `jitpack`to your repositories
```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
    ...
}
```

Add this dependency to your app _build.gradle_ and apply the plugin at the bottom:
```gradle
implementation 'com.github.grumpyshoe:android-module-imagemanager:1.0.0'
```

## Usage

Get instance of ImageManager:
```kotlin
val imageManager: ImageManager = ImageManagerImpl()
```

Forward permission results to ImageHandler for handling permission requests:
```kotlin
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
  if (!imageManager.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }
}
```


Forward activity results to ImageHandler for handling Intent responses:
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (!imageManager.onActivityResult(applicationContext, requestCode, resultCode, data)) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
```


### Dependencies
| Package  | Version  |
| ------------ | ------------ |
| com.github.grumpyshoe:android-module-permissionmanager  | 1.2.0  |


## Need Help or something missing?

Please [submit an issue](https://github.com/grumpyshoe/android-module-imagemanager/issues) on GitHub.


## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file.


#### Build Environment
```
Android Studio 3.3.2
Build #AI-182.5107.16.33.5314842, built on February 16, 2019
JRE: 1.8.0_152-release-1248-b01 x86_64
JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
macOS 10.14
```
