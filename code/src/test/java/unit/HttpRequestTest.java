package unit;

import org.junit.jupiter.api.Test;
import server.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @Test
    void testParseGetRequest() throws IOException {
        String rawRequest = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n";

        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));
        HttpRequest request = HttpRequest.parse(reader);

        assertEquals("GET", request.getMethod());
        assertEquals("/index.html", request.getPath());
        assertEquals("localhost", request.getHeaders().get("Host"));
        assertEquals("keep-alive", request.getHeaders().get("Connection"));
        assertEquals("", request.getBody());
    }

    @Test
    void testParsePostRequestWithBody() throws IOException {
        String rawRequest = "POST /api/data HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: 13\r\n" +
                "\r\n" +
                "{\"key\":\"value\"}";

        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));
        HttpRequest request = HttpRequest.parse(reader);

        assertEquals("POST", request.getMethod());
        assertEquals("/api/data", request.getPath());
        assertEquals("localhost", request.getHeaders().get("Host"));
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
        assertEquals("{\"key\":\"value\"}", request.getBody());
    }

    @Test
    void testParseRequestWithMultipleHeaders() throws IOException {
        String rawRequest = "GET /page.html HTTP/1.1\r\n" +
                "Host: example.com\r\n" +
                "User-Agent: Mozilla/5.0\r\n" +
                "Accept: text/html\r\n" +
                "Connection: close\r\n" +
                "\r\n";

        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));
        HttpRequest request = HttpRequest.parse(reader);

        assertEquals("GET", request.getMethod());
        assertEquals("/page.html", request.getPath());
        assertEquals(4, request.getHeaders().size());
        assertEquals("example.com", request.getHeaders().get("Host"));
        assertEquals("Mozilla/5.0", request.getHeaders().get("User-Agent"));
        assertEquals("text/html", request.getHeaders().get("Accept"));
    }

}