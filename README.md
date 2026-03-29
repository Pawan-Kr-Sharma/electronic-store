Electronic Store Platform (Backend)
A robust e-commerce backend solution built with Java 17 and Spring Boot, designed to handle high-volume retail operations, including product management, user carts, and secure order processing

Core Modules & Functionality
This project features a modular architecture to ensure clean separation of concerns and maintainability across the retail lifecycle.
User Module: Manages secure customer profiles, authentication, and personalised data storage.
Product Module: Handles a comprehensive catalogue of electronic goods with support for categories and dynamic pricing.
Cart Module: Implements persistent shopping cart logic, allowing users to manage selections across multiple sessions.
Order Module: Manages the complete order lifecycle from checkout to fulfilment, ensuring transactional integrity.
Payment Module: Integrates secure payment processing to facilitate seamless end-to-end transactions.

Technical Stack:
Backend: Java 8, Spring Boot, Spring MVC.Security: Spring Security (Role-based access control).
Data Persistence: Hibernate, Spring Data JPA.
Database: MySQL (Relational data management).
Build Tool: Maven.

Key Engineering Highlights:
RESTful Design: Implements a standard set of REST APIs for seamless integration with frontend or mobile clients.
Validation & Error Handling: Features centralised exception handling and robust input validation to ensure system reliability.
