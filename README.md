# API Versioning Demo - Java Spring Boot

This project demonstrates three common ways to version a REST API using Java and Spring Boot.

It contains a simple `UserController` with six endpoints that return slightly different JSON responses depending on the API versioning strategy used.

The three versioning strategies demonstrated are:

1. URI path versioning
2. Vendor media type versioning
3. Custom request header versioning

---

## Technology Stack

* Java 21
* Spring Boot
* Spring Web MVC
* Maven
* Springdoc OpenAPI / Swagger UI

---

## What This Project Demonstrates

The controller exposes example user endpoints using three different API versioning techniques.

### 1. URI Path Versioning

The version is part of the URL.

```text
/api/v1/users/{id}
/api/v2/users/{id}
```

Example:

```bash
curl http://localhost:8080/api/v1/users/123
curl http://localhost:8080/api/v2/users/123
```

This is the simplest and most visible approach. It is easy to test, easy to document, and easy to understand.

---

### 2. Header Versioning

The URL stays the same, but the version is passed using a custom request header.

```text
/api/headers/users/{id}
```

Version 1:

```bash
curl -H "API-Version: 1" \
  http://localhost:8080/api/headers/users/123
```

Version 2:

```bash
curl -H "API-Version: 2" \
  http://localhost:8080/api/headers/users/123
```

This keeps URLs clean, but it is less visible than URI versioning because the version is hidden in the request headers.

---

### 3. Vendor Media Type Versioning

The URL stays mostly the same, but the requested version is specified using the HTTP `Accept` header.

```text
/api/media/users/{id}
```

Version 1:

```bash
curl -H "Accept: application/vnd.demo.v1+json" \
  http://localhost:8080/api/media/users/123
```

Version 2:

```bash
curl -H "Accept: application/vnd.demo.v2+json" \
  http://localhost:8080/api/media/users/123
```

This approach uses HTTP content negotiation. The resource URL stays stable, while the client asks for a specific representation of that resource.

---

## Endpoint Summary

| Versioning Type | Method | Endpoint                  | Version Selection                      |
| --------------- | -----: | ------------------------- | -------------------------------------- |
| URI path        |    GET | `/api/v1/users/{id}`      | Path contains `v1`                     |
| URI path        |    GET | `/api/v2/users/{id}`      | Path contains `v2`                     |
| Header          |    GET | `/api/headers/users/{id}` | `API-Version: 1`                       |
| Header          |    GET | `/api/headers/users/{id}` | `API-Version: 2`                       |
| Media type      |    GET | `/api/media/users/{id}`   | `Accept: application/vnd.demo.v1+json` |
| Media type      |    GET | `/api/media/users/{id}`   | `Accept: application/vnd.demo.v2+json` |

---

## Example Responses

### URI Version 1

```bash
curl http://localhost:8080/api/v1/users/123
```

Returns:

```json
{
  "id": "123",
  "name": "Alice Smith"
}
```

### URI Version 2

```bash
curl http://localhost:8080/api/v2/users/123
```

Returns:

```json
{
  "id": "123",
  "firstName": "Alice",
  "lastName": "Smith",
  "status": "ACTIVE"
}
```

### Header Version 1

```bash
curl -H "API-Version: 1" \
  http://localhost:8080/api/headers/users/123
```

Returns:

```json
{
  "id": "123",
  "name": "Bob Smith"
}
```

### Header Version 2

```bash
curl -H "API-Version: 2" \
  http://localhost:8080/api/headers/users/123
```

Returns:

```json
{
  "id": "123",
  "firstName": "Bob",
  "lastName": "Smith",
  "status": "ACTIVE"
}
```

### Media Type Version 1

```bash
curl -H "Accept: application/vnd.demo.v1+json" \
  http://localhost:8080/api/media/users/123
```

Returns:

```json
{
  "id": "123",
  "name": "Paaa Smith"
}
```

### Media Type Version 2

```bash
curl -H "Accept: application/vnd.demo.v2+json" \
  http://localhost:8080/api/media/users/123
```

Returns:

```json
{
  "id": "123",
  "firstName": "Paas",
  "lastName": "Smith",
  "status": "ACTIVE"
}
```

---

## Running the Application

From the project root:

```bash
./mvnw spring-boot:run
```

Or, if Maven is installed locally:

```bash
mvn spring-boot:run
```

The application should start on:

```text
http://localhost:8080
```

---

## Swagger / OpenAPI

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

or:

```text
http://localhost:8080/swagger-ui/index.html
```

The raw OpenAPI JSON is available at:

```text
http://localhost:8080/v3/api-docs
```

Note that Swagger may show fewer visible endpoint rows than there are Java controller methods.

This is expected because some methods share the same HTTP method and path, differing only by headers or media types.

For example, these two Java methods:

```java
@GetMapping(value = "/headers/users/{id}", headers = "API-Version=1")
@GetMapping(value = "/headers/users/{id}", headers = "API-Version=2")
```

may appear as a single logical path in Swagger because both use:

```text
GET /api/headers/users/{id}
```

The same applies to media type versioning, where both versions share:

```text
GET /api/media/users/{id}
```

---

## PowerShell Note

On Windows PowerShell, `curl` may be an alias for `Invoke-WebRequest`.

If this causes problems, use `curl.exe` instead:

```powershell
curl.exe -H "API-Version: 1" http://localhost:8080/api/headers/users/123
```

Or use PowerShell syntax:

```powershell
Invoke-WebRequest `
  -Uri "http://localhost:8080/api/headers/users/123" `
  -Headers @{ "API-Version" = "1" }
```

---

## Building the Project

```bash
mvn clean package
```

This produces a JAR under:

```text
target/
```

Run the packaged application with:

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## Why This Demo Exists

The purpose of this project is to demonstrate that REST APIs can evolve over time without breaking existing clients.

Version 1 responses use a simple `name` field.

Version 2 responses split the name into separate fields and add a `status`.

This simulates a realistic API evolution:

```json
{
  "id": "123",
  "name": "Alice Smith"
}
```

evolving into:

```json
{
  "id": "123",
  "firstName": "Alice",
  "lastName": "Smith",
  "status": "ACTIVE"
}
```

Existing clients can continue using version 1, while newer clients can adopt version 2.

---

## Comparison of Versioning Strategies

| Strategy              | Pros                                 | Cons                                       |
| --------------------- | ------------------------------------ | ------------------------------------------ |
| URI path versioning   | Simple, visible, easy to test        | Version is embedded in the URL             |
| Header versioning     | Clean URLs                           | Less discoverable, harder to test manually |
| Media type versioning | Good use of HTTP content negotiation | More complex and less familiar             |

For most simple public APIs, URI path versioning is usually the easiest to understand and maintain.

Header and media type versioning are useful to understand because they show how Spring can route requests based on more than just the URL.
