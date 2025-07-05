# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Use Tomcat 10 with Jakarta EE 10
FROM tomcat:10.1-jdk17

# Remove default applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy built WAR file
COPY --from=0 /app/target/EbookWebsite-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copy custom server configuration
COPY server.xml /usr/local/tomcat/conf/

# Expose port 8080
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV CATALINA_OPTS="-Djava.security.egd=file:/dev/./urandom"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"] 