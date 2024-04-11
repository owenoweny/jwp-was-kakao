package webserver.httpmessage;

import utils.HttpBodyParser;

import java.util.Map;

public class HttpRequestBody {
    private final Map<String, String> values;

    public HttpRequestBody(Map<String, String> values) {
        this.values = values;
    }

    public static HttpRequestBody empty() {
        return new HttpRequestBody(Map.of());
    }

    public static HttpRequestBody of(String contentType, String bodyString) {
        HttpBodyParser parser = HttpBodyParser.from(contentType);
        return new HttpRequestBody(parser.parse(bodyString));
    }

    public String get(String key) {
        return values.get(key);
    }
}
