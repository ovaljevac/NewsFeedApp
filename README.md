# 📰 NewsFeedApp

**NewsFeedApp** is a moderately complex Android application built with **Jetpack Compose**, offering a smooth and modern news browsing experience. It fetches news from multiple categories via **TheNewsAPI**, and uses **Imagga API** to extract image tags for additional context in the news detail screen.

---

## ✨ Features

- 📚 **Categorized News Feed**  
  Browse top stories by selecting from a variety of news categories.

- 🔍 **News Details Screen**  
  Each article view includes:
  - Full content
  - Publication info
  - Image tags powered by **Imagga API**

- 🏷️ **Tagging with Imagga**  
  Automatically fetches relevant tags from the article image to enrich the user experience.

- 💾 **Offline Access with Room DB**  
  All fetched articles are stored in a local Room database, allowing offline access to previously viewed content.

- 🧰 **Filter Screen**  
  Filter news by:
  - Custom **date range**
  - **Banned words** (exclude certain terms from the feed)

---

## 🧱 Built With

- 🧩 **Jetpack Compose** — declarative UI toolkit
- 🌐 **Retrofit** — for API communication
- 🖼️ **Imagga API** — for image tagging
- 📰 **TheNewsAPI** — for fetching categorized news
- 🗃️ **Room** — for local database storage
- 🧪 **ViewModel & StateFlow** — for reactive state management
- 🧭 **Navigation Compose** — for screen transitions

---

## 🧪 Screens

- `NewsFeedScreen` – shows latest news by category
- `NewsDetailScreen` – shows full article and image tags
- `FilterScreen` – filter by date range and banned keywords

---

## 📦 Requirements

- Android Studio Giraffe or newer
- Android device or emulator with API 24+

---

## 🧠 Architecture

The app uses MVVM architecture and clean separation of concerns between UI, data, and domain layers. Repository pattern is used to unify local and remote data sources.

---

## 🔐 API Keys

You will need API keys for:
- [TheNewsAPI](https://www.thenewsapi.com/)
- [Imagga API](https://imagga.com/)

Store your keys securely (e.g., using `local.properties` or environment variables) and inject them via build config.

---

## 📄 License

MIT License — feel free to use, modify, and contribute.

