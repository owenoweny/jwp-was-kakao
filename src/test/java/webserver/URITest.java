package webserver;

import exceptions.HttpRequestFormatException;
import org.junit.jupiter.api.Test;
import webserver.httpmessage.URI;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class URITest {
    @Test
    void uri_형식이_유효하지_않은_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> new URI("aaa", Map.of()))
                .isInstanceOf(HttpRequestFormatException.class)
                .hasMessage("uri 형식이 올바르지 않습니다.");
    }
}