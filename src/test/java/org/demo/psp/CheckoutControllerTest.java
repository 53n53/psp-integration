package org.demo.psp;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.demo.psp.dto.CreateCheckoutRequest;
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
    void createCheckout_success () {
        final CreateCheckoutRequest requestBody = CreateCheckoutRequest.builder()
                .successUrl("https://test.com")
                .cancelUrl("https://test.com")
                .productEnumDTO(ProductEnumDTO.CAFE_GRAIN_COLOMBIE_250)
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
        .when()
                .post("/checkouts")
        .then()
                .body("url", startsWith("https://checkout.stripe.com/c/pay/"));
    }

    @ParameterizedTest
    @MethodSource("createCheckout_invalidInputs_provider")
    void createCheckout_invalidInputs (final String successUrl, final String cancelUrl, final ProductEnumDTO productEnumDTO, final String errorMessage) {
        final CreateCheckoutRequest requestBody = CreateCheckoutRequest.builder()
                .successUrl(successUrl)
                .cancelUrl(cancelUrl)
                .productEnumDTO(productEnumDTO)
                .build();

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
        return Stream.of(
                arguments("invalid.success.url", "https://valid.cancel.url.com", ProductEnumDTO.CAFE_GRAIN_COLOMBIE_250, "Invalid success url."),
                arguments("https://valid.success.url.com", "invalid.cancel.url", ProductEnumDTO.CAFE_GRAIN_COLOMBIE_250, "Invalid cancel url."),
                arguments("https://valid.success.url.com", "https://valid.cancel.url.com", null, "The product field cannot be null.")
        );
    }

}
