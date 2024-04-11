package webserver;

import annotations.HandleRequest;
import db.DataBase;
import model.User;

import java.util.Map;

public class RequestProcessor {
    private static final RequestProcessor REQUEST_PROCESSOR_INSTANCE = new RequestProcessor();

    public static RequestProcessor getInstance() {
        return REQUEST_PROCESSOR_INSTANCE;
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
