package io.pivotal;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ConsumerRunner implements CommandLineRunner {
	
	@Value("${sleepSeconds}")
	private Integer sleepSeconds;

	@Value("${queueName}")
	private String queueName;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	private Logger logger = Logger.getLogger(ConsumerRunner.class);
	
	public ConsumerRunner() {
		// TODO Auto-generated constructor stub
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		//rabbitTemplate.setQueue(queueName);
		return rabbitTemplate;
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		while(true){
			String msg = (String)rabbitTemplate.receiveAndConvert(queueName);
			logger.info("Received: " + msg);
			Thread.sleep(sleepSeconds*1000);
		}
	}

}
