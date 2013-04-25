QRatitude: A QR-code based Inventory Tagging System
===================================================

This is an Android application that implements a simple workflow for tagging inventory with pre-printed (<b>NOTE:</b> non-URL) QR codes.


It uses the [ZXing](https://code.google.com/p/zxing) android-integration library, which installs the ZXing barcode scanning application separately.

Workflow/Code Overview
----------------------

0. User takes a pre-printed QR code (representing a psuedo-GUID) and slaps it on a product.

1. User opens the application to the *ScanActivity*, and is immediately presented a button to start the camera to scan QR codes (using [ZXing](https://code.google.com/p/zxing/wiki/ScanningViaIntent) as described [here](http://stackoverflow.com/a/6735148/264961).

2. User scans a QR code and is taken to the *ProductDataEntry* view/intent.  This is where the user will enter in information about the tagged product (see examples of the products and the final form of the website [here](http://inventory.lifecyclebuildingcenter.org/).  If the user cancels, the app returns to the *ScanActivity*.

3. (Optional) User can take a few photos of the scanned items.

4. User then presses *Upload* to send the QR code (psuedo-GUID) plus *Form* data to a server for storage.  Ideally, this is a background task so the user can return to the camera to continue scanning.  It looks like we want to use an *IntentService* for this step.

5. (Optional) User can open a view that shows the progress of current uploads, similar to how one can view the progress of downloads in a typical browser app.  We'll have to figure out how to update this view from the *IntentService*, which seems to have some hooks for reporting progress.

Authentication to the server is handled via a separate view/intent, *Settings* where the user sets their username and password.  In *Settings* the user can set to enable compression of photos for shorter upload time.

Server-side Options
-------------------

There are several options for how to handle the data once it reaches server-side, so that implementation of the site in the final form (shown [here](http://inventory.lifecyclebuildingcenter.org)) could be made easy.  Feel free to add your thoughts below.

- (<b>emptyset</b>) I would store the data in postgresql.  Tables would be simple: *products* (productid, qrcode, form data ...), *images* (productid, image (binary)).  I would write a small API in whatever (java, node.js) that exposed a few methods for basic CRUD on this data.  Then, both the Android app and the website could consume the API.

- (<b>thrillgore</b>) The API itself should be JSON-based as we can use some object mappers for Java such as Jackson or GSON to map out those attributes into POJOs, and accelerate development. 

Further Directions
------------------

Some very easy improvements to the app are readily possible after the initial workflow is nailed:

- When an upload fails, the user can restart the upload at a later time.  This implies local store of form data (photos are stored by default on the SD card) that could be retrieved later.

- Editing and saving of form data, by scanning a QR code of an already uploaded product.

- ZXing supports barcode scanning, too - so perhaps the initial *ScanActivity* can also include an option to scan barcodes (if needed).

Signing
-------

In order to sign the release, the `sign.sh` script assumes a symlink to `freesideatlanta.keystore` in the qratitude top directory.
That symlink points to the actual keystore file, which is currently kept in an encrypted container that needs to be mounted.
Access to that (and therefore, the ability to sign the release apk) is reserved to Freeside Atlanta.

Here's [one opinion](http://stackoverflow.com/a/4053381/172217) on signing open-source software in the Android universe.

License/Legal
-------------

QRatitude is released under the [Creative Commons CC-BY-SA license](http://creativecommons.org/licenses/by-sa/3.0).

The ZXing android-integration library is released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).
