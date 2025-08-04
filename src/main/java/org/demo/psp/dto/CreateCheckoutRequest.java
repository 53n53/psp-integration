package org.demo.psp.dto;

public record CreateCheckoutRequest(
        String successUrl,
        String cancelUrl,
        ProductEnumDTO productEnumDTO
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String successUrl;
        private String cancelUrl;
        private ProductEnumDTO productEnumDTO;

        public Builder successUrl(final String successUrl) {
            this.successUrl = successUrl;
            return this;
        }

        public Builder cancelUrl(final String cancelUrl) {
            this.cancelUrl = cancelUrl;
            return this;
        }

        public Builder productEnumDTO(final ProductEnumDTO productEnumDTO) {
            this.productEnumDTO = productEnumDTO;
            return this;
        }

        public CreateCheckoutRequest build() {
            return new CreateCheckoutRequest(successUrl, cancelUrl, productEnumDTO);
        }
    }

}
