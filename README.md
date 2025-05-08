## 📘 Library Management System

A powerful and extensible Spring Boot REST API project that supports secure CRUD operations, statistics, Excel/PDF file handling, and transactional emails with Brevo.

## 📄 Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Tech Stack](#tech-stack)
4. [Getting Started](#getting-started)
5. [API Documentation](#api-documentation)
6. [Security](#security)
7. [Working with Files](#working-with-files)
8. [Statistics](#statistics)
9. [Transactional Emails (Brevo)](#transactional-emails-brevo)

##  🔍 Overview

This project is a Spring Boot REST API that serves as the backend for a multi-library book ordering system. It allows registered users to interact with one or more libraries, view available books, and place orders. Each user is associated with one library, and libraries manage their own book inventories. The system provides full CRUD operations for managing users, libraries, books, and orders.

Key features include secure user registration and authentication, detailed role-based access control, and transactional support for book orders. Administrators can manage libraries and their book collections, while regular users can browse and request books from their associated libraries. The API also supports exporting data to Excel, generating order receipts in PDF format, and sending transactional emails through Brevo SMTP.

This architecture promotes clean separation of concerns, scalability for multi-library environments, and flexibility for future integration with frontend applications or external services.

##  🚀 Features

🔒 Spring Security (JWT)

📊 Statistics endpoints

🗃 FULL CRUD Operations 

✉️ Transactional Emails via Brevo SMTP

📄 PDF Generation

📈 Excel Import/Export

📡 RESTful API

##  🧰 Tech Stack

Java 21+

Spring Boot

Spring Security

Spring Data JPA

PostgreSQL

Brevo SMTP (Email Service)

Apache POI (Excel)

iText/OpenPDF (PDF)

Swagger (for API docs, if applicable)

## ⚙️ Getting Started
Prerequisites
Java 21+

Maven

library.db (PostgreSQL)

Setup Instructions
bash
Copy
Edit
git clone https://github.com/unethicalJon/library-app.git
cd library-app
./mvnw spring-boot:run

## 📡 API Documentation
http://localhost:8080/swagger-ui/index.html

![ScreenRecording2025-04-09at4 03 27PM-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/abeb9e25-2160-42c4-8c31-789e4b07b3e9)


## 🔐 Security

JWT Authentication (Bearer Token)

All routes are role protected

![image](https://github.com/user-attachments/assets/d9af3266-853d-414c-83ee-4ef7ba3e7436)

Google Single Sign-On with 2FA

![Screenshot_7](https://github.com/user-attachments/assets/7d646c3d-7021-402e-9bdc-e08c7c338384)

![image](https://github.com/user-attachments/assets/de0c10df-0bf5-4606-876d-9b9531d5eef9)

![Screenshot_2](https://github.com/user-attachments/assets/7e28b6f5-9f90-4c00-bbf6-f4e2304853af)

![Screenshot_6](https://github.com/user-attachments/assets/01428510-ab38-4ebc-af2e-c73ae882c91a)


## 📁 Working with Files
📊 Excel

Orders.xlsx & BookOrders.xlsx

![image](https://github.com/user-attachments/assets/d2ade58e-6f59-4706-8120-961b73745826)

📄 PDF

Order Invoice 

invoice_order_orderId.pdf

![image](https://github.com/user-attachments/assets/53d228ad-b90b-4c51-9135-d5c9277bf697)


## 📊 Statistics

Top 3 Selling Books by year

GET: http://localhost:8080/api/bookorder/top-3?year=2023

Response:
[
    {
        "year": 2023,
        "book": {
            "id": 8,
            "title": "Harry Potter 1",
            "author": "New Author3"
        },
        "size": 16
    },
    {
        "year": 2023,
        "book": {
            "id": 3,
            "title": "Drita brenda nesh",
            "author": "Michelle Obama"
        },
        "size": 9
    },
    {
        "year": 2023,
        "book": {
            "id": 9,
            "title": "Harry Potter 2",
            "author": "New Author4"
        },
        "size": 7
    }
]

Illustrated in PostgreSQL:

![image](https://github.com/user-attachments/assets/ae924244-031d-4aec-b8d2-f3a878dffd3d)


## ✉️ Transactional Emails (Brevo)

Dynamic and automatic activation email with Brevo template.

<img width="690" alt="image" src="https://github.com/user-attachments/assets/23eb8ecd-0104-46d8-9fb6-6a694c2d0d33" />

