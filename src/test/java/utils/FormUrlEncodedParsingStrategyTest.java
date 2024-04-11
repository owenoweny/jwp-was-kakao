package utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FormUrlEncodedParsingStrategyTest {
    @Test
    void form_urlEncoded_타입을_파싱한다() {
        String body = "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com";

        Map<String, String> parsed = new FormUrlEncodedParsingStrategy().parse(body);

        assertThat(parsed)
                .contains(Map.entry("password", "password"))
                .contains(Map.entry("name", "%EC%9D%B4%EB%8F%99%EA%B7%9C"))
                .contains(Map.entry("userId", "cu"))
                .contains(Map.entry("email", "brainbackdoor%40gmail.com"));
    }

    @Test
    void 빈_값일_경우_빈_Map을_반환한다() {
        String body = "";

        assertThat(new FormUrlEncodedParsingStrategy().parse(body))
                .hasSize(0);
    }

    //TODO: 입력 형식이 유효하지 않은 경우 예외를 발생시킨다.
}