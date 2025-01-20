# CAPSTONEPROJECT-API

Creating Java Backend for a Payment Application

## Table of Contents
- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Team Members](#team-members)

## Introduction
This project is a capstone project aimed at developing the backend for a payment application using Java Spring Boot. The application allows users to register, log in, and manage their payment transactions securely.

## Technologies Used
- **Backend**: Java Spring Boot
- **Database**: MySQL, H2 (for testing)
- **Security**: Spring Security, JWT
- **Build Tool**: Maven

## Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven
- MySQL

### Backend Setup
1. Clone the repository:
    ```sh
    git clone https://github.com/itsgourav62/CapstoneProject-API.git
    cd CapstoneProject-API
    ```

2. Configure the database:
    - Update the [`src/main/resources/application.yml`](src/main/resources/application.yml ) file with your MySQL database credentials.

3. Build the project:
    ```sh
    ./mvnw clean install
    ```

4. Run the backend application:
    ```sh
    ./mvnw spring-boot:run
    ```

## Running the Application
- The backend server will be running at [`http://localhost:8080`](http://localhost:8080).

## Testing
### Backend Tests
To run the backend tests, use the following command:
```sh
./mvnw test