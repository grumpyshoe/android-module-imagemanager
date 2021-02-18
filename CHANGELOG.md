

# Changelog

## 1.6.2

### Bugfixes
* create own fileprovider so it doesn't conflict other provider


## 1.6.0

### BREAKING CHANGES
* `ImagemanagerConfig` is not longer available. The String no can be set directly on image request via `srcChooserDialog`, `cameraPermissionExplanation`, `cameraPermissionRetryExplanation`, `galleryPermissionRetryExplanation`and `galleryPermissionExplanation`.
All of these fields are optional, see the sample app.


## 1.4.0

### Bugfixes


## 1.3.3

### Features
* Added option to delete images from disk

### Bugfixes
* fix path for loading image from disk


## 1.3.2

### Bugfixes
* Just another commit because otherwise Jitpack wouldn't recognize the changes ( dont ask me why ;) )


## 1.3.1

### Bugfixes
* Fix unknown source type


## 1.3.0

### Features
* Added option to load images from local storage


## 1.2.0

### Features

* Added function for saving a bitmap to local storage