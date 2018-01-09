package net.mikeharris.example.kafka;



import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/*
 * This test doesn't seem to work - there may be an issue with embedded kafka on windows
 * https://issues.apache.org/jira/browse/KAFKA-6156
 */
//@PropertySource("file:src/test/resources/application.properties")
@RunWith(SpringRunner.class)
@ContextConfiguration({ "file:src/main/resources/app-config.xml", "file:src/main/resources/kafka-consumer-config.xml",
"file:src/main/resources/kafka-producer-config.xml" })
public class KafkaEmbeddedSendReceiveTests {

  protected final static String HELLOWORLD_TOPIC = "helloworld.t";

//  @Autowired
//  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  @ClassRule
  public static KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1, true, HELLOWORLD_TOPIC);

  @Before
  public void runBeforeTestMethod() throws Exception {
	kafkaEmbedded.setKafkaPorts(9092);
	
    // wait until all the partitions are assigned
	Thread.sleep(10000);
//    for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
//        .getListenerContainers()) {
//      ContainerTestUtils.waitForAssignment(messageListenerContainer,
//          kafkaEmbedded.getPartitionsPerTopic());
//    }
  }


	@BeforeClass
	public static void setup() {
		System.setProperty("spring.kafka.bootstrap-servers", kafkaEmbedded.getBrokersAsString());
		System.setProperty("spring.cloud.stream.kafka.binder.zkNodes", kafkaEmbedded.getZookeeperConnectionString());
	}

	@Autowired(required = true)
	KafkaTemplate<Integer, String> template;
	
	@Autowired(required = true)
	CustomMessageListener customMessageListener;

	@Test
	public void testSimpleSendReceive() throws Exception {
		System.out.println("thread sleeping");
		kafkaEmbedded.getKafkaServers().get(0).startup();
		
		
		Thread.sleep(10000); // wait a bit for the container to start

		template.setDefaultTopic("topic1");
		template.sendDefault(0, "foo");
		template.sendDefault(2, "bar");
		template.sendDefault(0, "baz");
		template.sendDefault(2, "qux");
		template.flush();
		Thread.sleep(10000);

		assertEquals(4, customMessageListener.getMessageCount());
	

	}
}
