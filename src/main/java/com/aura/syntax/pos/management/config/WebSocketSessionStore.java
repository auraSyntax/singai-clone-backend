package com.aura.syntax.pos.management.config;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessionStore {
    private static final ConcurrentHashMap<String, String> sessionTokens = new ConcurrentHashMap<>();

    public static void save(String sessionId, String token) {
        sessionTokens.put(sessionId, token);
    }

    public static String getToken(String sessionId) {
        return sessionTokens.get(sessionId);
    }

    public static void remove(String sessionId) {
        sessionTokens.remove(sessionId);
    }

    public static Set<String> getAllSessionIds() {
        return sessionTokens.keySet();
    }

}
