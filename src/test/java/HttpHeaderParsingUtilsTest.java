import org.junit.jupiter.api.Test;
import webserver.HttpHeaderParsingUtils;
import webserver.HttpMethod;
import webserver.HttpRequest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpHeaderParsingUtilsTest {
    @Test
    void HTTP_헤더_문자열을_HttpRequest_객체로_파싱한다() {
        String httpRequestString = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*";

        HttpRequest httpRequest = HttpHeaderParsingUtils.parse(httpRequestString);

        assertThat(httpRequest.getHttpMethod())
                .isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getBody())
                .isEqualTo(Map.of());
        assertThat(httpRequest.getUri())
                .isEqualTo("/index.html");
        assertThat(httpRequest.getHeaders())
                .contains(Map.entry("Connection", "keep-alive"))
                .contains(Map.entry("Accept", "*/*"))
                .contains(Map.entry("Host", "localhost:8080"));
        assertThat(httpRequest.getProtocol())
                .isEqualTo("HTTP/1.1");
    }
}
