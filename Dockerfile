FROM eclipse-temurin:17-jdk

WORKDIR /app

# Download PostgreSQL JDBC Driver
ADD https://jdbc.postgresql.org/download/postgresql-42.6.0.jar lib/postgresql.jar

# Copy source files
COPY src /app/src
COPY build.sh /app/build.sh

# Compile Java
RUN chmod +x build.sh && ./build.sh

# Expose the server port
EXPOSE 12345

# Run program
CMD ["java", "-cp", "out:lib/postgresql.jar", "Main"]
