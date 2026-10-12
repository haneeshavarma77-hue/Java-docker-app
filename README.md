# Java Task API

A REST API built with **Spring Boot 3** + **Java 21**, packaged with Docker.

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | API overview |
| GET | `/health` | Health check |
| GET | `/tasks` | List all tasks |
| POST | `/tasks` | Create task `{"title": "...", "description": "..."}` |
| GET | `/tasks/{id}` | Get task by ID |
| PUT | `/tasks/{id}` | Update task `{"done": true}` |
| DELETE | `/tasks/{id}` | Delete task by ID |

---

## Run with Docker Compose

```bash
docker compose up --build
```

API available at: http://localhost:8080

## Run locally (requires Java 21 + Maven)

```bash
mvn spring-boot:run
```

## Example Usage

```bash
# Create a task
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Learn Spring Boot", "description": "Build REST APIs"}'

# List tasks
curl http://localhost:8080/tasks

# Mark as done
curl -X PUT http://localhost:8080/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"done": true}'

# Delete
curl -X DELETE http://localhost:8080/tasks/1
```
