# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy pom first — lets Docker cache the dependency layer separately
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source and build the fat jar
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy

# Non-root user for security
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

WORKDIR /app

# Copy only the fat jar from the build stage
COPY --from=builder /build/target/task-api-1.0.0.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

# Health check using the /health endpoint
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
    CMD java -version > /dev/null && \
    wget -qO- http://localhost:8080/health || exit 1

# JVM tuning for containers
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
