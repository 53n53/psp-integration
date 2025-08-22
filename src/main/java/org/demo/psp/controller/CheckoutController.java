package org.demo.psp.controller;

import java.net.URI;

import org.apache.commons.validator.routines.UrlValidator;
import org.demo.psp.api.CheckoutsApi;
import org.demo.psp.dto.CreateCheckoutRequestDTO;
import org.demo.psp.dto.CreateCheckoutResponseDTO;
import org.demo.psp.service.PaymentService;

import io.quarkus.logging.Log;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/checkouts")
public class CheckoutController implements CheckoutsApi {

    private final PaymentService paymentService;

    public CheckoutController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private static final UrlValidator URL_VALIDATOR = new UrlValidator(new String[]{"http", "https"});

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCheckout(final CreateCheckoutRequestDTO request) {
        Log.infof("Received create checkout request: %s", request.toString());

        if (isNotValidUrl(request.getSuccessUrl())) {
            throw new BadRequestException("Invalid success url.");
        }
        if (isNotValidUrl(request.getCancelUrl())) {
            throw new BadRequestException("Invalid cancel url.");
        }
        // Invalid Product field is managed by the exception mapper.

        final CreateCheckoutResponseDTO response = paymentService.createCheckout(request);
        Log.debugf("Create checkout response: %s", response.toString());

        return Response.ok(response).build();
    }

    private static boolean isNotValidUrl(final URI uri) {
        return !URL_VALIDATOR.isValid(uri.toString());
    }

}
