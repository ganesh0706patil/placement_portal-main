AI-Powered College Placement Portal

This project is a comprehensive, AI-driven placement portal designed to streamline the campus recruitment process. It connects students, companies, and the college administration on a single platform. The core feature is its ability to automatically analyze student resumes and shortlist candidates based on their suitability for a given job description, using a custom-built machine learning model.
✨ Features

    Role-Based Access Control: Separate interfaces and permissions for Students, Companies, and Admins.

    User Authentication: Secure login for all users with JWT (JSON Web Token).

    Company & Job Management: Companies can register, post job openings, and view applicants.

    Student Profiles: Students can create profiles, upload their resumes, and apply for jobs.

    AI-Powered Resume Processing:

        Automatically extracts skills and keywords from uploaded PDF resumes.

        Calculates a "match score" between a student's resume and a job description.

    Automated Shortlisting: Generates a ranked list of the most suitable candidates for a job, saving recruiters time and effort.

    RESTful API: A well-documented API for all functionalities.

🛠️ Tech Stack

    Backend: Java, Spring Boot, Spring Security, JPA (Hibernate)

    Database: PostgreSQL

    Machine Learning Service: Python, Flask, Scikit-learn, NLTK

    Authentication: JWT

🚀 Getting Started

Follow these instructions to get the project up and running on your local machine.
Prerequisites

    Java JDK 17 or later

    Maven

    Python 3.8 or later

    PostgreSQL

    Postman (for API testing)

1. Database Setup

    Make sure you have PostgreSQL installed and running.

    Create a new database for the project. For example:

    CREATE DATABASE placement_portal;

    Update the database credentials in the src/main/resources/application.properties file:

    spring.datasource.url=jdbc:postgresql://localhost:5432/placement_portal
    spring.datasource.username=your_postgres_username
    spring.datasource.password=your_postgres_password

2. Running the Python ML Service

The machine learning service must be running before you start the main Java application.

    Navigate to the Python service directory:

    cd src/main/python

    Install the required Python packages:

    pip install -r requirements.txt

    The service uses the NLTK library, which needs to download some data. The resume_processor.py script handles this automatically.

    Start the Flask service:

    python ml_service.py

    The service will start on http://localhost:5000.

3. Running the Java Backend

    Open the project in your favorite IDE (like IntelliJ IDEA or VS Code).

    The project uses Maven. Your IDE should automatically detect the pom.xml and download the necessary dependencies.

    Run the PlacementPortalApplication.java file to start the Spring Boot application.

    The backend server will start on http://localhost:8080.

🧪 API Endpoints & Testing

The entire API is documented in the provided Postman collection.

    Import the Collection: Import the PlacementPortal.postman_collection.json file into Postman.

    Authentication:

        First, run the Auth > Login request. Use the credentials for one of the users you created (see data.sql).

        A successful login will automatically save the JWT token to a collection variable.

        All other protected endpoints will now use this token for authentication.

    Explore: Use the organized folders in the collection to test different features like creating jobs, applying for them, and generating AI shortlists.

🏛️ Project Architecture

The application is built on a microservice architecture:

    Spring Boot Backend (Core Application):

        Manages all CRUD operations, business logic, and database interactions.

        Handles user management, job postings, and applications.

        Exposes a REST API for the frontend (and for testing via Postman).

    Flask ML Service (AI Engine):

        A lightweight Python service dedicated to computationally intensive tasks.

        Endpoint /api/extract-skills: Reads a PDF resume, preprocesses the text, and extracts relevant skills.

        Endpoint /api/match: Takes a resume and a job description, and returns a match score based on content similarity (TF-IDF & Cosine Similarity) and skill overlap.

This separation ensures that the core application remains fast and responsive, while the heavy AI processing is offloaded to a specialized service.
