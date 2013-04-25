
Release Plan
============

Releases are feature sets that form a complete and working iteration of the application.

Each release is named after a phylum, because taxonomy.

Release: Arthropoda
-------------------
This is the initial release of QRatitude, and implements a basic product inventory application.

### Android App

- User can provide credentials to the Android app [done]
- User can scan a QR code [done]
- User can perform data entry of product attributes [done]
- User can take photos of product [done]
- Photos are stored on SD card [done]
- On expired or failed authentication token, the user is prompted to re-enter credentials
- On upload errors, user is notified and can try the upload again
- Product data is uploaded to server as an asynchronous, background service

### Web Service (API)

- User is authenticated via web service, providing an auth token to the app
- Product data is stored in a queryable database
- CRUD operations on product data are exposed via a RESTful API web service

### Web Application

- Web application authenticates user via web service, providing an auth token to the session
- Web application can query for and display product data
- Web application can edit product attributes
- Web application can accept photos of product for upload, or delete photos
- User can sign out of web application
- User can change username or password on the web application
- User can reset password via email on the web application

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
entry.  The main feature will be attempting to integrate GPS coordinates and to store them in PostGIS.

- On QR code scan, the GPS coordinates are read from the phone and stored in the product data in the Android app
- When GPS coordinates (as part of product data) are posted to the server, they create a spatial record
- The spatial record (could be columns in the product data table) includes GPS coordinates, and a point shape
- Some preset spatial queries can be run against the data from the web application
- Integrate some open source geocoding solution to lookup addresses from GPS coordinates
- User initiates geocoding for a product from the web application (could be turned into a server queue/process)
- Store geocoded address record as part of the product data, support CRUD operations


Ideas
-----

These are some ideas that we could incorporate into one the planned or future releases.  There are clarifications
after the list of ideas.

- Introduce a Facility[0] class, which is 1:N with Product (product gets new foreign key, facility_id)
- User can specify facility in the settings of the Android app (onsite)
- User can specify no facility in settings (roaming)
- User can query for product location within facility[1]
- When a user scans a product that was not associated to a facility, they can set the facility as part of product editing[2]
- User can enable a setting which defaults to a facility on product edit
- User can initiate a spatial query for products in facilities located within X distance from their location
- User can query for facilities located within X distance from their location

[0]: Facilities are where the products are stored.  Some users will always use the app on the smartphone within
a facility (say, as part of product intake).  Others may go out and source the products from various locations
(which are non-facility).  So, there needs to be a way to specify both cases.

[1]: Depending on the resolution (accuracy) of the GPS in practice.  This might work well in a large facility.

[2]: This models product intake.  Presumably, users that are roaming show up with a bunch of products already tagged.
The intake processor will scan each product and set the facility.
