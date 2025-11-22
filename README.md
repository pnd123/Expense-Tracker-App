# Expenzo – Personal Expense Tracker App (Android + Spring Boot)

**Expenzo** is a full-featured, privacy-focused **daily expense tracking app** built using **Java (Android)** and a **Spring Boot backend**. It helps users seamlessly manage their finances with offline capabilities, insightful charts, and secure data sync.
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

---

## 🛡 Security & Architecture

- No sensitive files committed (e.g., `.keystore`, `local.properties`, `.env`)
- `.gitignore` configured at root, backend, and frontend levels
- Supports local offline usage and secure API-based backend sync
- Backend uses `@Temporal` and strict date validation to avoid format conflicts

### 🔗 Backend (Spring Boot + MySQL)

The backend is a fully functional **Java Spring Boot application** that handles all expense operations and integrates directly with **MySQL** using Spring Data JPA + Hibernate.

#### ✔ Backend Features

* Secure REST API for CRUD operations
* Connected to **MySQL database** using JDBC
* Hibernate auto-generates tables via JPA entities
* JSON responses using Jackson (`@JsonFormat` for dates)
* Follows **Controller → Service → Repository → Model** structure
* Used by the Android app via Retrofit

---

## 🗄️ **MySQL Database Integration (Hands-On SQL Work)**

This project demonstrates real SQL usage through:

* Creating MySQL database (`expense_tracker`)
* Creating and managing tables using JPA + SQL
* Running SQL queries through both MySQL Workbench and Spring Boot
* Viewing SQL logs (`spring.jpa.show-sql=true`)

### **application.properties**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=root
spring.datasource.password=****
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```
---

## 📂 Backend Folder Structure (Java + SQL Proof)

```
src/main/java/com/example/expensetracker/
│
├── model/
│   └── Expense.java     → Maps to MySQL table
│
├── repository/
│   └── ExpenseRepository.java  → SQL operations via JPA
│
└── controller/
    └── ExpenseController.java  → REST API

```
### ▶ Run the Backend

```bash
cd backend/
./mvnw spring-boot:run
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
