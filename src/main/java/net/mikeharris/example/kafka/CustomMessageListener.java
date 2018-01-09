package net.mikeharris.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

public class CustomMessageListener implements MessageListener<Integer, String> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private int messageCount = 0;

	@Override
	public void onMessage(ConsumerRecord<Integer, String> message) {
		logger.info("Custom message listener received: " + message);
		messageCount++;
	}

	public int getMessageCount() {
		return messageCount;
	}

}
