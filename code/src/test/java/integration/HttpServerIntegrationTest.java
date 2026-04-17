package integration;

import logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpServerIntegrationTest {
    private HttpServer httpServer;
    private int PORT = 8081;
    private int threads = 5;
    private Thread serverThread;

    @BeforeEach
    void starterServer() throws InterruptedException{
        serverThread = new Thread(() -> {
            try{
                httpServer = new HttpServer(PORT, threads);
                httpServer.start();
            } catch (IOException e){
                Logger.error(e.getMessage());
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        waitForServer(PORT, 5000);

    }
    @Test
    @DisplayName("GET /index.html should return 200")
    void indexHtmlReturn200() throws IOException{
        String response = sendRequest("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n");

        assertTrue(response.startsWith("HTTP/1.1 200"),
                "Expected statuscode 200, but found " + firstLine(response));
    }






    @Test
    @DisplayName("GET non-existent path should return 404")
    void nonExistentPathReturn404() throws IOException{

        String response = sendRequest("GET /nonExistent HTTP/1.1\r\n\r\n");

        assertTrue(response.startsWith("HTTP/1.1 404"),
                "Expected statuscode 404, but found " + firstLine(response));
    }

    private String sendRequest(String rawRequest) throws IOException{
        try(Socket socket = new Socket("localhost", PORT);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream()){

            socket.setSoTimeout(5000);
            outputStream.write(rawRequest.getBytes());
            outputStream.flush();

            String s = new String( inputStream.readAllBytes());
            System.out.println(s);


            return new String(s);
        }
    }
    private String firstLine(String response){
        System.out.println(response);
        for(String line:response.split("\r\n")){
            System.out.println(line);
        }
        return response.split("\r\n")[0];
    }

    private void waitForServer(int port, int timeoutMs) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            try (Socket socket = new Socket("localhost", port)) {
                return; // conseguiu conectar, servidor está pronto
            } catch (IOException e) {
                Thread.sleep(100); // tenta novamente em 100ms
            }
        }
        throw new IllegalStateException("Servidor não subiu dentro do timeout");
    }
}
