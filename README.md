
# Imagemanager

![AndroidStudio 3.5](https://img.shields.io/badge/Android_Studio-3.5-brightgreen.svg)
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
implementation 'com.github.grumpyshoe:android-module-imagemanager:1.3.1'
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

If you want to customize the text that is shown at the _permission-explanation-dialog_ or the _source-chosser-dialog_ there're two option how that can be done:

1. Create your own version of these strings in your projects _strings.xml_:

| String ID  | Default value |
| ------------ | ------------ |
| imagemanager_source_chooser_dialog_title | Choose image source |
| imagemanager_add_image_from_camera_dialog_title | Create new image |
| imagemanager_add_image_from_gallery_dialog_title | Add from gallery |
| imagemanager_camera_permission_explanation_title | Camera Permission |
| imagemanager_camera_permission_explanation_message | The App needs the Camera Permission to be able to create new images |
| imagemanager_camera_permission_explanation_retry_title | Camera Permission |
| imagemanager_camera_permission_explanation_retry_message | Without this permission you will not be able to get new images from your camera. |
| imagemanager_gallery_permission_explanation_title | External Storage Permission |
| imagemanager_gallery_permission_explanation_message | The App needs access to your external storage to be able to show your images. |
| imagemanager_gallery_permission_explanation_retry_title | External Storage Permission |
| imagemanager_gallery_permission_explanation_retry_message | Without this permission you will not be able to get new images from your gallery. |

2. Overwrite config (example at MainActivity):

You don't have  to override all texts, just the one you want.
```kotlin
val config = ImagemanagerConfig
config.texts = ImagemanagerConfig.Texts(
    imagemanager_source_chooser_dialog_title = "My Custom Chooser Title",
    imagemanager_add_image_from_camera_dialog_title = "Use My Camera"
)
```



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
