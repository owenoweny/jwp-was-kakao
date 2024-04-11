package webserver.httpmessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestParameters {
    private final Map<String, String> values;

    public static RequestParameters from(String stringUri) {
        int queryStartIndex = stringUri.indexOf(URI.QUERY_SEPARATOR) + 1;
        if (queryStartIndex == 0) {
            return new RequestParameters(Map.of());
        }
        String query = stringUri.substring(queryStartIndex);
        Map<String, String> parameterMap = Arrays.stream(query.split(HttpRequest.PARAMETER_SEPARATOR))
                .map(str -> str.split(URI.PARAMETER_EQUAL_SIGN))
                .map(strings -> Map.entry(strings[0], strings[1]))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new RequestParameters(parameterMap);
    }

    public RequestParameters(Map<String, String> values) {
        this.values = values;
    }
}
