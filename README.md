# HourChangesColor
[![Build Status](https://img.shields.io/github/actions/workflow/status/Br4hms-d3v/HourChangesColor/ci.yml?style=for-the-badge)](
https://github.com/Br4hms-d3v/HourChangesColor/actions)

A Spring Boot app that changes the theme with the time (AM/PM).  
The page updates in real time with SSE.

---

## 🚀 Features

- 🎨 Theme changes with AM / PM
- 🕒 Live time display
- 🔄 Auto update without page reload (SSE)
- 🗄️ Data saved in H2 database
- 🧼 Code formatting with Spotless
- 🔁 Spring Batch runs every hour
- 🚀 CI/CD with GitHub Actions (build + deploy with SSH, no Docker)
- 📄 Simple Mustache front-end

---

## ⚙️ Technologies

- Java 25
- Spring Boot 4
- Spring Batch
- Spring Web
- Spring Data JPA
- H2 Database
- Mustache
- SSE (Server-Sent Events)
- Gradle
- Spotless
- GitHub Actions  

---

## 🔄 Batch Process

The batch runs every hour:

- Check current time
- Check AM or PM
- Update database with:
    - background color
    - text color
    - message (Good day / Good evening)
- Send SSE event to update the page  

---

## 🖥️ UI (Mustache)

The page shows:

live time
message (Good day / Good evening)
dynamic theme

---

## 🗄️ H2 DATABASE
{localhost}/h2-console

---

## 🚀 Run the Project

### 1. Clone the repo:
    git clone {{ this repo }} and go to {{ cd HourChangeColor }} the directory 
### 2. Run app
    ./gradlew bootRun
### 3. Open in browser
    http://localhost:8080