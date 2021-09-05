package nextstep.jwp.framework.message.request;

import nextstep.jwp.framework.message.HeaderFields;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RequestHeaderTest {

    @DisplayName("RequestHeader 를 생성한다.")
    @Test
    void create() {
        // given
        HeaderFields headerFields = headerFieldsWhenExistsBody();

        // when
        RequestHeader requestHeader = new RequestHeader(headerFields);

        // then
        assertThat(requestHeader.getHeaderFields()).isEqualTo(headerFields);
    }

    @DisplayName("Message Body Length 를 알아낸다 - Body 가 있는 경우")
    @Test
    void takeContentLengthWhenExistsBody() {
        // given
        RequestHeader requestHeader = new RequestHeader(headerFieldsWhenExistsBody());

        // when
        int contentLength = requestHeader.takeContentLength();

        // then
        assertThat(contentLength).isEqualTo(10);
    }

    @DisplayName("Message Body Length 를 알아낸다 - Body 가 없는 경우")
    @Test
    void takeContentLengthWhenNoBody() {
        // given
        RequestHeader requestHeader = new RequestHeader(headerFieldsWhenNoBody());

        // when
        int contentLength = requestHeader.takeContentLength();

        // then
        assertThat(contentLength).isZero();
    }

    @DisplayName("헤더에서 쿠키를 추출한다.")
    @Test
    void extractHttpCookies() {
        // given
        String cookieString =
                "yummy_cookie=choco; tasty_cookie=strawberry; JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46";
        HttpCookies expect = HttpCookies.from(cookieString);

        String headerMessage = String.join("\r\n",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 10",
                "Cookie: " + cookieString,
                "");
        RequestHeader requestHeader = new RequestHeader(headerMessage);

        // when
        HttpCookies httpCookies = requestHeader.extractHttpCookies();

        // then
        assertThat(httpCookies).isEqualTo(expect);
    }

    @DisplayName("Cookie 헤더가 없는 경우 비어 있는 쿠키가 추출된다.")
    @Test
    void extractHttpCookiesWithNoHeader() {
        // given
        String headerMessage = String.join("\r\n",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 10",
                "");
        RequestHeader requestHeader = new RequestHeader(headerMessage);

        // when
        HttpCookies httpCookies = requestHeader.extractHttpCookies();

        // then
        assertThat(httpCookies).isSameAs(HttpCookies.empty());
    }

    @DisplayName("RequestHeader 를 바이트 배열로 변환한다.")
    @Test
    void toBytes() {
        // given
        String headerMessage = String.join("\r\n",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 10",
                "");
        byte[] expect = headerMessage.getBytes();
        RequestHeader requestHeader = new RequestHeader(headerFieldsWhenExistsBody());

        // when
        byte[] bytes = requestHeader.toBytes();

        // then
        assertThat(bytes).isEqualTo(expect);
    }

    @DisplayName("equals 와 hashCode 검증")
    @Test
    void equalsAndHashCode() {
        // given
        HeaderFields headerFields = headerFieldsWhenExistsBody();
        RequestHeader requestHeader = new RequestHeader(headerFields);
        RequestHeader otherRequestHeader = new RequestHeader(headerFields);

        // then
        Assertions.assertThat(requestHeader).isEqualTo(otherRequestHeader)
                .hasSameHashCodeAs(otherRequestHeader);
    }

    private HeaderFields headerFieldsWhenExistsBody() {
        LinkedHashMap<String, String> headerParams = new LinkedHashMap<>();
        headerParams.put("Host", "localhost:8080");
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Content-Length", "10");
        return HeaderFields.from(headerParams);
    }

    private HeaderFields headerFieldsWhenNoBody() {
        LinkedHashMap<String, String> headerParams = new LinkedHashMap<>();
        headerParams.put("Host", "localhost:8080");
        headerParams.put("Connection", "keep-alive");
        return HeaderFields.from(headerParams);
    }
}
