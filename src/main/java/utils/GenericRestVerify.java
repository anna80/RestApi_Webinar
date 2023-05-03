package utils;

import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;


public class GenericRestVerify {

    public static final String UNEXPECTED_STATUS_CODE_MSG = "Unexpected status code! Reason : ";

    private GenericRestVerify() {
    }

    public static void checkResponseCoedIs(Response response, final int responseCode) {
        assertThat(response.getStatusCode()).as(UNEXPECTED_STATUS_CODE_MSG +
                        String.format("%s.%n===STACKTRACE===%n%s", response.getStatusLine(), response.getContentType()))
                .isEqualTo(responseCode);
    }
}
