package webserver;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.httpmessage.HttpRequest;
import webserver.httpmessage.HttpResponse;

import javax.xml.crypto.Data;
import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class RequestProcessorTest {
    private static RequestProcessor requestProcessor;

    @BeforeEach
    void setUpEach() {
        DataBase.deleteAll();
    }
    @BeforeAll
    static void setUp() {
        requestProcessor = new RequestProcessor();

    }

    @Test
    void Hello_world_를_반환한다() {
        HttpResponse response = requestProcessor.ping(null);
        assertThat(response.getBody())
                .isEqualTo("hello world".getBytes());
    }

    @Nested
    class saveUser {
        @Test
        void 이미_존재하는_회원인_경우_예외를_발생시킨다() throws IOException {
            String message = "POST /user/create HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "Connection: keep-alive\n" +
                    "Content-Length: 65\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Accept: */*\n" +
                    "\n" +
                    "userId=cu&password=password&name=hi&email=brainbackdoor@gmail.com\n";

            BufferedReader bufferedReader = retrieveBufferedReader(message);
            requestProcessor.saveUser(HttpRequest.from(bufferedReader));

            assertThatThrownBy(() -> requestProcessor.saveUser(HttpRequest.from(retrieveBufferedReader(message))))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("이미 존재하는 회원입니다.");
        }

        @Test
        void 요청_매개변수가_유효하지_않은_경우_예외를_발생시킨다() throws IOException {
            String message = "POST /user/create HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "Connection: keep-alive\n" +
                    "Content-Length: 65\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Accept: */*\n" +
                    "\n" +
                    "password=password&name=hi&email=brainbackdoor@gmail.com\n";

            BufferedReader bufferedReader = retrieveBufferedReader(message);
            assertThatThrownBy(() -> requestProcessor.saveUser(HttpRequest.from(bufferedReader)))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("회원 정보가 유효하지 않습니다.");
        }

        @Test
        void 회원을_저장한다() throws IOException {
            String message = "POST /user/create HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "Connection: keep-alive\n" +
                    "Content-Length: 65\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Accept: */*\n" +
                    "\n" +
                    "userId=cu&password=password&name=hi&email=brainbackdoor@gmail.com\n";

            BufferedReader bufferedReader = retrieveBufferedReader(message);
            requestProcessor.saveUser(HttpRequest.from(bufferedReader));
            System.out.println("userId=cu&password=password&name=hi&email=brainbackdoor@gmail.com".getBytes().length);

            assertThat(DataBase.findAll())
                    .hasSize(1)
                    .contains(new User("cu", "password", "hi", "brainbackdoor@gmail.com"));
        }

        private BufferedReader retrieveBufferedReader(String string) {
            InputStream is = new ByteArrayInputStream(string.getBytes());
            return new BufferedReader(new InputStreamReader(is));
        }
    }

    @Test
    void login() {
    }

    @Test
    void list() {
    }
}
