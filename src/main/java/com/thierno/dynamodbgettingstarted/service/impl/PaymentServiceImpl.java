package com.thierno.dynamodbgettingstarted.service.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.thierno.dynamodbgettingstarted.dto.PaymentRequest;
import com.thierno.dynamodbgettingstarted.dto.PaymentResponse;
import com.thierno.dynamodbgettingstarted.dto.PaymentResult;
import com.thierno.dynamodbgettingstarted.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService
{
	private final DynamoDBMapper dynamoDBMapper;

	public PaymentServiceImpl(DynamoDBMapper dynamoDBMapper)
	{
		this.dynamoDBMapper = dynamoDBMapper;
	}

	@Override
	public PaymentRequest save(PaymentRequest paymentRequest)
	{
		dynamoDBMapper.save(paymentRequest);
		return paymentRequest;
	}

	@Override
	public PaymentRequest getById(String id)
	{
		return dynamoDBMapper.load(PaymentRequest.class, id);
	}

	@Override
	public PaymentRequest getByOrderId(String orderId)
	{
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":v_order_id", new AttributeValue().withS(orderId));

		DynamoDBQueryExpression<PaymentRequest> queryExpression = new DynamoDBQueryExpression<PaymentRequest>()
				.withIndexName("orderId-index")
				.withKeyConditionExpression("orderId = :v_order_id")
				.withConsistentRead(false)
				.withExpressionAttributeValues(eav);

		List<PaymentRequest> latestReplies = dynamoDBMapper.query(PaymentRequest.class, queryExpression);

		if (latestReplies.size() > 1)
		{
			throw new RuntimeException("More than one payment request found for order id: " + orderId);
		}

		return latestReplies.size() == 0 ? null : dynamoDBMapper.load(PaymentRequest.class, latestReplies.get(0).getId());
	}

	@Override
	public PaymentRequest update(PaymentRequest paymentRequest)
	{
		PaymentRequest pr = dynamoDBMapper.load(PaymentRequest.class, paymentRequest.getId());

		pr.setMethod(pr.getMethod() + "_updated");
		dynamoDBMapper.save(pr);

		return pr;
	}

	@Override
	public void delete(String id)
	{
		PaymentRequest pr = dynamoDBMapper.load(PaymentRequest.class, id);
		if (pr != null)
		{
			dynamoDBMapper.delete(pr);
		}
	}

	@Override
	public List<PaymentRequest> getAll()
	{
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		return dynamoDBMapper.scan(PaymentRequest.class, scanExpression);
	}

	@Override
	public PaymentRequest process(PaymentRequest pr)
	{
		pr.setResponse(PaymentResponse.builder()
				.amount(pr.getAmount())
				.currency(pr.getCurrency())
				.orderId(pr.getOrderId())
				.result(PaymentResult.ACCEPTED )
				.build()
		);

		return save(pr);
	}
}
