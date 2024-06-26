# FITKIT

FITKIT is a fitness application designed to help users with their food and workout schedules. It aims to provide a comprehensive solution for managing fitness routines, dietary preferences, and personal goals. This project leverages a microservice architecture, allowing for scalability and flexibility. FITKIT is a side project intended for experimenting with various technologies, including subscription models and Kafka.

## Project Overview
### Features

- **Food Management:** Track and manage your dietary preferences, allergies, and food items. 
- **Workout Schedules:** Create and manage workout routines tailored to individual goals.
- **Subscription Models:** Experiment with different subscription tiers and benefits.
- **Microservice Architecture:** Modular design for scalability and flexibility.
- **Event-Driven Architecture:** Utilize Kafka for real-time data streaming and communication between services.

### Technology Stack

- **Backend:** Java, Spring Boot, Spring Data JPA, Hibernate
- **Frontend:** Vue.js, Pinia, TailwindCSS
- **Database:** MSSQL
- **Messaging:** Apache Kafka
- **Authentication:** Keycloak
- **Containerization:** Docker
- **Build Tools:** Maven, Gradle

## Microservice Architecture
### Services

- **Customer Service:** Manages customer information, including personal details, preferences, and subscription levels.
- **Food Service:** Handles food items, dietary preferences, and allergies.
- **Workout Service:** Manages workout routines and schedules.
- **Subscription Service:** Manages subscription plans and payment processing.
- **Notification Service:** Sends notifications to users about their schedules, updates, and more.

### Communication

- **REST API:** Services communicate via RESTful APIs.
- **Kafka:** Used for event-driven communication and real-time data streaming.

## Getting Started
### Prerequisites

- Java 17
- Node.js
- Docker
- MSSQL
- Apache Kafka

## License

This project is licensed under a proprietary license. Unauthorized copying, distribution, and commercial use are prohibited.