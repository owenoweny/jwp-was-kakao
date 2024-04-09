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
        requestMappings = new HashMap<>();
        for (HttpMethod httpMethod : HttpMethod.values()) {
            requestMappings.put(httpMethod, new URIHandlerMapping());
        }

        Method[] methods = APIHandler.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(HandleRequest.class)) {
                HandleRequest handleRequest = method.getAnnotation(HandleRequest.class);
                requestMappings.get(handleRequest.httpMethod()).addMapping(handleRequest.path(), method);
            }
        }
    }

    public HttpResponse handle(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException {
        HttpMethod httpMethod = httpRequest.getHttpMethod();
        String path = httpRequest.getUri().getPath();
        if (!requestMappings.get(httpMethod).contains(path)) {
            //TODO : 404
            throw new RuntimeException("404");
        }
        Method method = requestMappings.get(httpMethod).find(path);
        //TODO : 싱글톤으로 수정?
        return (HttpResponse) method.invoke(new APIHandler(), httpRequest);
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

        return HttpResponse.found();
    }
}
