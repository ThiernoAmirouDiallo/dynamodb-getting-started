package com.thierno.dynamodbgettingstarted.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@Builder(toBuilder = true)
@DynamoDbBean
public class PaymentResponse
{
	@Tolerate
	public PaymentResponse()
	{
		// Default constructor used by AWS SDK
	}

	private String orderId;

	private String amount;

	private String currency;

	private PaymentResult result;
}
