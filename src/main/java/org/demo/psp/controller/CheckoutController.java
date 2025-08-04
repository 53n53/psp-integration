package org.demo.psp.controller;

import org.apache.commons.validator.routines.UrlValidator;
import org.demo.psp.dto.CreateCheckoutRequest;
import org.demo.psp.dto.CreateCheckoutResponse;
import org.demo.psp.service.PaymentService;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import io.quarkus.logging.Log;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/checkouts")
public class CheckoutController {

    private final PaymentService paymentService;

    public CheckoutController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private static final UrlValidator URL_VALIDATOR = new UrlValidator();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<CreateCheckoutResponse> createCheckout(final CreateCheckoutRequest request) {
        Log.infof("Received create checkout request: %s", request.toString());

        if (isNotValidUrl(request.successUrl())) {
            throw new BadRequestException("Invalid success url.");
        }
        if (isNotValidUrl(request.cancelUrl())) {
            throw new BadRequestException("Invalid cancel url.");
        }
        if (request.productEnumDTO() == null) {
            throw new BadRequestException("The product field cannot be null.");
        }

        final CreateCheckoutResponse response = paymentService.createCheckout(request);
        Log.debugf("Create checkout response: %s", response.toString());

        return ResponseBuilder.ok(response).build();
    }

    private static boolean isNotValidUrl(final String url) {
        return !URL_VALIDATOR.isValid(url);
    }

}
