package webserver;

import enums.MIME;
import exceptions.HttpRequestFormatException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class URI {
    public static final String QUERY_SEPARATOR = "?";
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

    public static URI from(String stringURI) {
        Map<String, String> parameters = new HashMap<>();
        String path = URLDecoder.decode(stringURI, StandardCharsets.UTF_8);

        if (hasQuery(stringURI)) {
            int queryStartIndex = stringURI.indexOf(QUERY_SEPARATOR);
            path = stringURI.substring(0, queryStartIndex);
            parameters = parseParameters(stringURI);
        }
        if (hasExtension(path)) {
            stringURI = removeQuery(stringURI);
            String extension = parseExtension(stringURI);
            return new URI(path, parameters, MIME.from(extension.toUpperCase()));
        }
        return new URI(path, parameters);
    }

    private void validatePathFormat(String path) {
        if (!path.startsWith("/")) {
            throw new HttpRequestFormatException("uri 형식이 올바르지 않습니다.");
        }
    }

    private static Map<String, String> parseParameters(String stringUri) {
        Map<String, String> parameters = new HashMap<>();
        int queryStartIndex = stringUri.indexOf(QUERY_SEPARATOR) + 1;
        String query = stringUri.substring(queryStartIndex);

        List<String> queries = parseStringToList(query);

        for (String line : queries) {
            String[] parameter = line.split(PARAMETER_EQUAL_SIGN);
            parameters.put(parameter[0], parameter[1]);
        }
        return parameters;
    }

    private static String parseExtension(String stringURI) {
        int dotIndex = stringURI.lastIndexOf(EXTENSION_SEPARATOR);

        return stringURI.substring(dotIndex + 1);
    }

    private static String removeQuery(String path) {
        int index = path.indexOf("?");
        if (index != -1) {
            return path.substring(0, index);
        }
        return path;
    }

    private static List<String> parseStringToList(String requestLine) {
        String[] requestLineArray = requestLine.split(HttpRequest.PARAMETER_SEPARATOR);
        return Arrays.stream(requestLineArray).collect(Collectors.toList());
    }

    private static boolean hasExtension(String stringURI) {
        return stringURI.contains(EXTENSION_SEPARATOR);
    }

    private static boolean hasQuery(String stringURI) {
        return stringURI.contains(QUERY_SEPARATOR);
    }


    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public MIME getExtension() {
        return extension.orElseThrow(() -> new RuntimeException("확장자가 존재하지 않습니다."));
    }

    public boolean hasExtension() {
        return extension.isPresent();
    }
}
