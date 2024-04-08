package webserver;

import exceptions.HttpRequestFormatException;
import webserver.HttpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HttpRequestTest {
    @Test
    void request_resttemplate() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void uri_형식이_유효하지_않은_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> new HttpRequest.Builder()
                .uri("aaa")
                .queryString(Map.of())
                .body(Map.of())
                .protocol("HTTP/1.1")
                .headers(Map.of())
                .httpMethod(HttpMethod.GET)
                .build()
        )
                .isInstanceOf(HttpRequestFormatException.class)
                .hasMessage("uri 형식이 올바르지 않습니다.");
    }
}
