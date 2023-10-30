# Newsant News App 📰
An Android news application implemented using the MVVM pattern, Retrofit2, Paging3, Dagger-Hilt, Flow, ViewModel, Coroutines, Room, Navigation Components, View Binding and some other libraries from the [Android Jetpack] . Newsant app fetches data from the [NewsAPI] .


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
* [Flow](https://developer.android.com/kotlin/flow) for handling asynchronous data streams.
* [Navigation Component](https://developer.android.com/guide/navigation) to handle all navigations and also passing of data between destinations.
* [Material Design](https://m3.material.io/) an adaptable system of guidelines, components, and tools that support the best practices of user interface design.
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) Coroutines help in managing background threads and reduces the need for callbacks.
* [Room](https://developer.android.com/topic/libraries/architecture/room) persistence library which provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) helps you load and display small chunks of data at a time.
* [Coil](https://coil-kt.github.io/coil/) - An image loading library for Android backed by Kotlin Coroutines.

## App Icon 📱
<img src="https://github.com/mondal-souvik/git/assets/100204863/55c7cccb-0c86-4812-b552-99e374e8a882" width="80px" hspace="40">

## Some Screenshots

**Dark Theme :**

<img src="https://github.com/mondal-souvik/git/assets/100204863/6e5da5cf-087e-4ef6-acaf-bcb8753a7bb3" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git/assets/100204863/74bc929b-0984-41c0-a171-7f6699602158"  width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git/assets/100204863/f53b49e7-532c-4f46-8c97-68fab59c1ba4"  width="220" hspace="10">
<br/>
<br/>
<img src="https://github.com/mondal-souvik/git/assets/100204863/241f3e5f-cc2a-4c69-b760-bc0ae994568e"  width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git/assets/100204863/b24d8860-da7b-4b8f-acd8-b02c7865be42"  width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git/assets/100204863/0d65a227-0fda-422e-a75b-b34d7f6d380f"  width="220" hspace="10">

<br/>
<br/>
<br/>

**Light Theme :**

<img src="https://github.com/mondal-souvik/git/assets/100204863/ec5066be-a537-4c7e-bbcd-6786c51c1334" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git/assets/100204863/ceb3790d-9af6-4451-a2de-7df0a62632b3" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git/assets/100204863/c9a60098-238c-47f8-8583-28671a4ffded" height="470" width="220" hspace="5">
<br/>
<br/>
<img src="https://github.com/mondal-souvik/git/assets/100204863/be311c0c-dd16-454b-b457-60067980ca0a" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git/assets/100204863/c036db90-4d80-4a21-adf4-b379275a3989" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git/assets/100204863/49cd24da-976f-486a-80e7-55b8202a90a9" width="220" hspace="10">

## Download APK
<img src="https://github.com/mondal-souvik/git/assets/100204863/af8803b4-2434-4678-8fbd-5cf4b32dcd31" width="110px" hspace="10">

[Android Jetpack]: https://developer.android.com/jetpack
[NewsAPI]: https://newsapi.org/
