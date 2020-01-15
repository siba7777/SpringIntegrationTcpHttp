
package org.springframework.integration.samples.tcphttp;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CustomOrderTransformer {

	protected final Log logger = LogFactory.getLog(this.getClass());

	private static final int ORDER_NUMBER_LENGTH = 3;
	private static final int SENDER_NAME_LENGTH = 10;
	private static final int MESSAGE_LENGTH_LENGTH = 6;

	public CustomOrder transform(byte[] inputBytes) throws IOException {
		int orderNumber = parseOrderNumber(inputBytes);
		String senderName = parseSenderName(inputBytes);

		CustomOrder order = new CustomOrder(orderNumber, senderName);
		String message = parseMessage(inputBytes);
		order.setMessage(message);
		return order;
	}

	private String parseMessage(byte[] inputBytes) throws IOException {
		String lengthString = parseString(inputBytes, ORDER_NUMBER_LENGTH + SENDER_NAME_LENGTH, MESSAGE_LENGTH_LENGTH);
		int lengthOfMessage = Integer.valueOf(lengthString);

		String message = parseString(inputBytes, ORDER_NUMBER_LENGTH + SENDER_NAME_LENGTH + MESSAGE_LENGTH_LENGTH,
				lengthOfMessage);
		return message;
	}

	private String parseString(byte[] inputBytes, int length) throws IOException {
		return this.parseString(inputBytes, 0, length);
	}

	private String parseString(byte[] inputBytes, int start, int length) throws IOException {
		StringBuilder builder = new StringBuilder();

		int c;
		for (int i = start; i < start + length; ++i) {
			c = inputBytes[i];
			builder.append((char) c);
		}

		return builder.toString();
	}

	private String parseSenderName(byte[] inputBytes) throws IOException {
		return parseString(inputBytes, ORDER_NUMBER_LENGTH, SENDER_NAME_LENGTH);
	}

	private int parseOrderNumber(byte[] inputBytes) throws IOException {
		String value = parseString(inputBytes, ORDER_NUMBER_LENGTH);
		return Integer.valueOf(value.toString());
	}
}
