package webserver;

import utils.FormUrlEncodedParsingStrategy;
import utils.HttpBodyParser;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpHeaderParsingUtils {
    public static HttpRequest parse(BufferedReader bufferedReader) throws IOException {
        String[] f = bufferedReader.readLine().split(" ");
        HttpMethod httpMethod = HttpMethod.valueOf(f[0]);
        String[] splittedUri = f[1].split("\\?");
        Map<String, Object> queryString = Map.of();
        String uri = splittedUri[0];

        if (splittedUri.length == 2) {
            queryString = new FormUrlEncodedParsingStrategy().parse(splittedUri[1]);
        }
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
                .uri(uri)
                .queryString(queryString)
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
        String[] splittedUri = f[1].split("\\?");
        Map<String, Object> queryString = Map.of();
        String uri = splittedUri[0];

        if (splittedUri.length == 2) {
            queryString = new FormUrlEncodedParsingStrategy().parse(splittedUri[1]);
        }

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
                .uri(uri)
                .queryString(queryString)
                .body(body)
                .protocol(protocol)
                .headers(headers)
                .httpMethod(httpMethod)
                .build();
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
