package webserver;

import exceptions.HttpRequestFormatException;

import java.util.Map;
import java.util.Optional;

public class URI {
    public static final String QUERY_SEPARATOR = "?";
    public static final String PARAMETER_SEPARATOR = "&";
    public static final String SPACE = " ";
    public static final String HEADER_SEPARATOR = ": ";
    public static final String PARAMETER_EQUAL_SIGN = "=";
    public static final String EXTENSION_SEPARATOR = ".";

    private final String path;
    private final Map<String, String> parameters;
    private final Optional<MIME> extension;

    public URI(String path, Map<String, String> parameters) {
        validatePathFormat(path);
        this.path = path;
        this.parameters = parameters;
        this.extension = Optional.empty();
    }

    public URI(String path, Map<String, String> parameters, MIME extension) {
        this.path = path;
        this.parameters = parameters;
        this.extension = Optional.of(extension);
    }

    private void validatePathFormat(String path) {
        if (!path.startsWith("/")) {
            throw new HttpRequestFormatException("uri 형식이 올바르지 않습니다.");
        }
    }
}
