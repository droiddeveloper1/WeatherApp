# WeatherApp
A Kotlin Jetpack Compose app that allows user to enter location(city) and fetches the associated local weather info(JSON) from a remote
server, converts to data class objects then displays pieces of relevant weather data to the user along with the appropriate weather graphic/icon for the
current weather conditions in that city.

Requests location access in order to perform a reverse geo lookup, determine the local city name, then fetch the weather info and
auto-populate the UI fields with local weather.

# Technologies used
Kotlin

Gradle kts

Gradle Version Catalog

Dagger-Hilt

MVVM

Clean Architecture (Presentation Layer, Domain Layer, Repository Layer, Dependency Injection)

Coroutines

Flow (facilitates communication between architecture layers)

Retrofit

Okhttp

Coil

Jetpack Compose

Jetpack Navigation

LocationServices

JUnit

Mockito

SOLID design principle

OO Encapsulation (separation of Viewmodels from Composables)


# @ToDo
- add unit tests to cover the domain layer use cases and add more dependency injections as needed to facilitate unit testing
- expand instrumented unit tests 
- replace *SharedPreferences* mechanism with *Preferences Datastore* mechanism
- create a flexible Units selection mechanism instead of hardcoding to metric units
- move the API key away from BuildConfig and into JNI/C++ layer instead
- refactor the location permissions mechanism to auto-fetch&display immediately after the *initial* app install. (Currently will only auto-fetch for subsequent usages of the app, not at the very first session.)
- handle case of ungranted permissions in the *LocationManager* class (display some message in the UI etc.)
- fix positioning of snackbar to appear above the virtual keyboard and eliminate its delayed/extra rendering


-Oke Uwechue, Ph.D.
