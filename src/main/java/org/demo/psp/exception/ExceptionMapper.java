package org.demo.psp.exception;

import org.demo.psp.dto.ErrorResponseDTO;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.RestResponse.Status;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import io.quarkus.logging.Log;
import jakarta.ws.rs.BadRequestException;

public class ExceptionMapper {

    @ServerExceptionMapper
    public RestResponse<ErrorResponseDTO> mapBadRequestException(final BadRequestException e) {
        return ResponseBuilder.create(Status.BAD_REQUEST,
                        ErrorResponseDTO.builder()
                                .code(Status.BAD_REQUEST.getStatusCode())
                                .status(Status.BAD_REQUEST.getReasonPhrase())
                                .message(e.getMessage())
                                .build())
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponseDTO> mapException(final Exception e) {
        Log.error("Unexpected error.", e);
        return ResponseBuilder.create(Status.INTERNAL_SERVER_ERROR,
                        ErrorResponseDTO.builder()
                                .code(Status.INTERNAL_SERVER_ERROR.getStatusCode())
                                .status(Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                .message(null)
                                .build())
                .build();
    }
}
