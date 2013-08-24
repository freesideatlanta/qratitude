# QRatitude Inventory System
QRatitude is an inventory management system consisting of:
* an Android app for scanning QR codes (encoded psuedo-GUIDs) and uploading associated product data
* a java REST web service that exposes an API to store and query the product data

## Development Setup
There are several handy scripts to setup, build, and run the QRatitude system from CLI.  
Your mileage may vary with Eclipse or non-*nix environments.

### Installs
* install mongodb
* install java SDK
* install [Android SDK](http://developer.android.com/sdk/index.html)

### App Setup
* run `android avd`
* create an AVD for Platform 4.0, API Level 14, and with an SD card (256MB)
* launch the AVD from the tool (or modify `app/launch.sh`)
* run `app/install.sh` to bootstrap AVD with ZXing Barcode Scanner
* run `app/stage.sh` to build `bin/qratitude-debug.apk` and load QRatitude to AVD

### Service Setup
* run `mvn clean install` to build web service
* run `mvn cargo:start` to run the web service on `localhost:8080`

## License/Legal
Copyright (c) 2013 Freeside Technology Spaces.

The QRatitude software is released under the [MIT License](http://opensource.org/licenses/MIT).  
Assets (creative works) are released under the [Creative Commons CC-BY-SA license](http://creativecommons.org/licenses/by-sa/3.0).
