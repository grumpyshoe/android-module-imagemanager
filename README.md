
# Imagemanager

![AndroidStudio 4.1.1](https://img.shields.io/badge/Android_Studio-4.1.1-brightgreen.svg)
![minSDK 19](https://img.shields.io/badge/minSDK-API_19-orange.svg?style=flat)
![targetSDK 28](https://img.shields.io/badge/targetSDK-API_28-blue.svg)

`Imagemanager` is a small wrapper to avoid all the boilerplate code just to get a image from a camera or the gallery.

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

Add this dependency to your app _build.gradle_:
```gradle
implementation 'com.github.grumpyshoe:android-module-imagemanager:1.6.2'
```

## Usage

Get instance of ImageManager:
```kotlin
val imageManager: ImageManager = ImageManagerImpl()
```

Forward permission results to ImageManager for handling permission requests:
```kotlin
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
  if (!imageManager.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }
}
```


Forward activity results to ImageManager for handling Intent responses:
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (!imageManager.onActivityResult(applicationContext, requestCode, resultCode, data)) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
```

## Customize

If you want to customize the text that is shown at the _permission-explanation-dialog_ or the _source-chosser-dialog_ that an be done by adding the parameter `srcChooserDialog`, `cameraPermissionExplanation`, `cameraPermissionRetryExplanation`, `galleryPermissionRetryExplanation`and `galleryPermissionExplanation`.
All of these fields are optional, see the sample app.


### Dependencies
| Package  | Version  |
| ------------ | ------------ |
| com.github.grumpyshoe:android-module-permissionmanager  | 1.2.0  |
| com.github.grumpyshoe:android-module-intentutils | 1.1.0  |


## Need Help or something missing?

Please [submit an issue](https://github.com/grumpyshoe/android-module-imagemanager/issues) on GitHub.


## Changelog

See [CHANGELOG](CHANGELOG.md) for morre information.

## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file.


#### Build Environment
```
Android Studio 3.5
Build #AI-191.8026.42.35.5791312, built on August 9, 2019
JRE: 1.8.0_202-release-1483-b49-5587405 x86_64
JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
macOS 10.14.4
```
