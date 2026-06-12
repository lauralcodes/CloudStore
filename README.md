# ☁️ CloudStore – Spring Boot Cloud Capstone Project

CloudStore is my first-year Java capstone project built with Spring Boot, combining backend development, frontend integration, and full CI/CD cloud deployment.

The system demonstrates full-stack application design and modern cloud infrastructure workflows using AWS, Docker, and CI/CD tools.

---

## 🧩 Application Overview

CloudStore is a web-based e-commerce-style application that includes:

- Secure authentication system  
- Product and order management  
- RESTful backend APIs  
- Frontend UI integrated within the Spring Boot application  

The system follows a layered architecture with clear separation of concerns:

- Controllers – handle HTTP requests  
- Services – contain business logic  
- Repositories – manage database persistence (JPA)  
- Domain Models – represent core entities  

---

## 🏗️ Backend Architecture

The backend is built using Spring Boot and follows a standard service-oriented structure.

### 🔌 External API Integration

- `ProductClient` is a Spring component using `RestTemplate`
- Consumes the **FakeStore API**
- Retrieves product data as `Product[]`
- `ProductService` processes and stores data into a local MySQL database

This hybrid design combines:

- External API data (FakeStore API)  
- Local persistence (MySQL via AWS RDS)  

---

## 🎨 Frontend Integration

The frontend is a lightweight UI served directly from Spring Boot:

- Built using HTML, CSS, and JavaScript  
- Located in `static` and `templates` directories  
- Uses `fetch()` to communicate with backend REST APIs  
- Dynamically renders products and handles user interactions in the browser  

This provides a simple full-stack architecture within a single application.

---

## 🐳 Docker & Containerization

The application is containerized using Docker:

- Spring Boot app packaged into a Docker image  
- Image pushed to Docker Hub  
- Ensures consistent runtime across environments  
- Simplifies deployment and scaling  

---

## ☁️ AWS Cloud Infrastructure

The application is deployed using multiple AWS services:

- **Elastic Beanstalk** – Main deployment environment and orchestration layer  
- **EC2** – Underlying compute instances running the application  
- **Amazon RDS (MySQL/Aurora)** – Managed relational database  
- **S3** – Stores deployment artifacts and application versions  
- **CodeBuild** – Builds the application (JAR artifact)  
- **CodePipeline** – Automates deployment workflow from source to production  

---

## 🔄 CI/CD Workflow

The project uses a combination of GitHub Actions (CI) and AWS CodePipeline (CD).

### Continuous Integration (GitHub Actions)

Triggered on every push:

- Checkout repository  
- Setup Java environment  
- Build Spring Boot application  
- Run tests  
- Build Docker image  
- Push image to Docker Hub  

### Continuous Deployment (AWS CodePipeline)

Automated deployment pipeline:

- Detects changes from GitHub repository  
- CodeBuild compiles the application and generates a JAR file  
- Elastic Beanstalk deploys the updated version  
- Infrastructure is updated automatically on each release  

---

## 📌 Key Takeaways

This project helped me gain practical experience in:

- Spring Boot backend development  
- Full-stack web application structure  
- REST API design and integration  
- Docker containerization  
- AWS cloud deployment and infrastructure  
- CI/CD pipeline design and automation  

---

## 🚀 Summary

CloudStore demonstrates how a Spring Boot application can be developed, containerized, and deployed using modern DevOps practices. It reflects my learning journey in Java development, cloud services, and automated deployment pipelines.
