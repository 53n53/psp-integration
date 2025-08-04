package org.demo.psp.service.stripe;

import org.demo.psp.dto.CreateCheckoutRequest;
import org.demo.psp.dto.CreateCheckoutResponse;
import org.demo.psp.service.PaymentService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StripeService implements PaymentService {

    @ConfigProperty(name = "STRIPE_API_SECRET")
    private String apiKey;

    private StripeClient stripeClient;

    @PostConstruct
    void init() {
        stripeClient = new StripeClient(apiKey);
    }

    public CreateCheckoutResponse createCheckout(final CreateCheckoutRequest request) {

        final StripeProductEnum stripeProductEnum = StripeProductEnum.valueOf(request.productEnumDTO().name());

        final SessionCreateParams sessionCreateParams = new SessionCreateParams.Builder()
                .setSuccessUrl(request.successUrl())
                .setCancelUrl(request.cancelUrl())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice(stripeProductEnum.getPriceId())
                        .build())
                .build();

        try {
            final Session checkoutSession = stripeClient.checkout().sessions().create(sessionCreateParams);

            return CreateCheckoutResponse.builder()
                    .url(checkoutSession.getUrl())
                    .build();
        } catch (final StripeException e) {
            Log.error("Failed to create a Stripe checkout session.", e);
            return new CreateCheckoutResponse.Builder().build();
        }
    }

}
