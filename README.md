# QRatitude Inventory System
QRatitude is an inventory management system consisting of:
* an Android app for scanning QR codes (encoded psuedo-GUIDs) and uploading associated product data
* a Node.js web service that exposes an API to store and query the product data
* (coming soon) a website front-end that consumes the API

## Development Setup

### Installs
* install mongodb
* install node.js
* install [Android SDK](http://developer.android.com/sdk/index.html)

### App Setup
* run `android avd`
* create an AVD for Platform 4.0, API Level 14, and with an SD card (256MB)
* launch the AVD from the tool (or modify `app/launch.sh`)
* run `app/install.sh` to bootstrap AVD with ZXing Barcode Scanner
* run `app/stage.sh` to build `bin/qratitude-debug.apk` and load QRatitude to AVD

### Service Setup
* run `service/database/mongo/create.sh` to bootstrap database
* run `node service/app.js` to run the web service

### License/Legal
Copyright (c) 2013 Freeside Technology Spaces.

The QRatitude software is released under the [MIT License](http://opensource.org/licenses/MIT).  
Assets (creative works) are released under the [Creative Commons CC-BY-SA license](http://creativecommons.org/licenses/by-sa/3.0).
