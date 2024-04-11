package webserver;

import exceptions.HttpRequestFormatException;

import java.util.Map;

public class HttpRequest {
    private final URI uri;
    private final HttpMethod httpMethod;
    private final String protocol;
    private final Map<String, String> headers;
    private final Map<String, String> body;

    public HttpRequest(URI uri,
                       HttpMethod httpMethod, String protocol,
                       Map<String, String> headers,
                       Map<String, String> body) {
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    public URI getUri() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getBody() {
        return body;
    }
}
