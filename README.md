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

Key features include secure user registration and authentication, detailed role-based access control, and transactional support for book orders. Administrators can manage libraries and their book collections, while regular users can browse and request books from their associated libraries. The API also supports exporting data to Excel, generating order receipts in PDF format, and sending transactional email notifications (e.g., order confirmations) through Brevo SMTP.

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

## 🔐 Security

JWT Authentication (Bearer Token)

All routes are role protected

## 📁 Working with Files
📊 Excel

Orders.xlsx & BookOrders.xlsx

📄 PDF

Order Invoice 

invoice_orderNumber.pdf

## 📊 Statistics

Top 3 Selling Books by year

## ✉️ Transactional Emails (Brevo)

Dynamic and automatic activation email with Brevo template.

