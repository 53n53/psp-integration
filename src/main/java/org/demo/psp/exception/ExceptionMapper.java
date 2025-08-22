package org.demo.psp.exception;

import org.demo.psp.dto.ErrorResponseDTO;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.fasterxml.jackson.databind.JsonMappingException;

import io.quarkus.logging.Log;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class ExceptionMapper {

    @ServerExceptionMapper
    public Response mapBadRequestException(final BadRequestException e) {
        return Response.status(Status.BAD_REQUEST)
                .entity(new ErrorResponseDTO()
                        .code(Status.BAD_REQUEST.getStatusCode())
                        .status(Status.BAD_REQUEST.getReasonPhrase())
                        .message(e.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response mapWebApplicationException(final WebApplicationException e) {
        String message = e.getMessage();

        // if an invalid enum value is sent to the API, a JsonMappingException wrapped inside a WebApplicationException will be thrown
        // the exception is built by EnumErrorCustomizer
        // extract the message and send it back to the caller
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof JsonMappingException jsonMappingException) {
                message = jsonMappingException.getOriginalMessage();
                break;
            }
            cause = cause.getCause();
        }

        return Response.status(Status.BAD_REQUEST)
                .entity(new ErrorResponseDTO()
                        .code(Status.BAD_REQUEST.getStatusCode())
                        .status(Status.BAD_REQUEST.getReasonPhrase())
                        .message(message))
                .build();
    }

    @ServerExceptionMapper
    public Response mapInternalServerErrorException(final InternalServerErrorException e) {
        return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponseDTO()
                        .code(Status.INTERNAL_SERVER_ERROR.getStatusCode())
                        .status(Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message(e.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response mapThrowable(final Throwable e) {
        Log.error("Unexpected error.", e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponseDTO()
                        .code(Status.INTERNAL_SERVER_ERROR.getStatusCode())
                        .status(Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message(null))
                .build();
    }

}
