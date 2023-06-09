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

## App Icon 📱
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/07f6638a-902d-429a-b4ad-1fb6302530ba" width="80px" hspace="40">

## Some Screenshots

**Dark Theme :**

<img src="https://github.com/mondal-souvik/git-two/assets/100204863/6248beb1-9e22-4b7c-9e44-a8d94895d72f" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/48c6f1fb-2caf-4142-b634-dfea584b03e3" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/f4a90542-187a-4a07-8a91-d0e531ce4fc6" height="470" width="220" hspace="10">
<br/>
<br/>
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/0c8fa1be-5520-4938-8491-68ddba6f0aa3" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/67d2c8d7-0a86-462c-9059-e245cfb8ae3c" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/f230ae36-923b-433b-8260-8b31f2ecbc38" height="470" width="220" hspace="10">

<br/>
<br/>
<br/>

**Light Theme :**

<img src="https://github.com/mondal-souvik/git-two/assets/100204863/ee411289-cd76-4548-a17b-ac2a9ece210f" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/b73bd36d-11e8-40ef-95af-48c0be339457" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/823ad3da-d453-4e61-ab7a-018b2e109ff2" height="470" width="220" hspace="5">
<br/>
<br/>
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/810d6a40-cc01-489b-80f2-ae10885ad449" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/42453298-c636-401c-948b-63df316c8630" height="470" width="220" hspace="10">
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/8ba67238-5b56-4597-aa10-fb2fa1526b30" height="470" width="220" hspace="10">

## Download APK
<img src="https://github.com/mondal-souvik/git-two/assets/100204863/47ea6bb6-0431-4d69-829b-112b1b7b4f54" width="110px" hspace="10">

[Android Jetpack]: https://developer.android.com/jetpack
[NewsAPI]: https://newsapi.org/
