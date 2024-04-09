package webserver;

import enums.MIME;
import enums.StatusCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import static webserver.HttpRequestParser.*;

public class HttpResponse {
    private StatusCode statusCode;
    private byte[] body;
    private Map<String, String> headers;

    public static HttpResponse found(String redirectURI) {
        return new HttpResponse(StatusCode.FOUND, new byte[0], Map.of(LOCATION_KEY, redirectURI));
    }

    public static HttpResponse staticResource(byte[] body, MIME mime) {
        Map<String, String> headers = Map.of(CONTENT_TYPE_KEY, mime.contentType, CONTENT_LENGTH_KEY, Integer.toString(body.length));
        return new HttpResponse(StatusCode.OK, body, headers);
    }

    public HttpResponse(StatusCode statusCode, byte[] body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public void send(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode.getCode() + SPACE + statusCode.getDescription() + " \r\n");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                writeHeader(dos, entry.getKey(), entry.getValue());
            }
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            //TODO: 예외처리
            System.out.println(e.getMessage());
        }
    }

    private void writeHeader(DataOutputStream dos, String key, String value) throws IOException {
        String string = key + HEADER_SEPARATOR + value;
        if (CONTENT_TYPE_KEY.equals(key)) {
            string += ";charset=utf-8";
        }
        dos.writeBytes(string + "\r\n");
    }
}
