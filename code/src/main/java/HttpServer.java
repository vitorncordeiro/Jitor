import java.io.BufferedReader;
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

    public HttpServer(int port, int threads){
        this.port = port;
        this.pool = Executors.newFixedThreadPool(threads);
    }

    public void start() throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Http server starting at " + port + " port");

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
            System.out.println(request.path);
            switch(request.path){
                case "/", "/index.html"-> response.sendText(200,
                        """
                        <html>
                        <body>
                        <h1>My HTTP server</h1>
                        <p>working!</p>
                        </body>
                        </html>
                        """);
                case "/about" -> response.sendText(200,
                        """
                        <html>
                        <body>
                        <h1>My HTTP server was built with java core</h1>
                        <p>And it was realy hard!</p>
                        </body>
                        </html>
                        """);
                default -> response.sendText(404,
                        """
                         <html>
                         <body>
                         <h1 style="font-size: 50px">404</h1>
                         <p>Page not founded</p>
                         </body>
                         </html>
                         """);

            }

        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

}
