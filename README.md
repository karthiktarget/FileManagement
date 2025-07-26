# 📁 FileManagement - Spring Boot File Storage API

A simple RESTful backend service for uploading, downloading, listing, and deleting files using **Spring Boot**, **PostgreSQL**, and **local file system** storage.

---

## 🚀 Features

- ✅ Upload file (`multipart/form-data`)
- 📜 View uploaded file metadata
- 📥 Download file by ID
- 🗑️ Delete file
- 📦 Docker support
- 🧩 PostgreSQL integration

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3
- Spring Web & Spring Data JPA
- PostgreSQL
- Docker & Docker Compose

---

## ⚙️ Getting Started

### 1. Clone the repo
```bash
git clone https://github.com/karthiktarget/FileManagement.git
cd FileManagement
```
2. Set up PostgreSQL
Use Docker (or install manually):
```
docker run --name pg-filestore -e POSTGRES_DB=filestorage -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=yourpassword -p 5432:5432 -d postgres
```

3. Configure application.properties
```
spring.datasource.url=jdbc:postgresql://localhost:5432/filestorage
spring.datasource.username=postgres
spring.datasource.password=yourpassword
file.storage.location=uploads
spring.jpa.hibernate.ddl-auto=update
```
4. Run the app
```
./gradlew bootRun
```
