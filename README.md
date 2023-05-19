# Newsant News App 📰
An Android news application implemented using the MVVM pattern, Retrofit2, Dagger Hilt, LiveData, ViewModel, Coroutines, Room, Navigation Components, View Binding and some other libraries from the [Android Jetpack] . Newsant app fetches data from the [NewsAPI] .


## Architecture
The architecture of this application relies and complies with the following points below:
* A single-activity architecture, using the [Navigation Components](https://developer.android.com/guide/navigation) to manage fragment operations.
* Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)(MVVM) which facilitates a separation of development of the graphical user interface.
* [Android architecture components](https://developer.android.com/topic/libraries/architecture/) which help to keep the application robust, testable, and maintainable.

<p align="center"><a><img src="https://raw.githubusercontent.com/mayokunthefirst/Instant-Weather/master/media/final-architecture.png" width="700"></a></p>

## Technologies used:

* [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
* [Retrofit](https://square.github.io/retrofit/) a REST Client for Android which makes it relatively easy to retrieve and upload JSON (or other structured data) via a REST based webservice.
* [Dagger Hilt](https://dagger.dev/hilt/) for dependency injection.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) to store and manage UI-related data in a lifecycle conscious way.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) to handle data in a lifecycle-aware fashion.
* [Navigation Component](https://developer.android.com/guide/navigation) to handle all navigations and also passing of data between destinations.
* [Material Design](https://m3.material.io/) an adaptable system of guidelines, components, and tools that support the best practices of user interface design.
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) Coroutines help in managing background threads and reduces the need for callbacks.
* [Room](https://developer.android.com/topic/libraries/architecture/room) persistence library which provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) helps you load and display small chunks of data at a time.
* [Glide](https://github.com/bumptech/glide) - An image loading and caching library for Android focused on smooth scrolling.

<!-- <img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10"><img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10"><img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10"><img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10"><img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10">


<img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10"><img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10"><img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10"><img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10"><img src="https://github.com/mondal-souvik/git-two/assets/100204863/5331f848-11ee-4eee-99d5-41b77a715f1c" height="350" width="175" hspace="10"> -->











[Android Jetpack]: https://developer.android.com/jetpack
[NewsAPI]: https://newsapi.org/
