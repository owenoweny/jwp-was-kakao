package webserver;

import enums.MIME;
import utils.HttpBodyParser;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class HttpRequestParser {
    public static final String QUERY_SEPARATOR = "?";
    public static final String PARAMETER_SEPARATOR = "&";
    public static final String SPACE = " ";
    public static final String HEADER_SEPARATOR = ": ";
    public static final String PARAMETER_EQUAL_SIGN = "=";
    public static final String EXTENSION_SEPARATOR = ".";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_LENGTH_KEY = "Content-Length";
    public static final String LOCATION_KEY = "Location";


    public static HttpRequest parse(BufferedReader bufferedReader) throws IOException {
        String[] requestLine = bufferedReader.readLine().split(SPACE);
        HttpMethod httpMethod = HttpMethod.from(requestLine[0]);
        String uriString = requestLine[1];
        String protocol = requestLine[2];

        Map<String, String> headers = parseHeader(bufferedReader);
        Map<String, String> body = parseBody(bufferedReader, headers);
        URI uri = parseURI(uriString);

        return new HttpRequest.Builder()
                .uri(uri)
                .body(body)
                .protocol(protocol)
                .headers(headers)
                .httpMethod(httpMethod)
                .build();
    }

    private static Map<String, String> parseBody(BufferedReader bufferedReader, Map<String, String> headers) throws IOException {
        Map<String, String> body = Map.of();

        if (headers.containsKey(CONTENT_TYPE_KEY) && headers.containsKey(CONTENT_LENGTH_KEY)) {
            String contentType = String.valueOf(headers.get(CONTENT_TYPE_KEY));
            int contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH_KEY));
            String bodyString = IOUtils.readData(bufferedReader, contentLength);
            body = HttpBodyParser.from(contentType).parse(bodyString);
        }
        return body;
    }

    private static Map<String, String> parseHeader(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line;
        while (!"".equals(line = bufferedReader.readLine()) && line != null) {
            String[] header = line.split(HEADER_SEPARATOR);
            headers.put(header[0], header[1]);
        }
        return headers;
    }

    private static URI parseURI(String stringURI) {
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

    public static Map<String, String> parseParameters(String stringUri) {
        Map<String, String> parameters = new HashMap<>();
        int queryStartIndex = stringUri.indexOf(QUERY_SEPARATOR) + 1;
        String query = stringUri.substring(queryStartIndex);

        List<String> queries = parseStringToList(query, PARAMETER_SEPARATOR);

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

    private static List<String> parseStringToList(String requestLine, String separator) {
        String[] requestLineArray = requestLine.split(separator);
        return Arrays.stream(requestLineArray).collect(Collectors.toList());
    }

    private static boolean hasExtension(String stringURI) {
        return stringURI.contains(EXTENSION_SEPARATOR);
    }

    private static boolean hasQuery(String stringURI) {
        return stringURI.contains(QUERY_SEPARATOR);
    }
}
