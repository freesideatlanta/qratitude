
Release Plan
============

Releases are feature sets that form a complete and working iteration of the application.

Each release is named after a phylum, because taxonomy.

Release: Arthropoda
-------------------
This is the initial release of QRatitude, and implements a basic product inventory application.

- User can provide credentials to the Android app
- User can initiate reset of password from app
- User can scan a QR code [done]
- User can perform data entry of product attributes [done]
- User can take photos of product [done]
- Photos are stored on SD card [done]
- User is authenticated via web service, when an upload is initiated
- On upload errors, user is notified and can try the upload again
- Product data is uploaded to server as an asynchronous, background service
- Product data is stored in a queryable database
- CRUD operations on product data are exposed via a RESTful API web service
- Web application authenticates user via web service
- Web application can query for and display product data
- Web application can edit product attributes
- Web application can accept photos of product for upload, or delete photos
- User can sign out of web application
- User can change username or password
- User can reset password via email

Release: Brachiopoda
--------------------
The next release of QRatitude will have pricing support and integration with Square.

- User can scan a QR code to edit product data
- User can initiate a sale via launching Square intent
- Product data is updated on successful sale (count decremented?  product marked as sold?)
- Audit data is uploaded to server (who made sale, what price)
- User can query in web application for product data missing pricing
- User can query the audit trail
- User can generate a simple (non-customizable) report of sales

Release: Chordata
-----------------
This release of QRatitude might expand existing features, like customizing reports, integration with other
mobile or web payment options, or perhaps introduce stricter validation on product data for better data
entry.
