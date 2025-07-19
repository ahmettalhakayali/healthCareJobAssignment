# Gerimedica Hospital Management API

A Spring Boot application for managing hospital appointments and patients. This application provides a RESTful API for creating, searching, and managing medical appointments.

## 🚀 Features

- **Bulk Appointment Creation**: Create multiple appointments for a patient in a single request.
- **Appointment Search**: Find appointments by reason keyword (case-insensitive).
- **Patient Management**: Automatic patient creation when appointments are made.
- **Latest Appointment Lookup**: Find the most recent appointment for a patient.
- **Appointment Deletion**: Remove all appointments for a specific patient.

## 🛠️ Technology Stack

- **Java 21**
- **Spring Boot 3.4.1**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **JUnit 5**
- **Mockito**

## Prerequisites

Before running the application with Docker, you need to ensure that the Gradle wrapper files are present. If the `gradle` directory is missing, run the following command to generate it:

```bash
gradle wrapper
```

## 🐳 Running with Docker

### Using Docker Compose (Recommended)

```bash
# Build and start the application in the foreground
docker-compose up --build

# Build and start in the background (detached mode)
docker-compose up -d --build

# Stop the application
docker-compose down
```

### Using Docker Commands

```bash
# 1. Build the image
docker build -t gerimedica-hospital-api .

# 2. Run the container
docker run -d --name gerimedica-hospital-api -p 8080:8080 gerimedica-hospital-api

# 3. Stop the container
docker stop gerimedica-hospital-api
```

### Using Helper Scripts

The repository includes scripts to simplify the process.

```bash
# Make scripts executable (Linux/Mac)
chmod +x docker-build.sh docker-run.sh

# Build the image using the script
./docker-build.sh

# Run the container using the script
./docker-run.sh
```

## 📋 API Endpoints

Once running, the application is available at `http://localhost:8080`.

- **Health Check**: `http://localhost:8080/actuator/health`
- **H2 Database Console**: `http://localhost:8080/h2-console`
  - **JDBC URL**: `jdbc:h2:mem:testdb`
  - **Username**: `sa`
  - **Password**: `password`

---

### `POST /api/v1/appointments/bulk`
Creates multiple appointments for a patient. If the patient does not exist, a new one is created.

- **Query Parameters**:
  - `patientName` (required): The patient's full name.
  - `ssn` (required): The patient's Social Security Number.
- **Request Body**:
  ```json
  {
    "reasons": ["Checkup", "Follow-up", "X-Ray"],
    "dates": ["2025-02-01", "2025-02-15", "2025-03-01"]
  }
  ```
- **Example Request**:
  ```bash
  curl -X POST 'http://localhost:8080/api/v1/appointments/bulk?patientName=John%20Doe&ssn=123-45-678' \
  -H 'Content-Type: application/json' \
  -d '{
    "reasons": ["Checkup", "Follow-up"],
    "dates": ["2025-08-01", "2025-08-15"]
  }'
  ```

### `GET /api/v1/appointments/search`
Searches for appointments by a keyword in the reason.

- **Query Parameters**:
  - `keyword` (required): The term to search for in appointment reasons.
- **Example Request**:
  ```bash
  curl 'http://localhost:8080/api/v1/appointments/search?keyword=Checkup'
  ```

### `DELETE /api/v1/appointments`
Deletes all appointments associated with a patient's SSN.

- **Query Parameters**:
  - `ssn` (required): The patient's Social Security Number.
- **Example Request**:
  ```bash
  curl -X DELETE 'http://localhost:8080/api/v1/appointments?ssn=123-45-678'
  ```

### `GET /api/v1/appointments/latest`
Gets the most recent appointment for a patient.

- **Query Parameters**:
  - `ssn` (required): The patient's Social Security Number.
- **Example Request**:
  ```bash
  curl 'http://localhost:8080/api/v1/appointments/latest?ssn=123-45-678'
  ``` 