package webserver;

import annotations.HandleRequest;
import webserver.httpmessage.HttpMethod;
import webserver.httpmessage.HttpRequest;
import webserver.httpmessage.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class APIHandler {
    private static Map<HttpMethod, URIHandlerMapping> requestMappings;

    static {
        initializeRequestMapping();
        fillMethod();
    }

    private static void fillMethod() {
        Method[] methods = RequestProcessor.class.getDeclaredMethods();
        for (Method method : methods) {
            addMapping(method);
        }
    }

    private static void initializeRequestMapping() {
        requestMappings = new HashMap<>();
        for (HttpMethod httpMethod : HttpMethod.values()) {
            requestMappings.put(httpMethod, new URIHandlerMapping());
        }
    }

    private static void addMapping(Method method) {
        if (method.isAnnotationPresent(HandleRequest.class)) {
            HandleRequest handleRequest = method.getAnnotation(HandleRequest.class);
            requestMappings.get(handleRequest.httpMethod()).addMapping(handleRequest.path(), method);
        }
    }

    public HttpResponse handle(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException {
        HttpMethod httpMethod = httpRequest.getHttpMethod();
        String path = httpRequest.getUri().getPath();
        if (!requestMappings.get(httpMethod).contains(path)) {
            throw new RuntimeException("처리할 수 없는 요청입니다.");
        }
        Method method = requestMappings.get(httpMethod).find(path);
        return (HttpResponse) method.invoke(RequestProcessor.getInstance(), httpRequest);
    }
}
