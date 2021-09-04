package nextstep.jwp.framework.message;

public interface HttpMessage {
    StartLine getStartLine();

    MessageHeader getHeader();

    MessageBody getBody();

    byte[] toBytes();
}
