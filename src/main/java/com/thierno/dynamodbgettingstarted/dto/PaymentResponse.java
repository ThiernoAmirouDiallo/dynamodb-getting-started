package com.thierno.dynamodbgettingstarted.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder(toBuilder = true)
@DynamoDBDocument
public class PaymentResponse
{
	@Tolerate
	public PaymentResponse()
	{
		// Default constructor used by AWS SDK
	}

	@DynamoDBAttribute
	private String orderId;

	@DynamoDBAttribute
	private String amount;

	@DynamoDBAttribute
	private String currency;

	@DynamoDBAttribute
	@DynamoDBTypeConvertedEnum
	private PaymentResult result;
}
