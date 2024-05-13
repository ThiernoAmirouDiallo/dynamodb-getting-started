package com.thierno.dynamodbgettingstarted.service;

import com.thierno.dynamodbgettingstarted.dto.PaymentRequest;
import com.thierno.dynamodbgettingstarted.dto.PaymentResponse;

import java.util.List;

public interface PaymentService
{
	PaymentRequest save(PaymentRequest paymentRequest);
	PaymentRequest getById(String id);
	PaymentRequest getByOrderId(String orderId);
	PaymentRequest update(PaymentRequest paymentRequest);
	PaymentRequest delete(String id);
	List<PaymentRequest> getAll();

	PaymentRequest process(PaymentRequest pr);
}
