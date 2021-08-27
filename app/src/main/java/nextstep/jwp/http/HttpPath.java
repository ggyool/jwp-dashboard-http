package nextstep.jwp.http;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpPath {

    private static final String HTML_SUFFIX = ".html";
    private static final String QUERY_STRING_SEPARATOR = "?";
    private static final String QUERY_STRING_PIECE_SEPARATOR = "&";
    private static final String QUERY_STRING_PARAM_SEPARATOR = "=";

    private final String path;

    public HttpPath(String path) {
        this.path = path;
    }

    public boolean isHtmlPath() {
        return extractUri().endsWith(HTML_SUFFIX);
    }

    public String extractUri() {
        if (!hasQueryString()) {
            return path;
        }
        int index = path.indexOf(QUERY_STRING_SEPARATOR);
        return path.substring(0, index);
    }

    public boolean hasQueryString() {
        return path.contains(QUERY_STRING_SEPARATOR);
    }

    public String extractQueryString() {
        if (!hasQueryString()) {
            throw new IllegalStateException("QueryString이 존재하지 않습니다.");
        }
        int index = path.indexOf(QUERY_STRING_SEPARATOR);
        return path.substring(index + 1);
    }

    public Map<String, String> extractQueryParams() {
        String queryString = extractQueryString();
        String[] pieces = queryString.split(QUERY_STRING_PIECE_SEPARATOR);

        return Arrays.stream(pieces)
                .filter(this::isValidQueryStringPiece)
                .map(this::extractParam)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isValidQueryStringPiece(String queryStringPiece) {
        String[] keyAndValue = queryStringPiece.split(QUERY_STRING_PARAM_SEPARATOR);
        return keyAndValue.length == 2;
    }

    private Map.Entry<String, String> extractParam(String queryStringPiece) {
        int index = queryStringPiece.indexOf(QUERY_STRING_PARAM_SEPARATOR);
        String key = queryStringPiece.substring(0, index);
        String value = queryStringPiece.substring(index + 1);
        return new AbstractMap.SimpleEntry<>(key, value);
    }
}
