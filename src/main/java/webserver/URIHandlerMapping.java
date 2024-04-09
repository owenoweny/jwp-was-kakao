package webserver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class URIHandlerMapping {
    private Map<String, Method> map;

    public URIHandlerMapping() {
        this.map = new HashMap<>();
    }

    public void addMapping(String path, Method method) {
        map.put(path, method);
    }

    public boolean contains(String path) {
        return map.containsKey(path);
    }

    public Method find(String path) {
        return map.get(path);
    }
}
