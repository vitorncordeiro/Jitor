package unit;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {



    @Test
    void testParseGetRequest() throws IOException {
        String rawRequest = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n";

    }
}