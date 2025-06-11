# 📘 Hotel View API

## 🛠 Описание

Приложение — RESTful API для управления отелями. Реализована полная функциональность по техническому заданию: создание, получение, поиск, добавление удобств и построение гистограмм.

---

## 🚀 Как запустить проект

1. **Склонировать репозиторий:**

   ```bash
   [git clone https://github.com/timofeibratskov/hotel-view.git](https://github.com/timofeibratskov/hotel-view.git)
   cd hotel-view
   ```
2. **Запустить приложение:**
      ```bash
   mvn spring-boot:run
   ```
      
   ---
   
## 🔗 Swagger-документация     
****[http://localhost:8092/swagger-ui/index.html](http://localhost:8092/swagger-ui/index.html)****

---

## 🧩 Переключение между базами данных (H2 ↔ PostgreSQL)

По умолчанию приложение использует **встроенную H2-базу данных**.  
Если вы хотите использовать **PostgreSQL**, предусмотрена поддержка Spring-профилей.

**Активировать профиль PostgreSQL:**

```powershell
[System.Environment]::SetEnvironmentVariable("spring.profiles.active", "postgres", "Process")
mvn spring-boot:run
```

**вернуться на H2:**

```powershell
Remove-Item -Path Env:spring.profiles.active
mvn spring-boot:run
```
