package webserver;

import annotations.HandleRequest;
import db.DataBase;
import model.User;
import webserver.httpmessage.HttpMethod;
import webserver.httpmessage.HttpRequest;
import webserver.httpmessage.HttpRequestBody;
import webserver.httpmessage.HttpResponse;

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
        HttpRequestBody body = httpRequest.getBody();
        //TODO : NPE 예외처리
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");
        String userId = body.get("userId");

        if (DataBase.findUserById(userId) != null) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }
        DataBase.addUser(new User(userId, password, name, email));

        return HttpResponse.found("http://localhost:8080/index.html");
    }
}
