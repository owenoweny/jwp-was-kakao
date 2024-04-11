package webserver.httpmessage;

import enums.MIME;
import enums.StatusCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import static webserver.httpmessage.HttpRequest.*;

public class HttpResponse {
    private StatusCode statusCode;
    private byte[] body;
    private HttpHeaders headers;

    public static HttpResponse found(String redirectURI) {
        HttpHeaders httpHeaders = new HttpHeaders(Map.of(LOCATION_KEY, redirectURI));
        return new HttpResponse(StatusCode.FOUND, new byte[0], httpHeaders);
    }

    public static HttpResponse ok(String body) {
        return new HttpResponse(StatusCode.OK, body.getBytes(), new HttpHeaders());
    }

    public static HttpResponse staticResource(byte[] body, MIME mime) {
        HttpHeaders httpHeaders = HttpHeaders.retrieveResponseHeaders(body.length, mime);
        return new HttpResponse(StatusCode.OK, body, httpHeaders);
    }

    private HttpResponse(StatusCode statusCode, byte[] body, HttpHeaders headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public void send(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode.getCode() + SPACE + statusCode.getDescription() + " \r\n");
            byte[] formattedHeaders = headers.formatHttpMessage();
            dos.write(formattedHeaders, 0, formattedHeaders.length);
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            //TODO: 예외처리
            System.out.println(e.getMessage());
        }
    }
}
