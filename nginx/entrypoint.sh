#!/bin/sh
set -e

# Defaults — override via ECS task definition environment variables
: "${API_HOST:=api}"
: "${API_PORT:=8080}"

echo "Starting Nginx with API_HOST=${API_HOST} API_PORT=${API_PORT}"

# Substitute placeholders in the nginx config template
envsubst '${API_HOST} ${API_PORT}' \
    < /etc/nginx/conf.d/app.conf.template \
    > /etc/nginx/conf.d/app.conf

# Hand off to the official nginx entrypoint
exec nginx -g "daemon off;"
