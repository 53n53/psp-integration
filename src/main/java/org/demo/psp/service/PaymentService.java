package org.demo.psp.service;

import org.demo.psp.dto.CreateCheckoutRequestDTO;
import org.demo.psp.dto.CreateCheckoutResponseDTO;

import jakarta.ws.rs.InternalServerErrorException;

public interface PaymentService {

    CreateCheckoutResponseDTO createCheckout(CreateCheckoutRequestDTO request) throws InternalServerErrorException;

}
