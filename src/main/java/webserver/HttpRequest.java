package webserver;

import exceptions.HttpRequestFormatException;

import java.util.Map;

public class HttpRequest {
    private final URI uri;
    private final HttpMethod httpMethod;
    private final String protocol;
    private final Map<String, Object> headers;
    private final Map<String, Object> body;

    public HttpRequest(URI uri,
                       HttpMethod httpMethod, String protocol,
                       Map<String, Object> headers,
                       Map<String, Object> body) {
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    private HttpRequest(Builder builder) {
        this(builder.uri,
                builder.httpMethod,
                builder.protocol,
                builder.headers,
                builder.body);
    }

    public static class Builder {
        private URI uri;
        private String protocol;
        private HttpMethod httpMethod;
        private Map<String, Object> headers;
        private Map<String, Object> body;

        public Builder uri(URI uri) {
            this.uri = uri;
            return this;
        }

        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder httpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder headers(Map<String, Object> headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(Map<String, Object> body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public URI getUri() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, Object> getBody() {
        return body;
    }
}
