# Around Seattle

This is a simple android app that uses the [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/). It allows the user to search and favorite places in Seattle, WA.

It uses the Foursquare API for places search, and Google Maps for map display.

To build:

```git clone https://github.com/plarios/android_test
cd android_test/AroundSeattle
./gradlew build
```

(or open `android_test/AroundSeattle` in Android Studio)

You'll want to use valid Foursquare API and Google Maps credentials.
Take a look at `app/res/values/google_maps_api.xml` and `app/res/values/config.xml` for this.

In addition to getting a Google Maps API key, you'll also want to enable the Static Maps API for that key, and create a signing secret. Otherwise, the static maps URLs will most likely not work (the quota is 1 image per day for unsigned requests on the free tier). See [dev-guide](https://developers.google.com/maps/documentation/maps-static/dev-guide) and [usage-and-billing](https://developers.google.com/maps/documentation/maps-static/usage-and-billing) for details.
