package nextstep.jwp.http.message.response;

import nextstep.jwp.http.common.HttpStatusCode;
import nextstep.jwp.http.common.HttpVersion;
import nextstep.jwp.http.message.HeaderFields;
import nextstep.jwp.http.message.MessageBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class HttpResponseMessageTest {

    @DisplayName("HttpResponseMessage 생성")
    @Test
    void create() {
        // given
        StatusLine statusLine = statusLine();
        ResponseHeader responseHeader = responseHeader();
        MessageBody responseBody = responseBody();

        // when
        HttpResponseMessage httpResponseMessage = new HttpResponseMessage(statusLine, responseHeader, responseBody);

        // then
        assertThat(httpResponseMessage.getStartLine()).isEqualTo(statusLine);
        assertThat(httpResponseMessage.getHeader()).isEqualTo(responseHeader);
        assertThat(httpResponseMessage.getBody()).isEqualTo(responseBody);
    }

    @DisplayName("HttpResponseMessage 를 byte[] 로 변환")
    @Test
    void toBytes() {
        // given
        String responseMessage = String.join("\r\n",
                "HTTP/1.1 200 OK",
                "Content-Type: text/html;charset=utf-8",
                "Content-Length: 12",
                "",
                "hello world!");
        byte[] expect = responseMessage.getBytes();
        HttpResponseMessage httpResponseMessage = new HttpResponseMessage(statusLine(), responseHeader(), responseBody());

        // when
        byte[] bytes = httpResponseMessage.toBytes();

        // then
        assertThat(bytes).isEqualTo(expect);
    }

    private StatusLine statusLine() {
        return new StatusLine(HttpVersion.HTTP_1_1, HttpStatusCode.OK);
    }

    private ResponseHeader responseHeader() {
        LinkedHashMap<String, String> headerParams = new LinkedHashMap<>();
        headerParams.put("Content-Type", "text/html;charset=utf-8");
        headerParams.put("Content-Length", "12");
        HeaderFields headerFields = new HeaderFields(headerParams);
        return new ResponseHeader(headerFields);
    }


    private MessageBody responseBody() {
        return new MessageBody("hello world!");
    }
}
