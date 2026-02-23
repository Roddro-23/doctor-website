# 🏥 HealthCare Clinic — Full Stack Doctor Website

A professional, fully-featured doctor/clinic website built with **Spring Boot** (REST API backend) and **HTML/CSS/JS** (frontend), backed by **MySQL** and deployable on **Render**.

---

## 🚀 Features

| Feature | Description |
|---------|-------------|
| 🏠 Home Page | Hero, services preview, doctor intro, testimonials, CTA |
| 👨‍⚕️ About Page | Doctor profile, education timeline, awards, clinic timing |
| 🩺 Services Page | 8 medical services loaded from API with fallback |
| 📅 Appointment | Online booking form with validation |
| 📞 Contact | Map embed, contact form, emergency banner |
| 🔐 Admin Dashboard | Password-protected; view/update/delete appointments |

---

## 📁 Project Structure

```
doctor-website/
├── backend/                          # Spring Boot application
│   ├── src/main/java/com/doctorwebsite/
│   │   ├── config/           CorsConfig.java, DataSeeder.java
│   │   ├── controller/       AppointmentController, DoctorController, ServiceController, ContactController
│   │   ├── dto/              AppointmentDTO, DoctorDTO, ContactMessageDTO, ApiResponse
│   │   ├── entity/           User, Doctor, MedicalService, Appointment
│   │   ├── exception/        GlobalExceptionHandler, ResourceNotFoundException
│   │   ├── repository/       AppointmentRepository, DoctorRepository, ServiceRepository
│   │   └── service/          AppointmentService, DoctorService
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── index.html            Home page
│   ├── about.html            About doctor page
│   ├── services.html         Services page
│   ├── appointment.html      Booking form
│   ├── contact.html          Contact + map
│   ├── admin.html            Admin dashboard
│   ├── css/style.css
│   └── js/
│       ├── main.js           Shared utilities
│       ├── appointment.js    Booking form logic
│       ├── contact.js        Contact form logic
│       └── admin.js          Admin dashboard logic
├── render.yaml               Render deployment config
└── README.md
```

---

## 🛠️ Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Node.js (optional — for serving frontend locally)

---

## ⚙️ Local Setup

### 1. Create MySQL Database

```sql
CREATE DATABASE doctordb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configure Database

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/doctordb?useSSL=false&serverTimezone=UTC
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
```

### 3. Run the Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The API starts at: **http://localhost:8080**

On first run, the `DataSeeder` automatically populates:
- Doctor profile (Dr. Sarah Johnson)
- 8 medical services (Cardiology, Medicine, Surgery, etc.)

### 4. Open the Frontend

Open `frontend/index.html` directly in your browser **or** serve it with:

```bash
# Using Python (no install needed)
cd frontend
python3 -m http.server 3000
# Open: http://localhost:3000
```

> **Note:** The frontend JS auto-detects `localhost` and points API calls to `http://localhost:8080`. No configuration needed.

---

## 🔌 API Endpoints

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/doctors` | Public | Get all doctors |
| GET | `/api/doctors/{id}` | Public | Get doctor by ID |
| GET | `/api/services` | Public | Get all services |
| POST | `/api/appointments` | Public | Book appointment |
| GET | `/api/appointments?adminPassword=xxx` | Admin | Get all appointments |
| PUT | `/api/appointments/{id}/status?adminPassword=xxx` | Admin | Update status |
| DELETE | `/api/appointments/{id}?adminPassword=xxx` | Admin | Delete appointment |
| POST | `/api/contact` | Public | Send contact message |

### Book Appointment (Example)

```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "patientName": "Rahim Uddin",
    "phone": "01700000000",
    "patientEmail": "rahim@example.com",
    "appointmentDatetime": "2026-03-10T10:00:00",
    "reason": "Regular checkup"
  }'
```

---

## 🔒 Admin Dashboard

- URL: `frontend/admin.html`
- Default password: **`admin123`**
- Change via env var: `ADMIN_PASSWORD=yourpassword`

---

## 🌐 Deployment on Render

### Step 1 — Create a MySQL Database

Use **PlanetScale** (free tier), **Render MySQL** add-on, or **Aiven** to get a hosted MySQL URL.

### Step 2 — Push to GitHub

```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/doctor-website.git
git push -u origin main
```

### Step 3 — Deploy on Render

1. Go to [render.com](https://render.com) → **New** → **Blueprint**
2. Connect your GitHub repository
3. Render reads `render.yaml` and creates:
   - **`doctor-website-api`** — Spring Boot backend (Docker)
   - **`doctor-website-frontend`** — Static HTML site

### Step 4 — Set Environment Variables (Backend)

In Render Dashboard → `doctor-website-api` → **Environment**:

| Key | Value |
|-----|-------|
| `DB_URL` | `jdbc:mysql://your-host:3306/doctordb?useSSL=true&serverTimezone=UTC` |
| `DB_USER` | your MySQL username |
| `DB_PASS` | your MySQL password |
| `ADMIN_PASSWORD` | your admin password |

### Step 5 — Update Frontend API URL

Edit `frontend/js/main.js` line 6:
```js
: 'https://doctor-website-api.onrender.com' // ← Replace with your actual Render backend URL
```

Then redeploy or push to trigger a new deployment.

---

## 🗄️ Database Tables

Tables are auto-created by JPA (`spring.jpa.hibernate.ddl-auto=update`):

| Table | Description |
|-------|-------------|
| `users` | Admin/patient accounts |
| `doctors` | Doctor profiles |
| `services` | Medical service catalog |
| `appointments` | Patient booking records |

---

## 🧪 Testing

```bash
# Test backend health
curl http://localhost:8080/api/services

# Test appointment booking
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{"patientName":"Test Patient","phone":"01711111111","appointmentDatetime":"2026-12-01T10:00:00","reason":"Checkup"}'

# Test admin (list appointments)
curl "http://localhost:8080/api/appointments?adminPassword=admin123"

# Build JAR
cd backend && mvn clean package -DskipTests
```

---

## 🎨 Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2, JPA, Validation |
| Database | MySQL 8 |
| Frontend | HTML5, CSS3, Vanilla JS |
| Fonts | Inter (Google Fonts) |
| Icons | Font Awesome 6.5 |
| Deployment | Render (Docker + Static) |

---

## 📞 Default Configuration

- **Doctor:** Dr. Sarah Johnson
- **Clinic:** HealthCare Clinic
- **Fee:** BDT 800
- **Admin Password:** `admin123` (change via `ADMIN_PASSWORD` env var)
- **Hours:** Mon–Fri 9AM–6PM | Sat 10AM–2PM

---

## 📄 License

MIT License — free to use and modify.
