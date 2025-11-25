# TeamFileSharing

A simple, containerized Java application for team-based file sharing, built with raw sockets and PostgreSQL.

## Features
- **User Authentication**: Register and Login.
- **Team Isolation**: Users belong to teams and share files within their team.
- **File Operations**: Upload, Download, and Delete files.
- **Modern UI**: Clean interface with Drag & Drop support.
- **Tech Stack**: Java 17, PostgreSQL, Docker, Vanilla JS/CSS.

## Project Structure

```
TeamFileSharing/
├── src/
│   ├── database/       # Database connection (Singleton Pattern)
│   ├── filehandler/    # File I/O operations
│   ├── model/          # Data entities
│   ├── network/        # Custom HTTP Server & Request Processing
│   ├── repository/     # JDBC Data Access
│   ├── service/        # Business Logic
│   ├── index.html      # Frontend HTML
│   ├── script.js       # Frontend Logic
│   ├── style.css       # Frontend Styling
│   └── Main.java       # Entry Point
├── storage/            # File storage volume
├── Dockerfile          # App container config
├── docker-compose.yml  # Orchestration config
└── init.sql            # Database schema & seed data
```

## How to Run

1.  **Prerequisites**: Docker Desktop installed.

2.  **Start Application**:
    ```bash
    docker-compose up -d --build
    ```

3.  **Access Web Interface**:
    Open your browser and go to: [http://localhost:12345](http://localhost:12345)

4.  **Stop Application**:
    ```bash
    docker-compose down -v
    ```
