package webserver;

import annotations.HandleRequest;
import db.DataBase;
import model.User;

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
        Method[] methods = APIHandler.class.getDeclaredMethods();
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
        return (HttpResponse) method.invoke(this, httpRequest);
    }

    @HandleRequest(path = "/", httpMethod = HttpMethod.GET)
    public HttpResponse ping(HttpRequest httpRequest) {
        return HttpResponse.ok("hello world");
    }

    @HandleRequest(path = "/user/create", httpMethod = HttpMethod.POST)
    public HttpResponse saveUser(HttpRequest httpRequest) {
        Map<String, String> parameters = httpRequest.getBody();
        //TODO : NPE 예외처리
        String name = parameters.get("name");
        String email = parameters.get("email");
        String password = parameters.get("password");
        String userId = parameters.get("userId");

        if (DataBase.findUserById(userId) != null) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }
        DataBase.addUser(new User(userId, password, name, email));

        return HttpResponse.found("http://localhost:8080/index.html");
    }
}
