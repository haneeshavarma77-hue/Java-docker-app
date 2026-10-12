# ── Nginx frontend container ──────────────────────────────────────────────────
# Build context is the project root (set in docker-compose.yml)
FROM nginx:1.27-alpine

# Remove default config
RUN rm /etc/nginx/conf.d/default.conf

# Copy our reverse-proxy config
COPY nginx/nginx.conf /etc/nginx/conf.d/app.conf

# Copy the static frontend files
COPY src/main/resources/static /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 \
    CMD wget -qO- http://localhost:80/ || exit 1
