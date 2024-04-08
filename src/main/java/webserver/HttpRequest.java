package webserver;

import exceptions.HttpRequestFormatException;

import java.util.Map;

public class HttpRequest {
    private final String uri;
    private final HttpMethod httpMethod;
    private final String protocol;
    private final Map<String, Object> headers;
    private final Map<String, Object> queryString;
    private final Map<String, Object> body;

    public HttpRequest(String uri,
                       HttpMethod httpMethod, String protocol,
                       Map<String, Object> headers,
                       Map<String, Object> queryString,
                       Map<String, Object> body) {
        validateUri(uri);
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.protocol = protocol;
        this.headers = headers;
        this.queryString = queryString;
        this.body = body;
    }

    private void validateUri(String uri) {
        if (!uri.startsWith("/") || !uri.startsWith("./")) {
            throw new HttpRequestFormatException("uri 형식이 올바르지 않습니다.");
        }
    }

    private HttpRequest(Builder builder) {
        this(builder.uri,
                builder.httpMethod,
                builder.protocol,
                builder.headers,
                builder.queryString,
                builder.body);
    }

    public static class Builder {
        private String uri;
        private String protocol;
        private HttpMethod httpMethod;
        private Map<String, Object> headers;
        private Map<String, Object> queryString;
        private Map<String, Object> body;

        public Builder uri(String uri) {
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

        public Builder queryString(Map<String, Object> queryString) {
            this.queryString = queryString;
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

    public String getUri() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Map<String, Object> getQueryString() {
        return queryString;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, Object> getBody() {
        return body;
    }
}
