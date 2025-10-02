# Employee Data Management System

A full-stack web application for managing employee data with a modern React frontend and Spring Boot backend.

## 🏗️ Architecture

This project consists of two main components:

- **Backend (`employee-data-management/`)**: Spring Boot REST API with JPA/Hibernate
- **Frontend (`employee-data-management-ui/`)**: React + TypeScript + Vite application

## 🚀 Features

- **Employee Management**: Create, read, update, and delete employee records
- **Search & Pagination**: Search employees by name, email, or position with pagination
- **Data Validation**: Server-side and client-side validation
- **Responsive UI**: Modern, mobile-friendly interface built with React and Tailwind CSS
- **API Documentation**: Swagger/OpenAPI documentation available
- **Database Support**: H2 (development) and PostgreSQL (production) support

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

### Required Software

- **Java 21** or higher (OpenJDK or Oracle JDK)
- **Node.js 20.19+** or **22.12+** (currently using v22.9.0 - needs upgrade)
- **pnpm** (package manager for frontend)

### Version Check

```bash
# Check Java version
java --version

# Check Node.js version
node --version

# Check pnpm version
pnpm --version
```

> **⚠️ Important**: The current Node.js version (22.9.0) needs to be upgraded to 22.12+ for Vite compatibility.

## 🛠️ Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd employee-data-management-system
```

### 2. Backend Setup (Spring Boot)

Navigate to the backend directory:

```bash
cd employee-data-management
```

#### Build and Run

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The backend will start on `http://localhost:8080`

#### Alternative: Run with Gradle Wrapper (Windows)

```cmd
gradlew.bat build
gradlew.bat bootRun
```

### 3. Frontend Setup (React + Vite)

Navigate to the frontend directory:

```bash
cd employee-data-management-ui
```

#### Install Dependencies

```bash
pnpm install
```

#### Development Mode

```bash
# Start development server
pnpm dev
```

The frontend will start on `http://localhost:5173`

#### Production Build

```bash
# Build for production and copy to backend static resources
pnpm build
```

This command will:

1. Compile TypeScript
2. Build the React application with Vite
3. Create the static resources directory if it doesn't exist
4. Copy built files to the backend's static resources folder

## 🗄️ Database Configuration

### Development (Default)

The application uses H2 in-memory database by default:

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    username: sa
    password:
    url: jdbc:h2:mem:employee-data-management
```

### Production (PostgreSQL)

To use PostgreSQL, set the following environment variables:

```bash
export DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
export DATASOURCE_USERNAME=your_username
export DATASOURCE_PASSWORD=your_password
export DATASOURCE_URL=jdbc:postgresql://localhost:5432/employee_db
```

## 🌐 API Endpoints

The REST API provides the following endpoints:

### Employee Operations

| Method   | Endpoint              | Description                                    |
| -------- | --------------------- | ---------------------------------------------- |
| `GET`    | `/api/employees`      | Get all employees (with pagination and search) |
| `POST`   | `/api/employees`      | Create a new employee                          |
| `PUT`    | `/api/employees/{id}` | Update an employee (full update)               |
| `PATCH`  | `/api/employees/{id}` | Partially update an employee                   |
| `DELETE` | `/api/employees/{id}` | Delete an employee                             |

### Query Parameters

- `search`: Search by name, email, or position
- `page`: Page number (0-based)
- `size`: Number of items per page
- `employeeId`: Filter by specific employee ID

### Example Requests

```bash
# Get all employees
curl http://localhost:8080/api/employees

# Search employees
curl "http://localhost:8080/api/employees?search=john&page=0&size=10"

# Create employee
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","position":"Developer"}'
```

## 📚 API Documentation

When the backend is running, you can access the interactive API documentation at:

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🧪 Testing

### Backend Tests

```bash
cd employee-data-management
./gradlew test
```

### Frontend Tests

```bash
cd employee-data-management-ui
pnpm test:ts  # TypeScript compilation check
```

## 🚀 Production Deployment

### Full Stack Deployment

1. **Build the frontend and integrate with backend**:

   ```bash
   cd employee-data-management-ui
   pnpm build
   ```

2. **Build the backend with integrated frontend**:

   ```bash
   cd employee-data-management
   ./gradlew bootJar
   ```

3. **Run the production JAR**:
   ```bash
   java -jar build/libs/employee-data-management-0.0.1-SNAPSHOT.jar
   ```

The application will be available at `http://localhost:8080` with the React frontend served as static content.

### Environment Variables for Production

```bash
# Database configuration
export DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
export DATASOURCE_USERNAME=your_db_username
export DATASOURCE_PASSWORD=your_db_password
export DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/employee_db

# Server configuration
export SERVER_PORT=8080
```

## 🛠️ Development Workflow

### Running Both Services

1. **Terminal 1 - Backend**:

   ```bash
   cd employee-data-management
   ./gradlew bootRun
   ```

2. **Terminal 2 - Frontend**:
   ```bash
   cd employee-data-management-ui
   pnpm dev
   ```

### Making Changes

- **Backend changes**: The Spring Boot DevTools will automatically restart the application
- **Frontend changes**: Vite provides hot module replacement for instant updates

## 📁 Project Structure

```
employee-data-management-system/
├── employee-data-management/          # Spring Boot Backend
│   ├── src/main/java/co/dj/employee_data_management/
│   │   ├── controller/               # REST Controllers
│   │   ├── service/                  # Business Logic
│   │   ├── model/                    # JPA Entities
│   │   ├── dto/                      # Data Transfer Objects
│   │   ├── repo/                     # JPA Repositories
│   │   └── exception/                # Exception Handlers
│   ├── src/main/resources/
│   │   ├── application.yaml          # Configuration
│   │   └── static/                   # Frontend build output
│   └── build.gradle.kts              # Build configuration
├── employee-data-management-ui/       # React Frontend
│   ├── src/
│   │   ├── components/               # React Components
│   │   ├── hooks/                    # Custom Hooks
│   │   ├── lib/                      # Utilities & API clients
│   │   └── schema/                   # Validation schemas
│   ├── package.json                  # Dependencies & scripts
│   └── vite.config.ts               # Vite configuration
└── README.md                         # This file
```

## 🔧 Troubleshooting

### Common Issues

1. **Node.js Version Error**:

   ```
   You are using Node.js 22.9.0. Vite requires Node.js version 20.19+ or 22.12+
   ```

   **Solution**: Upgrade Node.js to version 22.12+ or use Node Version Manager (nvm)

2. **Directory Not Found Error**:

   ```
   cp: target '../employee-data-management/src/main/resources/static/': No such file or directory
   ```

   **Solution**: This is now fixed in the build script with `mkdir -p`

3. **Port Already in Use**:

   - Backend (8080): Check if another Spring Boot app is running
   - Frontend (5173): Check if another Vite dev server is running

4. **Database Connection Issues**:
   - Verify database credentials and connection URL
   - Ensure PostgreSQL is running (if using production config)

### Logs and Debugging

- **Backend logs**: Check console output when running `./gradlew bootRun`
- **Frontend logs**: Check browser console and terminal output
- **Database**: H2 console available at `http://localhost:8080/h2-console` (development only)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes
4. Run tests: `./gradlew test` (backend) and `pnpm test:ts` (frontend)
5. Commit your changes: `git commit -am 'Add feature'`
6. Push to the branch: `git push origin feature-name`
7. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check the troubleshooting section above
2. Review the API documentation at `http://localhost:8080/swagger-ui/index.html`
3. Check the application logs for error messages
4. Create an issue in the repository with detailed information about the problem
