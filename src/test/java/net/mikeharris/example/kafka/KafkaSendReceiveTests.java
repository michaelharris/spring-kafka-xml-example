package net.mikeharris.example.kafka;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration({ "file:src/main/resources/app-config.xml", "file:src/main/resources/kafka-consumer-config.xml",
		"file:src/main/resources/kafka-producer-config.xml" })
public class KafkaSendReceiveTests {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	KafkaTemplate<Integer, String> template;
	
	@Autowired(required = true)
	CustomMessageListener customMessageListener;

	@Test
	public void testSimpleSendReceive() throws Exception {

		Thread.sleep(1000); // wait a bit for the container to start

		template.setDefaultTopic("topic1");
		template.sendDefault(0, "foo");
		template.sendDefault(2, "bar");
		template.sendDefault(0, "baz");
		template.sendDefault(2, "qux");
		template.flush();
		Thread.sleep(10000);

		assertEquals(4, customMessageListener.getMessageCount());
		logger.info("End of test - count is " + customMessageListener.getMessageCount());
		

	}

}
