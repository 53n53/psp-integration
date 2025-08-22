package org.demo.psp.service.stripe;

import java.net.URI;
import java.net.URISyntaxException;

import org.demo.psp.dto.CreateCheckoutRequestDTO;
import org.demo.psp.dto.CreateCheckoutResponseDTO;
import org.demo.psp.service.PaymentService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;

@ApplicationScoped
public class StripeService implements PaymentService {

    @ConfigProperty(name = "STRIPE_API_SECRET")
    private String apiKey;

    private StripeClient stripeClient;

    @PostConstruct
    void init() {
        stripeClient = new StripeClient(apiKey);
    }

    public CreateCheckoutResponseDTO createCheckout(final CreateCheckoutRequestDTO request) throws InternalServerErrorException {

        final StripeProductEnum stripeProductEnum = StripeProductEnum.valueOf(request.getProduct().name());

        final SessionCreateParams sessionCreateParams = new SessionCreateParams.Builder()
                .setSuccessUrl(request.getSuccessUrl().toString())
                .setCancelUrl(request.getCancelUrl().toString())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice(stripeProductEnum.getPriceId())
                        .build())
                .build();

        try {
            final Session checkoutSession = stripeClient.checkout().sessions().create(sessionCreateParams);

            return new CreateCheckoutResponseDTO()
                    .redirectUrl(new URI(checkoutSession.getUrl()));
        } catch (final StripeException e) {
            final String msg = "Stripe create checkout session service returned an error";
            Log.error(msg, e);
            throw new InternalServerErrorException(msg);
        } catch (final URISyntaxException e) {
            final String msg = "Stripe create checkout session service returned an invalid URL";
            Log.error(msg, e);
            throw new InternalServerErrorException(e);
        }
    }

}
