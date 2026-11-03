# Business Plan Generator 🚀
> **Turn Your Business Idea Into a Winning Plan.**

An institutional-grade, AI-powered full-stack SaaS platform that transforms startup concepts and business ideas into comprehensive 30-section investor-ready business plans, complete 5-year financial projections (computed via Java `BigDecimal`), and actionable growth roadmaps.

[![Java 21](https://img.shields.io/badge/Java-21-orange.svg?logo=openjdk)](https://adoptium.net/)
[![Spring Boot 3.2.5](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind-CSS-38B2AC.svg?logo=tailwind-css)](https://tailwindcss.com/)
[![Google Gemini API](https://img.shields.io/badge/AI-Google%20Gemini-4285F4.svg?logo=google)](https://ai.google.dev/)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-336791.svg?logo=postgresql)](https://www.postgresql.org/)
[![Vercel Ready](https://img.shields.io/badge/Deploy-Vercel-black.svg?logo=vercel)](https://vercel.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 🌟 Key Highlights & Capabilities

- 🤖 **30-Section Comprehensive Generation**: Covers Executive Summary, TAM/SAM/SOM, Go-To-Market, SWOT Analysis, Unit Economics, Operations, and Implementation Milestones.
- 🧮 **Institutional Java `BigDecimal` Precision**: No floating-point or LLM calculation hallucination errors. Calculates fixed vs. variable costs, Break-Even unit horizons, cash runway, and 5-year EBITDA/CAGR schedules.
- 📊 **Interactive Studio & Document Editor**: 3-column workspace with a table of contents navigator, live markdown preview, and an inline AI Copilot to rewrite, expand, or adjust tone.
- 📑 **Investor-Grade Export (PDF & DOCX)**: Generates formatted executive reports with automatic table of contents, cover pages, and clean typographic tables powered by OpenPDF and Apache POI.
- 📈 **3-Scenario Sensitivity Analysis**: One-click toggling between Conservative (80%), Base (100%), and Aggressive (140%) market conditions.
- 💳 **Razorpay Payment Gateway**: Seamless subscription tiers (Free Starter, Pro Founder, Venture Agency) with secure order validation and webhook handling.
- 📁 **14 Turnkey Industry Templates**: Pre-configured assumptions for SaaS, E-Commerce, MedTech, FinTech, GreenTech, Hospitality, and Consulting.
- 🛡️ **SBA Loan & Bank Compliant**: Structured specifically to fulfill underwriting checklists for Small Business Administration (SBA) 7(a) and commercial bank loans.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Frontend** | React, TypeScript, Tailwind CSS, Lucide Icons, Recharts, Inter Typography |
| **Backend** | Java 21, Spring Boot 3.2.5, Spring Security, Spring Data JPA, Jakarta Bean Validation |
| **Database** | PostgreSQL, Flyway Database Migrations, H2 (In-Memory for rapid dev) |
| **AI Integration** | Google Gemini API via Spring `WebClient`, JSON structured DTO validation, Autonomous Fallback Engine |
| **Document Export** | OpenPDF (PDF Generation), Apache POI (Word `.docx` Generation) |
| **Payments** | Razorpay SDK (Order creation, signature verification, webhook validation) |
| **DevOps & Containers** | Docker (Multi-stage build), Docker Compose, Vercel (`vercel.json`) |

---

## 📂 Project Architecture

```
Business-Plan-Generator/
├── backend/
│   ├── src/main/java/com/bpg/
│   │   ├── ai/              # Gemini AI Provider, AI Service, Autonomous Demo Generator
│   │   ├── config/          # Spring Security, CORS, Demo Data Seeder
│   │   ├── controller/      # Auth, Plans, Financials, Payments, Admin REST APIs
│   │   ├── dto/             # Request & Response DTO records
│   │   ├── entity/          # JPA Entities (User, BusinessPlan, Subscriptions, Payments)
│   │   ├── export/          # OpenPDF and Apache POI export engines
│   │   ├── financial/       # Institutional BigDecimal calculation engine
│   │   ├── repository/      # Spring Data JPA repositories
│   │   ├── security/        # JWT Filter, Token Utility, UserDetailsService
│   │   └── service/         # Business logic & transaction orchestration
│   ├── src/main/resources/
│   │   ├── application.yml  # Database & Spring configurations
│   │   └── db/migration/    # Flyway V1 SQL schema
│   ├── Dockerfile           # Multi-stage production container build
│   └── pom.xml              # Maven dependencies & build configuration
├── index.html               # Full commercial SaaS website, Wizard & Studio Editor
├── vercel.json              # Vercel zero-config static routing
├── docker-compose.yml       # Orchestrated PostgreSQL + Spring Boot backend
├── .env.example             # Complete environment variables template
└── README.md
```

---

## 🚀 Quickstart Guide

### Prerequisites
- **JDK 21** or higher
- **Maven 3.9+**
- **Node.js 18+** (for frontend static serving or local development)

### 1. Clone the Repository
```bash
git clone https://github.com/hemanthkumarrrdy-bit/Business-Plan-Generator.git
cd Business-Plan-Generator
```

### 2. Run the Java Backend
```bash
cd backend
mvn clean test
mvn spring-boot:run
```
*The Spring Boot API will start on `http://localhost:8080` with pre-seeded demo accounts and H2/PostgreSQL database support.*

### 3. Run the Frontend Website
You can serve the application with any static web server:
```bash
# Using npx http-server
npx http-server . -p 3000 --cors
```
*Open [http://localhost:3000](http://localhost:3000) in your browser.*

---

## 🐳 Docker Orchestration

To run the entire full-stack application (PostgreSQL + Spring Boot Backend) with Docker Compose:

```bash
docker-compose up --build -d
```

---

## ⚙️ Environment Variables

Copy `.env.example` to `.env` and configure your credentials:

```ini
# Server Port & Active Profile
PORT=8080
SPRING_PROFILES_ACTIVE=prod

# PostgreSQL Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/bpg_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# JWT Secret (Min 256 bits)
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION_MS=86400000

# Google Gemini API
GEMINI_API_KEY=your_gemini_api_key_here
GEMINI_MODEL=gemini-1.5-flash

# Razorpay Payment Gateway
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret
RAZORPAY_WEBHOOK_SECRET=your_razorpay_webhook_secret
```

---

## 🌐 Public Deployment Guide

### Deploy Frontend to Vercel (1-Click)
1. Fork or push this repository to your GitHub account.
2. Go to [Vercel Dashboard](https://vercel.com/new).
3. Import `Business-Plan-Generator`.
4. The included [`vercel.json`](vercel.json) handles static routing automatically. Click **Deploy**.

### Deploy Backend to Render or Railway
1. Go to [Render Dashboard](https://dashboard.render.com/) or [Railway](https://railway.app/).
2. Create a new **Web Service** pointing to this repository.
3. Select **Docker** environment (it automatically detects [`backend/Dockerfile`](backend/Dockerfile)).
4. Attach a managed PostgreSQL database and set your environment variables.

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
