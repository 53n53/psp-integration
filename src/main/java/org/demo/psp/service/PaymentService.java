package org.demo.psp.service;

import org.demo.psp.dto.CreateCheckoutRequest;
import org.demo.psp.dto.CreateCheckoutResponse;

public interface PaymentService {

    CreateCheckoutResponse createCheckout(CreateCheckoutRequest request);

}
