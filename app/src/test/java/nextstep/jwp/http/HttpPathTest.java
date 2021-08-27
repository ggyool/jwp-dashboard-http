package nextstep.jwp.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpPathTest {

    @DisplayName("HTML 경로이면 참을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"/login.html", "/login.html?user=bear&password=password"})
    void isHtmlPath(String path) {
        HttpPath httpPath = new HttpPath(path);
        assertThat(httpPath.isHtmlPath()).isTrue();
    }

    @DisplayName("Query String을 제외한 경로를 추출한다.")
    @ParameterizedTest
    @ValueSource(strings = {"/login.html", "/login.html?user=bear&password=password"})
    void extractUri(String path) {
        String expected = "/login.html";
        HttpPath httpPath = new HttpPath(path);
        assertThat(httpPath.extractUri()).isEqualTo(expected);
    }

    @DisplayName("Query String을 가지고 있으면 참을 반환한다. (?의 여부로 판단)")
    @ParameterizedTest
    @ValueSource(strings = {"/login.html?", "/login.html?user=bear&password=password"})
    void hasQueryString(String path) {
        HttpPath httpPath = new HttpPath(path);
        assertThat(httpPath.hasQueryString()).isTrue();
    }

    @DisplayName("Query String을 추출한다.")
    @Test
    void extractQueryString() {
        String path = "/login.html?user=bear&password=password";
        HttpPath httpPath = new HttpPath(path);
        assertThat(httpPath.extractQueryString()).isEqualTo("user=bear&password=password");
    }

    @DisplayName("Query String을 가지고 있지 않은 경로에서 Query String을 추출한다.")
    @Test
    void extractQueryStringFromNonQueryString() {
        String path = "/login.html";
        HttpPath httpPath = new HttpPath(path);
        assertThatThrownBy(httpPath::extractQueryString)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("Query Params를 추출한다.")
    @Test
    void extractQueryParams() {
        // given
        Map<String, String> expected = Map.of("user", "bear", "password", "password");
        String path = "/login.html?user=bear&password=password";
        HttpPath httpPath = new HttpPath(path);

        // when, then
        assertThat(httpPath.extractQueryParams()).containsAllEntriesOf(expected);
    }

    @DisplayName("Query String을 가지고 있지 않은 경로에서 Query Params를 추출한다.")
    @Test
    void extractQueryParamsFromNonQueryString() {
        String path = "/login.html";
        HttpPath httpPath = new HttpPath(path);
        assertThatThrownBy(httpPath::extractQueryParams)
                .isInstanceOf(IllegalStateException.class);
    }
}