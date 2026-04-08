import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(String method, String path, Map<String, String> headers, String body){
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    public String getPath(){
        return path;
    }


    public static HttpRequest parse(BufferedReader reader) throws IOException {
        String[] parts = reader.readLine().split(" ");
        String path = parts[1];
        String method = parts[0];
        String headerLine;
        Map<String, String> headers = new HashMap<>();
        while(!(headerLine = reader.readLine()).isEmpty()){
            String[] kv = headerLine.split(":", 2);
            headers.put(kv[0].trim(), kv[1].trim());
            System.out.println(headerLine);
        }

        String body = "";
        if(headers.containsKey("Content-Length")) {
            int length = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[length];
            reader.read(buffer, 0, length);
            body = new String(buffer);
        }
        String parsedData = parseJson(body);
        return new HttpRequest(method, path, headers, body);
    }

    public static String parseJson(String jsonRequest){
        
        return "";
        //fazer o split pro json e ir colocando num MAP ou list pra separar o que é primitivo, array e objeto.
    }


}
