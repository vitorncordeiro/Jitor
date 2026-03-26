import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class HttpRequest {
    private final String method;
    final String path;
    private final Map<String, String> headers;

    public HttpRequest(String method, String path, Map<String, String> headers){
        this.method = method;
        this.path = path;
        this.headers = headers;
    }

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        String[] parts = reader.readLine().split(" ");
        String path = parts[1];
        String method = parts[0];
        String headerLine;
        Map<String, String> headers = new HashMap<>();
        while(!(headerLine = reader.readLine()).isEmpty()){
            String[] kv = headerLine.split(":", 2);
            headers.put(kv[0], kv[1]);
        }

        return new HttpRequest(method, path, headers);
    }
}
