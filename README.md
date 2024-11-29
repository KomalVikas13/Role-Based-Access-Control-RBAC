# Role-Based Access Control (RBAC) Project
A Spring Boot-based project that implements Role-Based Access Control (RBAC) for a web application with JWT authentication and multiple user roles (Admin, Moderator, User). The project ensures secure access to resources based on user roles and includes role management functionalities for the Admin.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Setup](#setup)
- [API Endpoints](#api-endpoints)
- [Security](#security)

## Overview
This project implements a role-based access control system using Spring Boot, Spring Security, and JWT authentication. The system has three types of users:
- **Admin**: Full access to manage users and roles.
- **Moderator**: Limited access to moderate content.
- **User**: Basic access to view content.

The system also features secure authentication, role management, and user status management.

## Features
- **JWT Authentication**: Secure login and authorization using JWT tokens.
- **Role Management**: Admin can add new roles and assign them to users.
- **User Management**: Admin can manage users and Moderator statuses.
- **Access Control**: Different access levels for Admin, Moderator, and User.
- **Secure Endpoints**: Protection for sensitive endpoints using role-based access control.

## Technologies
- **Backend**: Spring Boot, Spring Security, JWT, MySQL
- **Build Tool**: Maven
- **Authentication**: JWT (JSON Web Token)
- **Database**: MySQL
- **Other Tools**: Postman, Git

## Setup
### Prerequisites
- Java 17 or higher
- MySQL
- Maven

### 1. Clone the Repository
```bash
https://github.com/KomalVikas13/Role-Based-Access-Control-RBAC.git
cd Role-Based-Access-Control-RBAC
```

### 2. Install Dependencies
Ensure you have Maven installed, then run:
```bash
mvn clean install
```

### 3. Configure the Application
Modify `application.properties` to configure your database credentials and JWT settings.

### 4. Run the Application
```bash
# Option 1: Run directly
mvn spring-boot:run

# Option 2: Build and run JAR file
mvn clean package
java -jar target/your-application.jar
```

## API Endpoints
### Auth Endpoints
- **POST `/auth/register`** - Register a new user.
    - Request Body: `{ "email", "password", "fullName", "roles", "cellNumber" }`
    - Response: Success message

- **POST `/auth/login`** - Login to get JWT token.
    - Request Body: `{ "email", "password" }`
    - Response: `{ "token": "<JWT_TOKEN>", "type": "Bearer" }`

### Admin Endpoints
- **POST `/admin/addRole`** - Add a new role.
    - Request Body: `{ "name" }`
    - Response: Success message

- **GET `/admin/users/{status}`** - Get users by their status (active, inactive).
    - Response: List of users matching the status.
- **POST `admin/updateUserStatus`** - Update user status (active, inactive, denied).
    - Request Body: `{ "email","status" }`
    - Response: Success message

### User Endpoints
Protected routes for users with the USER role. Access is allowed only if the user has the correct role.

## Security
### JWT Authentication
After login, a JWT token is provided. This token should be included in the Authorization header of subsequent requests:
```
Authorization: Bearer <JWT_TOKEN>
```

### Roles and Permissions
- **ADMIN**: Full access to manage users, roles, and other system resources.
- **MODERATOR**: Limited access to moderate content and manage users.
- **USER**: Basic access to view content.

### CORS Configuration
Cross-Origin Resource Sharing (CORS) is configured to allow requests from the frontend application hosted at `http://localhost:5173`. You can modify the allowed origins in the `CorsConfig.java` file.
