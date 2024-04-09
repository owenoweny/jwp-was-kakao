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
        private Map<String, String> headers;
        private Map<String, String> body;

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

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(Map<String, String> body) {
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
