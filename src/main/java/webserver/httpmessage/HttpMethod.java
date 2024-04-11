package webserver.httpmessage;

import exceptions.HttpRequestFormatException;

public enum HttpMethod {
    GET, POST;

    public static HttpMethod from(String httpMethodString) {
        HttpMethod httpMethod;
        try {
            httpMethod = valueOf(httpMethodString);
        } catch (IllegalArgumentException e) {
            throw new HttpRequestFormatException("지원하지 않는 Http Method입니다.");
        }
        return httpMethod;
    }
}
