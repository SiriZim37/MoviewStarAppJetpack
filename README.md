# 🎬 TMDB Movies App

![App Demo](./demo/demo.gif)

---

## 🚀 Overview

This app displays upcoming movies from the TMDB API using modern Android technologies.  
Designed to load data efficiently with pagination and cache it for offline use.

Technologies used:  
- **Jetpack Compose** — Modern, declarative UI toolkit  
- **Kotlin Coroutines** — For smooth background tasks without blocking UI  
- **Paging 3** — Efficiently load large lists by pagination  
- **Room** — Local SQLite database for caching data  
- **Hilt** — Dependency Injection for easier object management  
- **DataStore** — Efficient and safe data storage for preferences  
- **Multi-module Architecture** — Clean separation of concerns and scalable development  

---

## 🗂️ Module Structure

| Module              | Description                                  |
|---------------------|----------------------------------------------|
| `app`               | Main module, handles UI and navigation       |
| `feature:upcoming`  | Feature module to display upcoming movies    |
| `core:data`         | Data sources including API, database, cache  |
| `core:database`     | Room database setup and DAOs                  |
| `core:network`      | Retrofit and API service definitions          |
| `core:ui`           | Shared UI components, themes, custom widgets |

---

## ✨ Key Features

- Fetch upcoming movies from TMDB API with pagination  
- Splash screen with smooth transition  
- Offline caching of movie data using Room  
- Seamless Paging + Room integration via RemoteMediator  
- DataStore to store timestamps and manage refresh intervals  
- Modular architecture for easy maintenance and scalability  

---

## ⚙️ Getting Started

1. Clone this repository:
    ```bash
    git clone https://github.com/yourusername/tmdb-movies-app.git
    ```
2. Open the project in Android Studio  
3. Add your TMDB API key to the `local.properties` file:
    ```
    tmdb_api_key="YOUR_API_KEY_HERE"
    ```
4. Build and run the app on an emulator or physical device  

---

## 📸 Demo Screenshot

![App Screenshot](./demo/movie_star_demo.png)

---

## 🛠️ Technology Stack

- Kotlin  
- Jetpack Compose  
- Coroutines  
- Paging 3  
- Room  
- Retrofit2  
- Hilt  
- DataStore  

---

## 📄 License

MIT License © 2025 SiriZim37
