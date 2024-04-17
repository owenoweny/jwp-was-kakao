package webserver;

import annotations.HandleRequest;
import db.DataBase;
import model.User;
import utils.FileIoUtils;
import webserver.httpmessage.HttpMethod;
import webserver.httpmessage.HttpRequest;
import webserver.httpmessage.HttpRequestBody;
import webserver.httpmessage.HttpResponse;

import java.io.IOException;
import java.util.Collection;

import static webserver.HttpCookie.SESSION_KEY;

public class RequestProcessor {
    private static final RequestProcessor REQUEST_PROCESSOR_INSTANCE = new RequestProcessor();
    public static final String INDEX_REDIRECT_URI = "http://localhost:8080/index.html";

    public static RequestProcessor getInstance() {
        return REQUEST_PROCESSOR_INSTANCE;
    }

    @HandleRequest(path = "/", httpMethod = HttpMethod.GET)
    public HttpResponse ping(HttpRequest httpRequest) {
        return HttpResponse.ok("hello world");
    }

    @HandleRequest(path = "/user/create", httpMethod = HttpMethod.POST)
    public HttpResponse saveUser(HttpRequest httpRequest) {
        validateSaveUserBody(httpRequest);
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

    private void validateSaveUserBody(HttpRequest httpRequest) {
        HttpRequestBody body = httpRequest.getBody();
        if (!body.contains("userId")
                || !body.contains("password")
                || !body.contains("name")
                || !body.contains("email")) {
            throw new RuntimeException("회원 정보가 유효하지 않습니다.");
        }
    }

    @HandleRequest(path = "/user/login", httpMethod = HttpMethod.POST)
    public HttpResponse login(HttpRequest httpRequest) {
        HttpRequestBody body = httpRequest.getBody();
        //TODO : NPE 예외처리
        String userId = body.get("userId");
        String password = body.get("password");

        User user = DataBase.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            return HttpResponse.found("http://localhost:8080/user/login_failed.html");
        }

        Session session = SessionManager.createSession();
        session.addAttribute("userId", userId);
        session.addAttribute("password", password);

        HttpCookie httpCookie = HttpCookie.of(SESSION_KEY, session.getSessionId());
        HttpResponse httpResponse = HttpResponse.found(INDEX_REDIRECT_URI);
        httpResponse.addCookie(httpCookie, "/");
        return httpResponse;
    }

    @HandleRequest(path = "/user/list", httpMethod = HttpMethod.GET)
    public HttpResponse list(HttpRequest httpRequest) throws IOException {
        checkLogin(httpRequest);

        Collection<User> users = DataBase.findAll();
        byte[] body = FileIoUtils.loadFileFromClassPath("/user/list", users);

        return HttpResponse.ok(body);
    }

    private void checkLogin(HttpRequest httpRequest) {
        String sessionId = httpRequest.getHttpCookie().get(SESSION_KEY);

        if (sessionId.isEmpty() || !SessionManager.hasSession(sessionId)) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
    }
}
