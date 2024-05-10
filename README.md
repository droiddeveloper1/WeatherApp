# WeatherApp
A Kotlin Jetpack Compose app that allows user to enter location(city) info and fetches the local weather info from a remote
server, then displays pieces of relevant weather info back tothe user along with the appropriate weather graphic/icon for the
current weather conditions in that city.

Requests location access in order to perform a reverse geo lookup, determine the local city name, then fetch the weather info and
auto-populate the UI fields with local weather.



# @ToDo
- add unit tests to cover the domain layer use cases and add more dependency injections as needed to facilitate unit testing
- fix instrumented unit tests 
- replace SharedPreferences mechanism with Preferences Datastore mechanism
- create a flexible Units selection mechanism instead of hardcoding to metric units
- move the API key away from BuildConfig and into JNI/C++ layer instead
- handle case of ungranted permissions in the LocationManager class (display some message in the UI etc.)
- fix positioning of snackbar to appear above the virtual keyboard and eliminate its late/duplicate instantiation
