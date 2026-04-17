import server.HttpServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8080, 2);
        server.start();
    }

}