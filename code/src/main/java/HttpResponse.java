import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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
    public void sendText(int status, String html) throws IOException{
        send(status, "text/html; charset=utf-8", html.getBytes(StandardCharsets.UTF_8));
    }

    public String statusText(int statusCode){
        return switch (statusCode){
            case 200 -> "OK";
            case 404 -> "Not Found";
            case 400 -> "Bad Request";
            case 500 -> "Internal Server Error";
            default  -> "Unknown";
        };
    }
}