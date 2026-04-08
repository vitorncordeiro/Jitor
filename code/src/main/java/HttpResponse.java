import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse{
    private OutputStream output;

    public HttpResponse(OutputStream output){
        this.output = output;
    }

    public void send(int statusCode, String contentType, byte[] body) throws IOException {
        String statusText = statusText(statusCode);

        String headers =
                "Http/1.1 " + statusCode + " " + statusText + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + body.length + "\r\n" +
                "\r\n";
        output.write(headers.getBytes());
        output.write(body);
        output.flush();
    }
    public void sendText(int status, File html) throws IOException{
        send(status, "text/html; charset=utf-8", Files.readAllBytes(html.toPath()));
    }

    public String statusText(int statusCode){
        Map<Integer, String> statusCodes = new HashMap<>();
        statusCodes.put(200, "OK");
        statusCodes.put(201, "Created");
        statusCodes.put(404, "Not Found");
        statusCodes.put(400, "Bad Request");
        statusCodes.put(500, "Internal Server Error");
        return statusCodes.get(statusCode);
    }
}