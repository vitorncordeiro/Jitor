package unit;

import org.junit.jupiter.api.Test;
import server.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseTest {

    @Test
    void testSendWithStatusCodeOk() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(output);

        String body = "Hello World";
        response.send(200, "text/plain", body.getBytes());

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("HTTP/1.1 200 OK"));
        assertTrue(result.contains("Content-Type: text/plain"));
        assertTrue(result.contains("Content-Length: " + body.length()));
        assertTrue(result.contains(body));
    }

    @Test
    void testSendWithStatusCodeNotFound() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(output);

        String body = "404 Not Found";
        response.send(404, "text/html; charset=utf-8", body.getBytes());

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("HTTP/1.1 404 Not Found"));
        assertTrue(result.contains("Content-Type: text/html; charset=utf-8"));
        assertTrue(result.contains("Content-Length: " + body.length()));
        assertTrue(result.contains(body));
    }

    @Test
    void testSendWithStatusCodeCreated() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(output);

        String body = "Resource Created";
        response.send(201, "application/json", body.getBytes());

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("HTTP/1.1 201 Created"));
        assertTrue(result.contains("Content-Type: application/json"));
        assertTrue(result.contains("Content-Length: " + body.length()));
    }

    @Test
    void testSendWithStatusCodeBadRequest() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(output);

        String body = "Invalid Request";
        response.send(400, "text/plain", body.getBytes());

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("HTTP/1.1 400 Bad Request"));
    }

    @Test
    void testSendWithStatusCodeInternalError() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(output);

        String body = "Internal Server Error";
        response.send(500, "text/plain", body.getBytes());

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("HTTP/1.1 500 Internal Server Error"));
    }

    @Test
    void testSendWithEmptyBody() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(output);

        response.send(200, "text/plain", new byte[0]);

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("HTTP/1.1 200 OK"));
        assertTrue(result.contains("Content-Length: 0"));
    }

    @Test
    void testSendHeadersEndWithDoubleNewline() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(output);

        String body = "Test";
        response.send(200, "text/plain", body.getBytes());

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("\r\n\r\n"));
        // Verifica que existe uma separação entre headers e body
        int headerBodySeparator = result.indexOf("\r\n\r\n");
        int bodyStart = headerBodySeparator + 4;
        assertEquals(body, result.substring(bodyStart));
    }

}

