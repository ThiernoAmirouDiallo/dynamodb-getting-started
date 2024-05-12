package com.thierno.dynamodbgettingstarted.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse
{
	private String orderId;
	private String amount;
	private String currency;
	private PaymentResult result;
}
