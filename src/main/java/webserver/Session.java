package webserver;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final String sessionId;
    private final Map<String, String> attributes = new HashMap<>();

    public Session(String sessionId, Map<String, String> attributes) {
        this.sessionId = sessionId;
        this.attributes.putAll(attributes);
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getValue(String key) {
        return attributes.get(key);
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }
}
