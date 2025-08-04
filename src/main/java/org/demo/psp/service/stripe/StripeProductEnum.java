package org.demo.psp.service.stripe;

public enum StripeProductEnum {
    CAFE_GRAIN_COLOMBIE_250("price_1Rpf2zJt3M82JSPEri5TePXu");

    private final String priceId;

    StripeProductEnum(final String priceId) {
        this.priceId = priceId;
    }

    String getPriceId() {
        return this.priceId;
    }
}
