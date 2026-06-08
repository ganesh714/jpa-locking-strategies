# JPA Pessimistic vs Optimistic Locking in Concurrent Order Processing

This Spring Boot application demonstrates the difference between JPA Pessimistic and Optimistic locking strategies when dealing with high-concurrency database updates (e.g., placing orders and decrementing inventory).

## Prerequisites
- Docker and Docker Compose
- Java 17 (if running without Docker)
- Maven

## Setup & Running
To run the database and the application using Docker:

```bash
# Start Postgres and the Application
docker-compose up -d --build
```

The database is automatically seeded with a `products` table and a dummy product.

## Testing Concurrency
We use a Bash script `concurrent-test.sh` to fire concurrent requests at the application.

### Optimistic Locking
Uses a `@Version` field on the entity. When conflicts happen, JPA throws an `OptimisticLockException`.

```bash
./concurrent-test.sh http://localhost:8080/api/orders/optimistic 1 1 5
```
You should notice that some requests succeed while others fail due to a version conflict.

### Pessimistic Locking
Uses `LockModeType.PESSIMISTIC_WRITE`. This locks the row upon reading, ensuring all requests are processed sequentially without conflict (though they may take longer if there is heavy contention).

```bash
./concurrent-test.sh http://localhost:8080/api/orders/pessimistic 1 1 5
```
You should notice that all requests succeed as long as there is enough stock, but they are serialized.
