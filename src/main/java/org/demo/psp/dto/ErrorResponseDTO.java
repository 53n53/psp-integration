package org.demo.psp.dto;

public record ErrorResponseDTO(
        int code,
        String status,
        String message
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int code;
        private String status;
        private String message;

        public Builder code(final int code) {
            this.code = code;
            return this;
        }

        public Builder status(final String status) {
            this.status = status;
            return this;
        }

        public Builder message(final String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseDTO build() {
            return new ErrorResponseDTO(code, status, message);
        }
    }

}
