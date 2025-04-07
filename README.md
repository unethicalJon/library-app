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

Explain what the project is, what problems it solves, and who it's for. Include any relevant context.

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

