package com.thierno.dynamodbgettingstarted.controller;

import com.thierno.dynamodbgettingstarted.dto.PaymentRequest;
import com.thierno.dynamodbgettingstarted.dto.PaymentResponse;
import com.thierno.dynamodbgettingstarted.dto.PaymentResult;
import com.thierno.dynamodbgettingstarted.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("payment")
public class PaymentController
{

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

	@PostMapping("/request")
	public PaymentRequest postPaymentRequest(@RequestBody PaymentRequest pr)
	{
		return paymentService.process(pr);
	}

	@GetMapping("/request/{id}")
	public PaymentRequest getById(@PathVariable String id)
	{
		return paymentService.getById(id);
	}

	@GetMapping("/request")
	public PaymentRequest postPaymentRequest(@RequestParam("orderId") String orderId)
	{
		return paymentService.getByOrderId(orderId);
	}
}
