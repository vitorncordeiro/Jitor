# Jitor - Java HTTP Server

## Overview

This repository contains a small HTTP server written from scratch in Java as a learning project.

Today, the project is a **minimal socket-based server prototype**: it accepts connections, parses basic HTTP requests, and serves HTML files for two route outcomes (`/` or not found).

## Current Features (Implemented)

- TCP server using `ServerSocket`
- Fixed thread pool for concurrent request handling
- Basic HTTP request parsing:
  - request line (`method`, `path`)
  - headers
  - optional body via `Content-Length`
- Basic HTTP response writing with status line + headers + body
- Static HTML response behavior:
  - `GET /` or `GET /index.html` -> serves `index.html`
  - any other path -> serves `404.html`
- Simple console logger with timestamp and level (`INFO`, `ERROR`)

## Project Structure

```text
JavaHttpServer/
  README.md
  GUIA_TESTES.md
  code/
    pom.xml
    src/main/java/
      Main.java
      HttpServer.java
      HttpRequest.java
      HttpResponse.java
      logging/Logger.java
      public/
        index.html
        404.html
```

## How It Works

1. `Main` creates `HttpServer(8080, 2)` and starts it.
2. `HttpServer` listens on port `8080`.
3. Each accepted socket is handled by the fixed thread pool.
4. `HttpRequest.parse(...)` reads request line, headers, and optional body.
5. `HttpServer` chooses which HTML file to return based on the path.
6. `HttpResponse` writes an HTTP/1.1 response to the socket output stream.

## Run Locally

From the repository root:

```powershell
mvn -f "C:\dev\projects\JavaHttpServer\code\pom.xml" clean compile
java -cp "C:\dev\projects\JavaHttpServer\code\target\classes" Main
```

Then open:

- `http://localhost:8080/`
- `http://localhost:8080/any-other-path` (returns the 404 page)

## Requirements

- Maven
- Java version compatible with `code/pom.xml` (currently set to source/target `25`)

## Purpose

This project is focused on learning fundamentals:

- sockets and network I/O in Java
- HTTP request/response flow
- basic concurrency with thread pools
- server architecture concepts before using full frameworks
