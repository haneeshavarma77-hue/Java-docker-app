# =============================================================================
# Combined Dockerfile for API (Spring Boot) and Frontend (Nginx)
#
# Build the API image:
#   docker build --target api -t java-api .
#
# Build the Frontend image:
#   docker build --target frontend -t java-frontend .
# =============================================================================

# -----------------------------------------------------------------------------
# Stage 1 — Build the Spring Boot fat jar
# -----------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build

# Cache dependency layer separately from source
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn clean package -DskipTests -q

# -----------------------------------------------------------------------------
# Stage 2 — API runtime image (Spring Boot)
# -----------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS api

RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

WORKDIR /app

COPY --from=builder /build/target/task-api-1.0.0.jar app.jar
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
    CMD wget -qO- http://localhost:8080/health || exit 1

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]

# -----------------------------------------------------------------------------
# Stage 3 — Frontend runtime image (Nginx)
# -----------------------------------------------------------------------------
FROM nginx:1.27-alpine AS frontend

RUN apk add --no-cache gettext

# Remove default config
RUN rm /etc/nginx/conf.d/default.conf

# Copy nginx config template and static files
COPY nginx/nginx.conf    /etc/nginx/conf.d/app.conf.template
COPY src/main/resources/static /usr/share/nginx/html

# Copy entrypoint that runs envsubst at container start
COPY nginx/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 \
    CMD wget -qO- http://localhost:80/ || exit 1

ENTRYPOINT ["/entrypoint.sh"]
