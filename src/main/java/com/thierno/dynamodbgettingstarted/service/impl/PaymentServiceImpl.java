package com.thierno.dynamodbgettingstarted.service.impl;

import com.thierno.dynamodbgettingstarted.dto.PaymentRequest;
import com.thierno.dynamodbgettingstarted.dto.PaymentResponse;
import com.thierno.dynamodbgettingstarted.dto.PaymentResult;
import com.thierno.dynamodbgettingstarted.service.PaymentService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService
{
	private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

	public PaymentServiceImpl(DynamoDbEnhancedClient dynamoDbEnhancedClient)
	{
		this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
	}

	@Override
	public PaymentRequest save(PaymentRequest paymentRequest)
	{
		paymentRequest.setId(UUID.randomUUID().toString());
		getMappedTable(PaymentRequest.class).putItem(paymentRequest);
		return paymentRequest;
	}

	@Override
	public PaymentRequest getById(String id)
	{

		/*QueryConditional q = QueryConditional.keyEqualTo(Key.builder().partitionValue(id)
																 // .sortValue(accountId)
																 .build());
		return getMappedTable(PaymentRequest.class).query(q).items().stream().collect(Collectors.toList()).get(0);
		*/

		return getMappedTable(PaymentRequest.class).getItem(Key.builder().partitionValue(id).build());
	}

	@Override
	public PaymentRequest getByOrderId(String orderId)
	{
		DynamoDbIndex<PaymentRequest> orderIdIndex = getMappedTable(PaymentRequest.class).index("orderId-index");
		QueryConditional q = QueryConditional.keyEqualTo(Key.builder().partitionValue(orderId)
																 // .sortValue(accountId)
																 .build());

		Iterator<Page<PaymentRequest>> result = orderIdIndex.query(q).iterator();
		List<PaymentRequest> paymentRequests = new ArrayList<>();
		while (result.hasNext())
		{
			Page<PaymentRequest> userPage = result.next();
			paymentRequests.addAll(userPage.items());
		}

		if (paymentRequests.size() > 1)
		{
			throw new RuntimeException("More than one payment request found for order id: " + orderId);
		}

		return paymentRequests.size() == 0 ? null : getById(paymentRequests.get(0).getId());
	}

	@Override
	public PaymentRequest update(PaymentRequest paymentRequest)
	{
		PaymentRequest pr = getMappedTable(PaymentRequest.class).getItem(Key.builder().partitionValue(paymentRequest.getId()).build());

		pr.setMethod(pr.getMethod() + "_updated");
		getMappedTable(PaymentRequest.class).updateItem(b -> b.item(paymentRequest).ignoreNulls(true));

		return pr;
	}

	@Override
	public PaymentRequest delete(String id)
	{
		return getMappedTable(PaymentRequest.class).deleteItem(Key.builder().partitionValue(id).build());
	}

	@Override
	public List<PaymentRequest> getAll()
	{
		return getMappedTable(PaymentRequest.class).scan().items().stream().toList();
	}

	@Override
	public PaymentRequest process(PaymentRequest pr)
	{
		pr.setResponse(PaymentResponse.builder().amount(pr.getAmount()).currency(pr.getCurrency()).orderId(pr.getOrderId()).result(PaymentResult.ACCEPTED)
							   .build());

		return save(pr);
	}

	private <T> DynamoDbTable<T> getMappedTable(Class<T> type)
	{
		return dynamoDbEnhancedClient.table(type.getSimpleName(), TableSchema.fromBean(type));
	}
}
