package utils;

import java.util.Map;

public interface HttpBodyParsingStrategy {
    Map<String, String> parse(String body);
}
