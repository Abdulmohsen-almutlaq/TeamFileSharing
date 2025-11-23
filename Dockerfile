FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy source files
COPY src /app/src
COPY build.sh /app/build.sh

# Compile Java
RUN chmod +x build.sh && ./build.sh

# Run program
CMD ["java", "Main"]
