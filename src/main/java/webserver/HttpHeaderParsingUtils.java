package webserver;

import utils.HttpBodyParser;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static webserver.URI.*;

public class HttpHeaderParsingUtils {
    public static HttpRequest parse(BufferedReader bufferedReader) throws IOException {
        String[] f = bufferedReader.readLine().split(" ");
        HttpMethod httpMethod = HttpMethod.valueOf(f[0]);
        String uriString = f[1];
        String protocol = f[2];

        String line;
        Map<String, Object> headers = new HashMap<>();

        while (!"".equals(line = bufferedReader.readLine()) && line != null) {
            String[] header = line.split(": ");
            headers.put(header[0], header[1]);
        }

        Map<String, Object> body = Map.of();

        // (body의 존재 - Content-Type 필드의 존재) -> 필요충분조건?? 표준에선 있어야댐
        if (headers.containsKey("Content-Type") && headers.containsKey("Content-Length")) {
            //TODO: Content-Length 정수 파싱 예외 처리...
            String contentType = String.valueOf(headers.get("Content-Type"));
            int contentLength = (int) headers.get("Content-Length");
            String bodyString = IOUtils.readData(bufferedReader, contentLength);
            body = HttpBodyParser.from(contentType).parse(bodyString);
        }

        return new HttpRequest.Builder()
                .uri(parseURI(uriString))
                .body(body)
                .protocol(protocol)
                .headers(headers)
                .httpMethod(httpMethod)
                .build();
    }

    public static HttpRequest parse(String httpRequestString) {
        StringTokenizer stringTokenizer = new StringTokenizer(httpRequestString, "\n");
        String[] f = stringTokenizer.nextToken().split(" ");
        HttpMethod httpMethod = HttpMethod.valueOf(f[0]);
        String uriString = f[1];

        String protocol = f[2];

        String tmp;
        Map<String, Object> headers = new HashMap<>();

        while (stringTokenizer.hasMoreTokens() && !(tmp = stringTokenizer.nextToken()).isEmpty()) {
            String[] header = tmp.split(": ");
            headers.put(header[0], header[1]);
        }

        Map<String, Object> body = Map.of();
        
        // (body의 존재 - Content-Type 필드의 존재) -> 필요충분조건?? 표준에선 있어야댐
        if (headers.containsKey("Content-Type")) {
            String bodyString = body(stringTokenizer);
            body = HttpBodyParser.from(String.valueOf(headers.get("Content-Type"))).parse(bodyString);
        }

        return new HttpRequest.Builder()
                .uri(parseURI(uriString))
                .body(body)
                .protocol(protocol)
                .headers(headers)
                .httpMethod(httpMethod)
                .build();
    }

    private static URI parseURI(String stringURI) {
        Map<String, String> parameters = new HashMap<>();
        String path = stringURI;

        if (hasQuery(stringURI)) {
            int queryStartIndex = stringURI.indexOf(QUERY_SEPARATOR);
            path = stringURI.substring(0, queryStartIndex);
            parameters = parseParameters(stringURI);
        }
        if (hasExtension(path)) {
            stringURI = removeQuery(stringURI);
            String extension = parseExtension(stringURI);
            //TODO : 지원하지 않는 MIME인 경우에 대한 예외 처리
            return new URI(path, parameters, MIME.valueOf(extension.toUpperCase()));
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
        String result;
        if (index != -1) {
            result = path.substring(0, index);
        } else {
            result = path;
        }
        return result;
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


    //TODO : rename
    public static String body(StringTokenizer st) {
        StringBuilder sb = new StringBuilder();

        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());

            if (st.hasMoreTokens()) {
                sb.append(" "); // add space character
            }
        }

        return sb.toString();
    }
}
