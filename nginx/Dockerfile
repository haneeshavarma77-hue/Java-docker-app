# ── Nginx frontend container ──────────────────────────────────────────────────
# Build context is the project root (set in docker-compose.yml)
FROM nginx:1.27-alpine

# envsubst is included in nginx:alpine via gettext
RUN apk add --no-cache gettext

# Remove default config
RUN rm /etc/nginx/conf.d/default.conf

# Copy config as a template (entrypoint will render it at startup)
COPY nginx/nginx.conf /etc/nginx/conf.d/app.conf.template

# Copy static frontend files
COPY src/main/resources/static /usr/share/nginx/html

# Copy and permission the entrypoint script
COPY nginx/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 \
    CMD wget -qO- http://localhost:80/ || exit 1

ENTRYPOINT ["/entrypoint.sh"]
