# 🚀 Spring Boot Microservices Demo

A beginner-friendly microservices project with **User Service** and **Order Service** built using Spring Boot, OpenFeign, Eureka Service Registry, H2 In-Memory Database, JPA and Lombok.

---

## 📌 Tech Stack

| Technology | Purpose |
|---|---|
| Spring Boot 3.2.4 | Base framework for all services |
| Spring Cloud Netflix Eureka | Service Registry & Discovery |
| OpenFeign | Inter-service communication |
| Spring Data JPA | Database ORM |
| H2 Database | In-memory database |
| Lombok | Reduce boilerplate code |
| Maven Multi-Module | Single repository structure |

---

## 🏗️ Architecture

```
                        ┌─────────────────────┐
                        │    Eureka Server     │
                        │     Port: 8761       │
                        └─────────┬───────────┘
                    registers /   │   \ registers
                              /   │    \
               ┌─────────────┘   │     └──────────────┐
               ▼                                        ▼
   ┌─────────────────────┐              ┌─────────────────────┐
   │    User Service     │◄─OpenFeign──│    Order Service    │
   │     Port: 8081      │             │     Port: 8082       │
   └─────────┬───────────┘             └──────────┬──────────┘
             │                                     │
             ▼                                     ▼
        H2 Database                          H2 Database
         (userdb)                             (orderdb)
```

---

## 📁 Project Structure

```
microservices-demo/
├── pom.xml                                        ← Parent POM
│
├── eureka-server/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/example/eurekaserver/
│       │   └── EurekaServerApplication.java
│       └── resources/
│           └── application.yml
│
├── user-service/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/example/userservice/
│       │   ├── UserServiceApplication.java
│       │   ├── entity/
│       │   │   └── User.java
│       │   ├── repository/
│       │   │   └── UserRepository.java
│       │   ├── dto/
│       │   │   └── UserResponse.java
│       │   ├── service/
│       │   │   └── UserService.java
│       │   └── controller/
│       │       └── UserController.java
│       └── resources/
│           └── application.yml
│
└── order-service/
    ├── pom.xml
    └── src/main/
        ├── java/com/example/orderservice/
        │   ├── OrderServiceApplication.java
        │   ├── entity/
        │   │   └── Order.java
        │   ├── repository/
        │   │   └── OrderRepository.java
        │   ├── dto/
        │   │   ├── OrderRequest.java
        │   │   ├── OrderResponse.java
        │   │   └── UserResponse.java
        │   ├── client/
        │   │   └── UserClient.java
        │   ├── service/
        │   │   └── OrderService.java
        │   └── controller/
        │       └── OrderController.java
        └── resources/
            └── application.yml
```

---

## ⚙️ Service Configuration

| Service | Port | App Name | H2 Console |
|---|---|---|---|
| Eureka Server | 8761 | eureka-server | ❌ |
| User Service | 8081 | user-service | ✅ `/h2-console` |
| Order Service | 8082 | order-service | ✅ `/h2-console` |

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Step 1 — Clone the repository
```bash
git clone https://github.com/your-username/microservices-demo.git
cd microservices-demo
```

### Step 2 — Build all modules
```bash
mvn clean install
```

### Step 3 — Start services in order

> ⚠️ Always start Eureka Server first. Wait for it to fully start before running the other services.

**Terminal 1 — Eureka Server**
```bash
cd eureka-server
mvn spring-boot:run
```

**Terminal 2 — User Service**
```bash
cd user-service
mvn spring-boot:run
```

**Terminal 3 — Order Service**
```bash
cd order-service
mvn spring-boot:run
```

### Step 4 — Verify all services are registered

Open your browser and go to:
```
http://localhost:8761
```
You should see both `user-service` and `order-service` listed under **Instances currently registered with Eureka**.

---

## 📬 API Endpoints

### User Service — `http://localhost:8081`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/users?name=Alice&email=alice@example.com` | Create a new user |
| GET | `/users/{id}` | Get user by ID |
| GET | `/users` | Get all users |

### Order Service — `http://localhost:8082`

| Method | Endpoint | Description | Body |
|---|---|---|---|
| POST | `/orders` | Create a new order | JSON |
| GET | `/orders/{id}` | Get order by ID | — |
| GET | `/orders` | Get all orders | — |

---

## 🧪 Testing the Flow

### 1. Create a User
```bash
POST http://localhost:8081/users?name=Alice&email=alice@example.com
```

**Response:**
```json
{
  "id": 1,
  "name": "Alice",
  "email": "alice@example.com"
}
```

### 2. Create an Order (uses OpenFeign to fetch user details)
```bash
POST http://localhost:8082/orders
Content-Type: application/json

{
  "userId": 1,
  "product": "Laptop",
  "quantity": 2
}
```

**Response:**
```json
{
  "orderId": 1,
  "product": "Laptop",
  "quantity": 2,
  "user": {
    "id": 1,
    "name": "Alice",
    "email": "alice@example.com"
  }
}
```

### 3. Get All Orders
```bash
GET http://localhost:8082/orders
```

---

## 🔑 Key Concepts Explained

### Why Eureka?
Eureka is the **service registry**. Instead of hardcoding `http://localhost:8081` in the Order Service, it asks Eureka — "where is `user-service` running?" This means if the User Service moves to a different port or host, the Order Service still finds it automatically.

### Why OpenFeign?
OpenFeign lets you call another microservice by writing a simple **Java interface** instead of writing HTTP client code manually. You just annotate a method with the endpoint path and Feign handles the rest.

### Why DTOs?
DTOs (Data Transfer Objects) separate your **database model** from your **API response**. If you add a sensitive field (like a password) to your entity later, it won't accidentally appear in your API response.

### Why H2?
H2 is an in-memory database that runs inside your application — no installation needed. Perfect for development and learning. Each service has its own separate H2 database, which is the microservices pattern (each service owns its own data).

---

## ⚠️ Common Issues & Fixes

| Error | Cause | Fix |
|---|---|---|
| `Syntax error in SQL... insert into order` | `order` is a reserved SQL keyword | Use `@Table(name = "orders")` on the Order entity |
| Services not showing in Eureka | Services started before Eureka was ready | Always start Eureka first and wait 30 seconds |
| Feign returns 404 | Wrong service name in `@FeignClient` | Ensure `name` matches `spring.application.name` exactly |
| Port already in use | Another process on same port | Change port in `application.yml` or kill the process |

---

## 📚 Learning Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)
- [Netflix Eureka](https://docs.spring.io/spring-cloud-netflix/docs/current/reference/html/)
- [Lombok Documentation](https://projectlombok.org/features/)

---

## 👨‍💻 Author

Built for learning purposes. Feel free to fork, modify and use as a reference.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
