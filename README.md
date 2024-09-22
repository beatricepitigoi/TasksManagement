# Task Management Application

## Table of Contents
- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Architecture Overview](#architecture-overview)
- [Database Design](#database-design-)
- [Authentication & Authorization](#authentication--authorization)
- [API Documentation](#api-documentation)
- [Running the Application](#running-the-application)
- [License](#license)

## Overview
This is a **Task Management Application** designed to help users manage their tasks efficiently. It features user authentication, role-based access control (admin/user), and CRUD functionality for tasks. The application is built using the **Spring Boot** framework with a RESTful API architecture.

## Technologies Used
- **Spring Boot** - Main framework for the application
- **Spring Security** - For authentication and role-based authorization
- **JPA / Hibernate** - For database interaction and ORM
- **H2 / PostgreSQL** - In-memory database (H2) for testing and PostgreSQL for production
- **Swagger UI** - For API documentation and testing


## Architecture Overview
The architecture follows a layered approach for clear separation of concerns:

- **Controller Layer**: Handles HTTP requests, processes input, and returns responses.
    - Each controller method is mapped to a specific URL (e.g., `/api/users`, `/api/tasks`).
- **Service Layer**: Contains business logic and interacts with the repository layer.
- **Repository Layer**: Responsible for data access and interacts with the database using JPA/Hibernate.
    - Uses Spring Data JPA to abstract data access logic
- **Entity Layer**: Defines the domain model and maps entities to database tables.
    -  Maps them to database tables using JPA annotations.
       Each entity represents a table in the relational database.
- **Security Layer**: Manages authentication and authorization using Spring Security.


## Database Design
In _'Database Structure.md'_ file you will find the detailed description of how I have defined it.
You will also find the necessary commands for reproducing the db structure.


## Authentication & Authorization
The application uses **Basic Authentication** to secure its endpoints. Authentication is role-based, with two distinct roles: **Admin** and **User**.

### Admin User (Hardcoded)
The admin account is **hardcoded** and stored using **in-memory authentication**. This admin user has elevated privileges and can access all tasks created by any user, as well as perform administrative functions.

**Admin Credentials**:
- **Username**: `admin`
- **Password**: `admin`

These credentials are hardcoded in the application's security configuration file, allowing the admin to manage all users and tasks.

### Regular Users (Database-backed)
Regular users are created using a **sign-up** process, and their credentials are stored in the application's **database** ( **PostgreSQL**).
Each regular user can:
- Sign in to create and manage their own tasks.
- Only view, update, or delete tasks that they have created.

### How Authentication Works
1. **Basic Authentication**: Users and the admin authenticate via **Basic Auth**, which requires the client (browser or API consumer) to send the username and password with every request.

2. **Role-based Access Control**:
    - **Admin Role** (`ROLE_ADMIN`): Has access to all tasks and users.
    - **User Role** (`ROLE_USER`): Can only manage their own tasks, but not defined as a role in database.

### Key Points in the Description:
1. **Admin User (In-memory)**: Hardcoded credentials for admin with elevated privileges.
2. **Regular Users**: Created via a sign-up process and stored in a database with encrypted passwords.
3. **Basic Authentication**: All users authenticate with username and password in each request.
4. **Role-based Access Control**: Different access rights for admin and regular users.
5. **Security**: Passwords are encrypted, ensuring secure authentication.


## API Documentation
The application is documented using **Swagger UI**.
Swagger is accessible at: http://localhost:8080/swagger-ui/index.html

## Running the Application

### Prerequisites
- **Java 22+**
- **Maven** or **Gradle** (for dependency management)
- **PostgresSQL** or **H2** (for the database)

### Running the Application Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/beatricepitigoi/TasksManagement.git

2. Run the application using Maven:
   ```bash
    mvn spring-boot:run

3. The appilication will start on: (it will be redirected to http://localhost:8080/swagger-ui/index.html)
    ```bash
     http://localhost:8080/

## License
This project is licensed under the MIT License - see the LICENSE file for details.

### Key Highlights:
1. **API Documentation** section now includes details about accessing Swagger.
2. **Running the Application** section outlines the prerequisites, how to run the application, and where to access Swagger UI.

Feel free to modify or expand the content based on your specific application setup!
@bptg 



Disclaimer: 
Left unresolved: 
-> At update task still has the getownerid shown, but it takes it from login credentials.


