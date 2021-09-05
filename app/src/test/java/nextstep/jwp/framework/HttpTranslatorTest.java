package nextstep.jwp.framework;

import nextstep.jwp.framework.common.HttpStatusCode;
import nextstep.jwp.framework.common.HttpVersion;
import nextstep.jwp.framework.message.HeaderFields;
import nextstep.jwp.framework.message.MessageBody;
import nextstep.jwp.framework.message.MessageHeader;
import nextstep.jwp.framework.message.StartLine;
import nextstep.jwp.framework.message.request.HttpRequestMessage;
import nextstep.jwp.framework.message.request.RequestHeader;
import nextstep.jwp.framework.message.request.RequestLine;
import nextstep.jwp.framework.message.response.HttpResponseMessage;
import nextstep.jwp.framework.message.response.ResponseHeader;
import nextstep.jwp.framework.message.response.StatusLine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class HttpTranslatorTest {

    @DisplayName("InputStream 을 번역하여 HttpRequestMessage 를 만든다.")
    @Test
    void translate() throws IOException {
        // given
        InputStream inputStream = generateInputStream();
        OutputStream outputStream = generateOutputStream();
        HttpTranslator httpTranslator = new HttpTranslator(inputStream, outputStream);

        // when
        HttpRequestMessage httpRequestMessage = httpTranslator.translate();

        // then
        StartLine requestLine = httpRequestMessage.getStartLine();
        MessageHeader header = httpRequestMessage.getHeader();
        MessageBody body = httpRequestMessage.getBody();

        assertThat(requestLine).isEqualTo(
                RequestLine.from(requestLineMessage())
        );
        assertThat(header).isEqualTo(
                new RequestHeader(requestHeaderMessage())
        );
        assertThat(body).isEqualTo(
                MessageBody.from(requestBodyMessage())
        );
    }

    @DisplayName("HttpResponseMessage 를 응답한다.")
    @Test
    void respond() throws IOException {
        // given
        String expected = String.join("\r\n",
                "HTTP/1.1 200 OK",
                "Content-Type: text/html;charset=utf-8",
                "Content-Length: 12",
                "",
                "hello world!");

        InputStream inputStream = generateInputStream();
        OutputStream outputStream = generateOutputStream();
        HttpTranslator httpTranslator = new HttpTranslator(inputStream, outputStream);

        // when
        httpTranslator.respond(httpResponseMessage());

        // then
        assertThat(outputStream.toString()).hasToString(expected);
    }

    private InputStream generateInputStream() {
        String requestMessage = String.join("\r\n",
                requestLineMessage(),
                requestHeaderMessage(),
                "",
                requestBodyMessage());

        return new ByteArrayInputStream(requestMessage.getBytes());
    }

    private String requestLineMessage() {
        return "POST /index.html HTTP/1.1";
    }

    private String requestHeaderMessage() {
        return String.join("\r\n",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Content-Length: 12");
    }

    private String requestBodyMessage() {
        return "hello world!";
    }

    private OutputStream generateOutputStream() {
        return new ByteArrayOutputStream();
    }

    private HttpResponseMessage httpResponseMessage() {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1, HttpStatusCode.OK);

        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        fields.put("Content-Type", "text/html;charset=utf-8");
        fields.put("Content-Length", "12");
        HeaderFields headerFields = HeaderFields.from(fields);
        ResponseHeader responseHeader = new ResponseHeader(headerFields);

        MessageBody responseBody = MessageBody.from("hello world!");
        return new HttpResponseMessage(statusLine, responseHeader, responseBody);
    }
}
