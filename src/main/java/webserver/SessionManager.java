package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, Session> sessions = new HashMap<>();

    public static Session createSession() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, new HashMap<>());
        sessions.put(sessionId, session);
        return session;
    }

    public static Session findSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public static boolean hasSession(String sessionId) {
        return sessions.containsKey(sessionId);
    }
}
