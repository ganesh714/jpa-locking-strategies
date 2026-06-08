# JPA Pessimistic vs Optimistic Locking in Concurrent Order Processing

This Spring Boot application demonstrates the differences between JPA Pessimistic and Optimistic locking strategies when dealing with high-concurrency database updates, such as placing orders and decrementing inventory simultaneously.

## What is JPA Locking?
When multiple users try to buy the same limited-stock item at the exact same time, a race condition can occur, potentially leading to overselling the item. JPA locking prevents this.

- **Optimistic Locking (`@Version`)**: Assumes conflicts are rare. It doesn't lock the database row. Instead, it checks a version number when saving. If another transaction has modified the row since it was read, the version changes, and JPA throws an `OptimisticLockException`. It's fast but requires you to handle retry logic on failures.
- **Pessimistic Locking (`@Lock`)**: Assumes conflicts are common. It locks the database row at the database level (`SELECT ... FOR UPDATE`) as soon as it's read. No other transaction can read or write to that row until the lock is released. It's extremely safe but can slow down the system if many users are waiting for the lock.

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

The database is automatically seeded with a `products` table and a dummy product with a stock of 100.

## Testing Concurrency
We use a Bash script `concurrent-test.sh` to fire concurrent requests at the application.

### Testing Optimistic Locking
```bash
./concurrent-test.sh http://localhost:8080/api/orders/optimistic 1 1 5
```
**What to expect:** Some requests will succeed, but others will fail instantly with a message indicating a conflict (`OptimisticLockException`). This is because multiple requests read the same `version` concurrently, but only the first one to save successfully increments the version.

### Testing Pessimistic Locking
```bash
./concurrent-test.sh http://localhost:8080/api/orders/pessimistic 1 1 5
```
**What to expect:** All requests will succeed in a serialized manner (one after another). The database lock prevents concurrent reads of the same row, forcing the other requests to wait in line until the previous order is complete.
