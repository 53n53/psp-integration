package org.demo.psp.dto;

public record CreateCheckoutResponse(
        String url
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String url;

        public Builder url(final String url) {
            this.url = url;
            return this;
        }

        public CreateCheckoutResponse build() {
            return new CreateCheckoutResponse(url);
        }
    }

}
