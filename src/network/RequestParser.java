package network;

import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private String path;
    private Map<String, String> parameters = new HashMap<>();

    public RequestParser(String url) {
        parseUrl(url);
    }

    private void parseUrl(String url) {
        if (url.contains("?")) {
            String[] parts = url.split("\\?", 2);
            this.path = parts[0];
            String query = parts[1];
            for (String pair : query.split("&")) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    parameters.put(kv[0], kv[1]);
                }
            }
        } else {
            this.path = url;
        }
    }

    public String getPath() {
        return path;
    }

    public String get(String key) {
        return parameters.get(key);
    }

    /**
     * Parses a space-separated body and maps values to the provided keys
     * only if the key is not already present (from query params).
     */
    public void parseBodyIfEmpty(String body, String... keys) {
        if (body == null || body.trim().isEmpty()) return;
        
        String[] parts = body.trim().split("\\s+");
        for (int i = 0; i < parts.length && i < keys.length; i++) {
            parameters.putIfAbsent(keys[i], parts[i]);
        }
    }
}
