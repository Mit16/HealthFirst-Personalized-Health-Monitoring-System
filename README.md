# HealthFirst – Personalized Health Monitoring System

A monorepo for a full-stack health monitoring solution integrating Wear OS, a Spring Boot backend, and a web dashboard. The system enables real-time vitals tracking, AI-based risk assessment, and secure data sync using encrypted communication and offline queuing.

## 🗂️ Project Structure

health-monitoring-system/ <br>
├── frontend/ # Web dashboard for users and doctors <br>
├── backend/ # Spring Boot microservices handling auth, health data, AI, and alerts <br>
└── wearos/ # Wear OS app for live health tracking and secure pairing


---

## ✅ Key Features

- 📡 **Live vitals tracking** from Wear OS (Heart Rate, SpO₂, Temperature, Steps, Sleep)
- 🔐 **Secure pairing** between watch and user via token-based authentication
- 🧠 **AI-based risk prediction** service using health metrics
- 📬 **Email alert system** for high-risk conditions
- 📦 **Offline support** with encrypted queueing and automatic retry
- 🔄 **Time-series aware** health data storage structure
- 🧩 **Modular microservices architecture**

---

## 🧰 Tech Stack

| Layer       | Technology                                              |
|-------------|----------------------------------------------------------|
| **Wear OS** | Kotlin, Android SDK, Retrofit, EncryptedSharedPreferences |
| **Backend** | Spring Boot, PostgreSQL, JPA, MQTT, REST APIs, JWT       |
| **AI/Alerts** | Internal risk engine & mail alert microservice         |
| **Frontend** | React (In Progress)                                     |
| **Infra**    | Microservices, Token Auth, Time-series DB Design        |

---

## ⚙️ Monorepo Setup Using Git Subtree

To preserve commit history from separate Git repos:

```bash
# Step 1: Initialize empty monorepo
mkdir health-monitoring-system && cd health-monitoring-system
git init
git commit --allow-empty -m "Initial empty commit"

# Step 2: Add subprojects as subtrees
git remote add frontend-origin /path/to/frontend
git fetch frontend-origin
git subtree add --prefix=frontend frontend-origin main --squash

git remote add backend-origin /path/to/backend
git fetch backend-origin
git subtree add --prefix=backend backend-origin main --squash

git remote add wearos-origin /path/to/wearos
git fetch wearos-origin
git subtree add --prefix=wearos wearos-origin main --squash

# Step 3: Push to GitHub
git remote add origin https://github.com/yourusername/health-monitoring-system.git
git push -u origin main

```
Replace master with main if needed, and paths with local repo paths.

---

## 🔐 Security  
All personal health data is encrypted on-device.  
Devices are paired using token-based authentication.  
Communication and syncing follow secure protocols.  
Offline-collected data is encrypted and queued for safe transmission once online.

---

## 📍 Project Status

| Component  | Status                                                |
|------------|--------------------------------------------------------|
| Backend    | ✅ Complete with auth, AI, alerts, health services     |
| Wear OS    | ✅ Real-time tracking, background service, offline sync |
| Frontend   | 🛠️ In Progress (React-based web dashboard)            |

---

## 🤝 Contributing  
Feel free to fork, contribute, or raise issues. All constructive feedback is welcome!

## 📄 License  
MIT License (or replace with your preferred license)

