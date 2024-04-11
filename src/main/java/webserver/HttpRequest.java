package webserver;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    public static final String PARAMETER_SEPARATOR = "&";
    public static final String SPACE = " ";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_LENGTH_KEY = "Content-Length";
    public static final String LOCATION_KEY = "Location";
    public static final String HEADER_SEPARATOR = ": ";

    private final URI uri;
    private final HttpMethod httpMethod;
    private final String protocol;
    private final HttpHeaders headers;
    private final HttpRequestBody body;

    public HttpRequest(URI uri,
                       HttpMethod httpMethod, String protocol,
                       HttpHeaders headers,
                       HttpRequestBody body) {
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest from(BufferedReader bufferedReader) throws IOException {
        String[] requestLine = bufferedReader.readLine().split(SPACE);
        HttpMethod httpMethod = HttpMethod.from(requestLine[0]);
        String uriString = requestLine[1];
        String protocol = requestLine[2];
        URI uri = URI.from(uriString);

        HttpHeaders headers = HttpHeaders.from(parseHeader(bufferedReader));
        HttpRequestBody httpRequestBody = parseBody(bufferedReader, headers);

        return new HttpRequest(uri, httpMethod, protocol, headers, httpRequestBody);
    }

    private static HttpRequestBody parseBody(BufferedReader bufferedReader, HttpHeaders headers) throws IOException {
        HttpRequestBody httpRequestBody = HttpRequestBody.empty();
        if (headers.containsKey(CONTENT_TYPE_KEY) && headers.containsKey(CONTENT_LENGTH_KEY)) {
            int contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH_KEY));
            String bodyString = IOUtils.readData(bufferedReader, contentLength);
            httpRequestBody = HttpRequestBody.of(headers.get(CONTENT_TYPE_KEY), bodyString);
        }
        return httpRequestBody;
    }

    public URI getUri() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getProtocol() {
        return protocol;
    }

    public HttpRequestBody getBody() {
        return body;
    }

    private static String parseHeader(BufferedReader bufferedReader) throws IOException {
        StringBuilder result = new StringBuilder();
        String line;
        while (!"".equals(line = bufferedReader.readLine()) && line != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }
}
