# Smart Payment Router

A production-ready Smart Payment Router built using Spring Boot, React, PostgreSQL, Redis, Kafka, Docker, Render, and Vercel.

## Live Demo

Frontend:
https://smart-payment-router.vercel.app/

Backend API:
https://smart-payment-router-1.onrender.com/actuator

GitHub:
https://github.com/Bhumi-singh/smart-payment-router

---

## Project Overview

Smart Payment Router simulates a real-world payment orchestration system that intelligently routes transactions through multiple payment gateways, tracks payment statuses, maintains dashboard analytics, and handles failed transactions using Dead Letter Queue (DLQ) mechanisms.

The system provides:

- Payment creation and processing
- Payment status tracking
- Dashboard analytics
- PostgreSQL persistence
- Redis caching
- Kafka event-driven architecture
- DLQ monitoring
- Cloud deployment

---

## Features

### Payment Processing
- Create new payments
- Track payment status
- Simulate multiple payment gateways
- Store payment history

### Dashboard Analytics
- Total payments
- Successful payments
- Failed payments
- Pending payments
- Recent transactions

### Reliability
- Dead Letter Queue (DLQ)
- Retry handling
- Event-driven processing

### Performance
- Redis caching
- Database optimization
- RESTful APIs

### Cloud Deployment
- Backend deployed on Render
- Frontend deployed on Vercel
- PostgreSQL hosted on Render

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 3
- Spring Data JPA
- Spring Web
- PostgreSQL
- Redis
- Apache Kafka
- Maven

### Frontend
- React.js
- Vite
- Axios
- CSS

### DevOps & Deployment
- Docker
- Docker Compose
- GitHub
- Render
- Vercel

---

## Architecture

Frontend (React)
↓
Spring Boot REST APIs
↓
Payment Router Service
↓
Kafka Event Stream
↓
Payment Consumer
↓
PostgreSQL Database

Additional Components:

- Redis Cache
- Dead Letter Queue (DLQ)
- Dashboard Analytics

---

## Project Structure

```text
paymentrouter
│
├── frontend
│   ├── src
│   ├── public
│   ├── package.json
│   └── vite.config.js
│
├── src
│   └── main
│       ├── java
│       │   └── com.bhumi.paymentrouter
│       │       ├── controller
│       │       ├── service
│       │       ├── repository
│       │       ├── entity
│       │       ├── kafka
│       │       ├── gateway
│       │       ├── dto
│       │       └── config
│       │
│       └── resources
│           └── application.properties
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## API Endpoints

### Create Payment

```http
POST /payments
```

Request:

```json
{
  "orderId": "ORD123",
  "amount": 5000
}
```

---

### Get Dashboard Statistics

```http
GET /dashboard
```

---

### Recent Transactions

```http
GET /dashboard/recent
```

---

### Cache Status

```http
GET /dashboard/cache
```

---

### DLQ Count

```http
GET /dashboard/dlq-count
```

---

## Local Setup

### Clone Repository

```bash
git clone https://github.com/YOUR-USERNAME/smart-payment-router.git
cd smart-payment-router
```

### Backend

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Runs on:

```text
http://localhost:8080
```

### Frontend

```bash
cd frontend

npm install

npm run dev
```

Runs on:

```text
http://localhost:5173
```

---

## Deployment

### Backend Deployment
- Render
- PostgreSQL Database
- Environment Variables

### Frontend Deployment
- Vercel
- Connected to Render Backend APIs

---

## Future Enhancements

- JWT Authentication
- Payment Gateway Integration (Razorpay/Stripe)
- Advanced Analytics Dashboard
- Kubernetes Deployment
- Microservices Architecture
- CI/CD Pipeline using GitHub Actions
- Prometheus & Grafana Monitoring

---

## Key Learnings

- Spring Boot Application Development
- REST API Design
- PostgreSQL Integration
- Redis Caching
- Kafka Event Streaming
- Docker Containerization
- Cloud Deployment
- CORS Handling
- Full Stack Application Development

---
