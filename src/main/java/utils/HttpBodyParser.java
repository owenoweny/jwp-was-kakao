package utils;

import java.util.Map;

public class HttpBodyParser {
    private final HttpBodyParsingStrategy httpBodyParsingStrategy;

    public HttpBodyParser(HttpBodyParsingStrategy httpBodyParsingStrategy) {
        this.httpBodyParsingStrategy = httpBodyParsingStrategy;
    }

    public static HttpBodyParser from(String contentType) {
        if ("application/x-www-form-urlencoded".equals(contentType)) {
            return new HttpBodyParser(new FormUrlEncodedParsingStrategy());
        }
        //예외를 얘가 던지는게 맞나?... HttpRequest가 던져야하지 않나?
        throw new IllegalArgumentException("파싱을 지원되지 않는 컨텐츠타입입니다.");
    }

    public Map<String, String> parse(String bodyString) {
        return httpBodyParsingStrategy.parse(bodyString);
    }
}
