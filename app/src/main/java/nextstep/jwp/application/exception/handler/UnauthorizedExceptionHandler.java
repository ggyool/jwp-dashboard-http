package nextstep.jwp.application.exception.handler;

import nextstep.jwp.framework.exception.ExceptionHandler;
import nextstep.jwp.framework.message.builder.HttpResponseBuilder;
import nextstep.jwp.framework.message.response.HttpResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnauthorizedExceptionHandler implements ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(UnauthorizedExceptionHandler.class);

    @Override
    public HttpResponseMessage run(RuntimeException exception) {
        log.info("인증 관련 예외 발생 {}", exception.getMessage());
        return HttpResponseBuilder.redirectTemporarily("/401.html")
                .build();
    }
}
