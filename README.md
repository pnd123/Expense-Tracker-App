# Expenzo – Personal Expense Tracker App (Android + Spring Boot)

**Expenzo** is a full-featured, privacy-focused **daily expense tracking app** built using **Java (Android)** and a **Spring Boot backend**. It helps users seamlessly manage their finances with offline capabilities, insightful charts, and secure data sync.

> Built with simplicity and performance in mind — perfect for college students, individuals, or anyone who wants clear control over spending.

---

## ✨ Features

### 📱 Android App (Frontend)
- Add, edit, delete expenses with category, amount, notes, and date
- Visualize monthly spending via **interactive Pie Charts** (MPAndroidChart)
- Track balances with **real-time budget calculations**
- View **expenses grouped by date/month/category**
- Light/Dark Theme support
- Offline mode using **SQLite** (auto syncs when online)

### 🔗 Backend (Spring Boot + MySQL)
- Secure REST API endpoints for all expense operations
- Uses **MySQL** for persistent storage
- Supports **Retrofit** integration with Android app
- Parses JSON with correct date handling (`yyyy-MM-dd`)
- Designed with clean, scalable architecture (MVC + Repository Pattern)

---

## 🧰 Tech Stack

### Android (Frontend)
- **Java** & **XML**
- **SQLite** (via custom DB helper)
- **MPAndroidChart** (PieChart)
- **Retrofit 2** for HTTP client
- **Gson** for JSON parsing with custom `Date` format
- **MVVM-style architecture** for cleaner UI & logic separation

### Spring Boot (Backend)
- **Spring Boot 3.5**
- **Spring Data JPA** + **Hibernate**
- **MySQL 8.0**
- **REST API** with JSON responses
- Date format handling with `@JsonFormat`
- `.gitignore` and `application.properties` protected for safe GitHub upload

---

## 🛡 Security & Architecture

- No sensitive files committed (e.g., `.keystore`, `local.properties`, `.env`)
- `.gitignore` configured at root, backend, and frontend levels
- Supports local offline usage and secure API-based backend sync
- Backend uses `@Temporal` and strict date validation to avoid format conflicts

---

## 📂 Folder Structure

```bash
Expenzo/
│
├── backend/             # Spring Boot backend (MySQL)
│   ├── src/
│   ├── pom.xml
│   └── application.properties (ignored)
│
├── app/                 # Android Studio project
│   ├── app/src/
│   └── build.gradle
│
├── .gitignore           # Root-level ignore file
└── README.md
```

---
🚀 Getting Started
Clone the Repo
```
git clone https://github.com/yourusername/Expenzo.git
```
---
📲 Run the Android App
Open the project in Android Studio

Let Gradle sync complete

Run on an emulator or physical device

Run the Backend

cd backend/
```
./mvnw spring-boot:run
```

---
## 📱 Home Screen

<h2 align="center">📱 Home Screen</h2>
<p align="center">
  <img src="docs/images/app.png" width="300" alt="Home Screen">
</p>

---

👤 Author
Prasann
🚀 Passionate Android + Java Developer
🔗 GitHub: https://github.com/pnd123
