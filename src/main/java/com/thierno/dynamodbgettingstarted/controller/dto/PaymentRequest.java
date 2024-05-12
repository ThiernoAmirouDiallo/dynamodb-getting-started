package com.thierno.dynamodbgettingstarted.controller.dto;

import lombok.Data;

@Data
public class PaymentRequest
{
	private String orderId;
	private String amount;
	private String currency;
	private String method;
}
