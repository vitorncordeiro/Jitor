package server;

import logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpServer {
    private final int port;
    private final ExecutorService pool;
    static AtomicInteger totalRequisitions = new AtomicInteger(0);
    private static final String PUBLIC_PACKAGE_PATH = "code/src/main/java/public/" ;

    public HttpServer(int port, int threads){
        this.port = port;
        this.pool = Executors.newFixedThreadPool(threads);
    }

    public void start() throws IOException {
        ServerSocket server = new ServerSocket(port);
        Logger.info("Http server starting at " + port + " port");

        while(true){
            Socket socket = server.accept();
            pool.submit(()->serveClient(socket));
        }
    }
    static void serveClient(Socket socket){
        int req = totalRequisitions.incrementAndGet();
        Logger.info("[thead=" + Thread.currentThread().getName() + "] [requestId=" + req + "] Request received"
                 );
        try(socket){
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            HttpResponse response = new HttpResponse(socket.getOutputStream());
            HttpRequest request = HttpRequest.parse(reader);

            switch(request.getPath()){
                case "/", "/index.html" -> {
                    InputStream is = getResource("/index.html");
                    if (is == null) throw new IOException("Resource not found");
                    response.send(200, "text/html; charset=utf-8", is.readAllBytes());
                }
                default -> {
                    InputStream is = getResource("/404.html");
                    if (is == null) throw new IOException("Resource not found");
                    response.send(404, "text/html; charset=utf-8", is.readAllBytes());
                }
            }
        }catch (IOException e){
            Logger.error(e.getMessage());
        }
    }
    private static InputStream getResource(String path) {
        return HttpServer.class.getResourceAsStream("/public" + path);
    }
}
