import logging.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
        System.out.println("Requistion #" + req + " - thread:"
                + Thread.currentThread().getName());
        try(socket){
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            HttpResponse response = new HttpResponse(socket.getOutputStream());

            HttpRequest request = HttpRequest.parse(reader);
            System.out.println(request.getPath());
            switch(request.getPath()){
                case "/", "/index.html"-> response.sendText(200,
                        new File( PUBLIC_PACKAGE_PATH + "index.html"));
                //case "/json" -> response.sendText(201,
                //        new File(PUBLIC_PACKAGE_PATH));
                default -> response.sendText(404,
                        new File( PUBLIC_PACKAGE_PATH + "404.html"));

            }

        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

}
