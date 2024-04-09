package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private StatusCode statusCode;
    private byte[] body;
    private MIME mime;

    public HttpResponse(StatusCode statusCode, byte[] body, MIME mime) {
        this.statusCode = statusCode;
        this.body = body;
        this.mime = mime;
    }

    public void send(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getDescription() + " \r\n");
            dos.writeBytes("Content-Type: " + mime.contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
