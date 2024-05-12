package com.thierno.dynamodbgettingstarted.controller;

import com.thierno.dynamodbgettingstarted.controller.dto.PaymentRequest;
import com.thierno.dynamodbgettingstarted.controller.dto.PaymentResponse;
import com.thierno.dynamodbgettingstarted.controller.dto.PaymentResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("payment")
public class PaymentController
{
	@PostMapping("/request")
	public PaymentResponse postPaymentRequest(@RequestBody PaymentRequest pr)
	{
		return PaymentResponse.builder()
				.amount(pr.getAmount())
				.currency(pr.getCurrency())
				.orderId(pr.getOrderId())
				.result(PaymentResult.ACCEPTED)
				.build();
	}

}
