# 🔗 Yorlink API

A powerful and feature-rich URL shortening service built with Spring Boot. Create short, memorable links with advanced features like password protection, scheduled activation, expiration dates, and click tracking.

## ✨ Features

- **🔐 Password Protection** - Secure your short URLs with password authentication
- **📅 Scheduled Activation** - Set activation dates for your links
- **⏰ Auto-Expiration** - Configure expiration dates or maximum click limits
- **🎯 Custom Aliases** - Create memorable, custom short URL aliases
- **📊 Click Tracking** - Monitor click counts for your shortened URLs
- **🌐 IP Tracking** - Track the origin of URL creation requests
- **🔄 Duplicate Prevention** - Automatically reuse existing short URLs for the same original URL
- **🎨 Multiple URL Types** - Support for normal, scheduled, expiring, and protected URLs

## 🛠️ Tech Stack

- **Java 21** - Modern Java with latest features
- **Spring Boot 3.5.7** - Enterprise-grade application framework
- **Spring Data JPA** - Database abstraction layer
- **Spring Security** - Authentication and authorization
- **PostgreSQL 16.3** - Robust relational database
- **Lombok** - Boilerplate code reduction
- **Docker & Docker Compose** - Containerized deployment
- **Gradle** - Build automation tool

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
- **Gradle 8.x** or higher (or use the included Gradle wrapper)
- **Docker** and **Docker Compose** (for database setup)
- **PostgreSQL 16.3** (if not using Docker)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd yorlink-api
```

### 2. Environment Setup

Create a `.env` file in the root directory with the following variables:

```env
POSTGRES_DB=apartment_db
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin
SPRING_DATASOURCE_HOST=localhost
```

### 3. Start the Database

Using Docker Compose (recommended):

```bash
make db/up
```

Or manually:

```bash
docker-compose up database -d
```

The database will be available at `localhost:5432` with the credentials specified in your `.env` file.

### 4. Run Database Migrations

The database schema will be automatically initialized from `migrations/init.sql` when the Docker container starts for the first time.

### 5. Build and Run the Application

Using Gradle Wrapper:

```bash
# On Windows
gradlew.bat bootRun

# On Linux/Mac
./gradlew bootRun
```

Or build and run separately:

```bash
# Build
./gradlew build

# Run
./gradlew bootRun
```

The API will be available at `http://localhost:8080`

## 📡 API Endpoints

### Base URL
```
/api/public/url
```

### Create Short URL
```http
POST /api/public/url
Content-Type: application/json

{
  "url": "https://example.com/very/long/url",
  "alias": "my-custom-link",  // Optional: custom alias
  "isCustomAlias": true,      // Optional: use custom alias
  "type": "normal",           // normal, scheduled, expiring, protect
  "password": "secure123",    // Required for protect type
  "passwordHint": "My hint",  // Optional hint for protected URLs
  "activationDate": "2024-12-01",  // For scheduled type
  "expirationDate": "2024-12-31",  // For expiring type
  "maximumClicks": 100        // For expiring type
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "alias": "my-custom-link",
    "shortUrl": "http://localhost:8080/my-custom-link",
    "originalUrl": "https://example.com/very/long/url"
  }
}
```

### Get URL Details
```http
GET /api/public/url/detail?alias=my-custom-link
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "alias": "my-custom-link",
    "originalUrl": "https://example.com/very/long/url",
    "type": "normal",
    "currentClicks": 0,
    "isActive": true,
    "createdAt": "2024-11-20"
  }
}
```

### Access Protected URL
```http
POST /api/public/url/protected
Content-Type: application/json

{
  "id": 1,
  "password": "secure123"
}
```

### Delete Short URL
```http
DELETE /api/public/url/{id}
```

## 🏗️ Project Structure

```
yorlink-api/
├── src/
│   ├── main/
│   │   ├── java/org/yorlink/yorlinkapi/
│   │   │   ├── config/          # Configuration classes (CORS, Security)
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── service/          # Business logic
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── model/
│   │   │   │   ├── entity/      # JPA entities
│   │   │   │   └── dtos/        # Data transfer objects
│   │   │   └── excetion/        # Exception handlers
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Test files
├── migrations/                   # Database migration scripts
├── docker-compose.yml           # Docker services configuration
├── build.gradle                 # Gradle build configuration
└── Makefile                     # Convenience commands
```

## ⚙️ Configuration

### Application Properties

The main configuration is in `src/main/resources/application.properties`:

```properties
spring.application.name=yorlink-api

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/apartment_db
spring.datasource.username=admin
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver
```

Environment variables can override these values:
- `SPRING_DATASOURCE_HOST` - Database host
- `POSTGRES_DB` - Database name
- `POSTGRES_USER` - Database user
- `POSTGRES_PASSWORD` - Database password

## 🗄️ Database Schema

The `short_urls` table includes:

- **id** - Primary key
- **alias** - Unique short URL identifier
- **original_url** - The full URL being shortened
- **type** - URL type (normal, scheduled, expiring, protect)
- **activation_date** - When the URL becomes active (for scheduled)
- **expiration_date** - When the URL expires (for expiring)
- **maximum_clicks** - Maximum allowed clicks (for expiring)
- **current_clicks** - Current click count
- **password_hash** - Hashed password (for protect)
- **password_hint** - Optional password hint
- **is_custom_alias** - Whether alias was custom or auto-generated
- **ip_address** - IP address of the creator
- **created_at** - Creation timestamp
- **updated_at** - Last update timestamp

## 🔒 Security Features

- **BCrypt Password Hashing** - Secure password storage for protected URLs
- **Spring Security** - Framework-level security
- **CORS Configuration** - Cross-origin resource sharing setup
- **Input Validation** - Request validation using Spring Validation

## 🧪 Testing

Run tests using:

```bash
./gradlew test
```

## 📝 URL Types

### Normal
- Always active
- No expiration
- No password protection

### Scheduled
- Becomes active on `activationDate`
- Remains active indefinitely after activation

### Expiring
- Can expire by date (`expirationDate`)
- Can expire by clicks (`maximumClicks`)
- Tracks `currentClicks`

### Protect
- Requires password to access
- Password stored as BCrypt hash
- Optional password hint

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License.

---

**Built with ❤️ using Spring Boot**

