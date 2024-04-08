package utils;

import java.util.Map;

public interface HttpBodyParsingStrategy {
    Map<String, Object> parse(String body);
}
