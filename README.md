# TeamFileSharing

A simple Java application for managing teams, users, and file sharing, backed by a postgresSQL database.

## Project Structure

```
TeamFileSharing/
├── src/
│   ├── database/       # Database connection logic
│   ├── filehandler/    # File management operations
│   ├── model/          # Data models (User, Team, FileItem)
│   ├── network/        # Network communication
│   ├── repository/     # Data access layer
│   ├── service/        # Business logic
│   └── Main.java       # Application entry point
├── storage/            # Directory for stored files
├── Dockerfile          # Docker build configuration
├── docker-compose.yml  # Docker Compose configuration
├── init.sql            # Database initialization script
└── build.sh            # Build script
```

## How to Run with Docker

1.  **Prerequisites**: Ensure you have Docker desktop  installed.

2.  **Build and Run**:
    Open a terminal in the project root and run:
    ```bash
    docker-compose up -d --build
    ```

3.  **Stop the Application**:
    To stop the containers and remove volumes:
    ```bash
    docker-compose down -v
    ```
