# Jitor - Micro Java WebFramework

## Overview

This project is a lightweight HTTP server and web framework built from scratch in Java. It was designed as a learning and portfolio project to demonstrate how modern web frameworks and servlet containers operate internally.

The framework provides routing, middleware support, JSON handling, static file serving, and basic session management, strongly inspired by tools like Apache Tomcat and Jetty.

---

## Features

* HTTP/1.1 request parsing (methods, headers, body)
* Routing system with support for path parameters
* Middleware pipeline (request/response interception)
* JSON request and response handling
* Static file serving (HTML, CSS, JS, images)
* Thread pool for concurrent request handling
* Cookie parsing and session management
* Configurable server settings
* Basic error handling and logging

---

## Architecture

The project is structured into modular components:

* **HTTP Layer**

  * `HttpRequest` – represents incoming requests
  * `HttpResponse` – builder for responses

* **Server Core**

  * `HttpServer` – manages sockets and request lifecycle
  * Thread pool for concurrency

* **Routing**

  * Maps paths and HTTP methods to handlers
  * Supports dynamic routes (e.g. `/users/:id`)

* **Middleware**

  * Chainable request/response processing
  * Enables logging, authentication, etc.

* **Session Management**

  * In-memory session store
  * Cookie-based session tracking

* **Static File Server**

  * Serves files from a public directory
  * MIME type detection

---

## Example Usage

```java
Router router = new Router();

router.get("/hello", (req, res) -> {
    res.status(200).body("Hello World");
});

router.get("/users/:id", (req, res) -> {
    String id = req.getParam("id");
    res.json("{\"id\": \"" + id + "\"}");
});

HttpServer server = new HttpServer(8080, router);
server.start();
```

---

## Middleware Example

```java
app.use((req, res, next) -> {
    System.out.println(req.getMethod() + " " + req.getPath());
    next.handle();
});
```

---

## Running the Project

1. Clone the repository
2. Compile the project:

```bash
javac -d out src/**/*.java
```

3. Run the server:

```bash
java -cp out server.Main
```

4. Open in browser:

```
http://localhost:8080
```

---

## Performance

This project uses a fixed thread pool to handle concurrent requests. While not intended to replace production-grade servers, it demonstrates core performance concepts such as:

* Request concurrency
* Thread management
* Basic scalability

---

## Limitations

* Not fully compliant with HTTP specification
* No HTTPS support
* In-memory session storage only
* Limited error handling compared to production frameworks

---

## Future Improvements

* HTTP/2 support
* Better routing performance (trie-based)
* File caching and compression
* Annotation-based controllers
* Dependency injection system

---

## Purpose

This project was built to deepen understanding of:

* Networking and sockets in Java
* HTTP protocol internals
* Concurrency and threading
* Web framework design

---
