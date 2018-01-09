package net.mikeharris.example.kafka;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

	public static void main(String[] args) throws Exception {
		App obj = new App();
		obj.run();
	}

	private void run() throws Exception {

		String[] springConfig = { "kafka-producer-config.xml", "kafka-consumer-config.xml", "app-config.xml" };

		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

	}

}