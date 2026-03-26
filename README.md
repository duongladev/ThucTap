# 🚀 ThucTap - Task Management System

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?logo=java">
  <img src="https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen?logo=springboot">
  <img src="https://img.shields.io/badge/MySQL-Database-blue?logo=mysql">
  <img src="https://img.shields.io/badge/JWT-Auth-red">
  <img src="https://img.shields.io/badge/Maven-Build-purple?logo=apachemaven">
</p>

<p align="center">
  <b>Hệ thống quản lý công việc sử dụng Spring Boot + JWT Authentication</b>
</p>

---

## 📸 Preview

---

## 📌 Giới thiệu

**ThucTap** là hệ thống quản lý công việc (Task Management System) cho phép:

* Quản lý người dùng 👤
* Quản lý công việc 📋
* Phân quyền 🔐
* Xác thực bằng JWT

Phù hợp cho:

* Học tập Spring Boot
* Demo đồ án
* Portfolio cá nhân

---

## 🏗️ Kiến trúc hệ thống

```
Client
   ↓
Controller
   ↓
Service
   ↓
Repository (JPA)
   ↓
Database (MySQL)

+ JWT Security Filter
```

---

## 🔐 JWT Authentication Flow

```
User → Login → Server
        ↓
   Generate JWT
        ↓
   Client lưu token
        ↓
Request API → Bearer Token
        ↓
   JWT Filter validate
        ↓
   Response
```

---

## ⚙️ Cài đặt nhanh

### 1️⃣ Clone project

```bash
git clone git@github.com:duongladev/ThucTap.git
cd ThucTap
```

---

### 2️⃣ Setup Database

```sql
CREATE DATABASE thuctap_db;
```

Cập nhật file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/thuctap_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

---

### 3️⃣ Run project

```bash
mvn spring-boot:run
```

📍 Server chạy tại:

```
http://localhost:8080
```

---

## 📡 API Overview

### 🔑 Authentication

| Method | Endpoint         | Description |
| ------ | ---------------- | ----------- |
| POST   | `/auth/register` | Register    |
| POST   | `/auth/login`    | Login       |

---

### 📋 Task

| Method | Endpoint      | Description   |
| ------ | ------------- | ------------- |
| GET    | `/tasks`      | Get all tasks |
| POST   | `/tasks`      | Create task   |
| PUT    | `/tasks/{id}` | Update task   |
| DELETE | `/tasks/{id}` | Delete task   |

---

### 👤 User

| Method | Endpoint | Description |
| ------ | -------- | ----------- |
| GET    | `/users` | Get users   |

---

## 🧪 Demo Checklist

* [x] Register user
* [x] Login → nhận token
* [x] Gọi API với token
* [x] CRUD Task
* [x] Phân quyền

---

## 📁 Project Structure

```
src/main/java/org/example/tuan3
│
├── controller
├── service
├── repository
├── entity
├── dto
├── security
└── config
```

---

## 🌟 Tính năng nổi bật

✅ JWT Authentication
✅ Role-based Authorization
✅ RESTful API chuẩn
✅ Clean Architecture (Controller → Service → Repo)
✅ Dễ mở rộng

---

## 🚀 Hướng phát triển

* [ ] Refresh Token
* [ ] Pagination
* [ ] Upload file
* [ ] Frontend (Vue / React)
* [ ] Docker

---

## 👨‍💻 Author

**Đinh Trọng Dương**

---

