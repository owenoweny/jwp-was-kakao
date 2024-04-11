package webserver.httpmessage;

import enums.MIME;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static webserver.httpmessage.HttpRequest.*;

public class HttpHeaders {
    private Map<String, String> values;

    public HttpHeaders(Map<String, String> values) {
        this.values = values;
    }

    public HttpHeaders() {
        this.values = Map.of();
    }

    public static HttpHeaders from(String headerString) {
        Map<String, String> headers = new HashMap<>();

        Arrays.stream(headerString.split("\n"))
                .forEach((line) -> {
                    String[] header = line.split(HEADER_SEPARATOR);
                    headers.put(header[0], header[1]);
                });
        return new HttpHeaders(headers);
    }

    public static HttpHeaders retrieveResponseHeaders(int lengthOfBody, MIME mime) {
        Map<String, String> headerValues = Map.of(CONTENT_TYPE_KEY,
                mime.contentType,
                CONTENT_LENGTH_KEY,
                Integer.toString(lengthOfBody));
        return new HttpHeaders(headerValues);
    }

    public byte[] formatHttpMessage() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> header : values.entrySet()) {
            result.append(formatOneHeader(header)).append("\n");
        }
        return result.toString().getBytes();
    }

    public boolean containsKey(String key) {
        return values.containsKey(key);
    }

    public String get(String key) {
        return values.get(key);
    }

    public Map<String, String> getValues() {
        return Collections.unmodifiableMap(values);
    }

    private String formatOneHeader(Map.Entry<String, String> header) {
        String result = header.getKey() + HEADER_SEPARATOR + header.getValue();
        if (CONTENT_TYPE_KEY.equals(header.getKey())) {
            result += ";charset=utf-8";
        }
        return result;
    }
}
