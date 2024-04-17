package webserver;

import webserver.httpmessage.HttpHeaders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpCookie {
    public static final String SESSION_KEY = "JSSESSIONID";
    private final Map<String, String> values;

    private HttpCookie(Map<String, String> values) {
        this.values = values;
    }

    public static HttpCookie from(HttpHeaders headers) {
        String cookieHeader = headers.get("Cookie");
        if (cookieHeader == null) {
            return HttpCookie.empty();
        }
        return HttpCookie.parseHeader(cookieHeader);
    }

    private static HttpCookie parseHeader(String headerString) {
        Map<String, String> values = new HashMap<>();
        Arrays.stream(headerString.split(";"))
                .map(String::trim)
                .forEach(cookie -> {
                    String[] cookiePair = cookie.split("=");
                    values.put(cookiePair[0], cookiePair[1]);
                });

        return new HttpCookie(values);
    }

    public static HttpCookie of(String key, String value) {
        Map<String, String> values = Map.of(key, value);
        return new HttpCookie(values);
    }

    private static HttpCookie empty() {
        return new HttpCookie(new HashMap<>());
    }

    public String formatResponse() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }
        return result.toString();
    }

    public String get(String key) {
        return values.get(key);
    }
}
