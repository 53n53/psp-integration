package org.demo.psp;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.stream.Stream;

import org.demo.psp.dto.ProductEnumDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
class CheckoutControllerTest {

    @Test
    void createCheckout_success() {
        final String requestBody = "{" +
                "\"successUrl\":\"https://test.com\"," +
                "\"cancelUrl\":\"https://test.com\"," +
                "\"product\":\"" + ProductEnumDTO.CAFE_GRAIN_COLOMBIE_250 + "\"" +
                "}";

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
        .when()
                .post("/checkouts")
        .then()
                .body("redirectUrl", startsWith("https://checkout.stripe.com/c/pay/"));
    }

    @ParameterizedTest
    @MethodSource("createCheckout_invalidInputs_provider")
    void createCheckout_invalidInputs(final String successUrl, final String cancelUrl, final String productEnumDTO, final String errorMessage) {
        final String requestBody = "{" +
                "\"successUrl\":\"" + successUrl + "\"," +
                "\"cancelUrl\":\"" + cancelUrl + "\"," +
                "\"product\":\"" + productEnumDTO + "\"" +
                "}";

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
        .when()
                .post("/checkouts")
        .then()
                .body(is("{" +
                        "\"code\":400," +
                        "\"status\":\"Bad Request\"," +
                        "\"message\":\"" + errorMessage + "\"" +
                        "}"));
    }

    static Stream<Arguments> createCheckout_invalidInputs_provider() {
        final String productValues = Arrays.toString(ProductEnumDTO.values());
        return Stream.of(
                arguments("invalid.success.url", "https://valid.cancel.url.com", "CAFE_GRAIN_COLOMBIE_250", "Invalid success url."),
                arguments(null,                  "https://valid.cancel.url.com", "CAFE_GRAIN_COLOMBIE_250", "Invalid success url."),
                arguments("https://valid.success.url.com", "invalid.cancel.url", "CAFE_GRAIN_COLOMBIE_250", "Invalid cancel url."),
                arguments("https://valid.success.url.com", null,                 "CAFE_GRAIN_COLOMBIE_250", "Invalid cancel url."),
                arguments("https://valid.success.url.com", "https://valid.cancel.url.com", "something", "Invalid Product: 'something'. Allowed values: " +productValues),
                arguments("https://valid.success.url.com", "https://valid.cancel.url.com", null,        "Invalid Product: 'null'. Allowed values: " +productValues)
        );
    }

}
