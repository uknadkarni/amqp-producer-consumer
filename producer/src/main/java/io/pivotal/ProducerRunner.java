package io.pivotal;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ProducerRunner implements CommandLineRunner {

	@Value("${queueName}")
	private String queueName;

	@Value("${exchangeName}")
	private String exchangeName;
	
	@Value("${sleepSeconds}")
	private long sleepSeconds;
	
	private Logger logger = Logger.getLogger(ProducerRunner.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private static int count = 0;

	public ProducerRunner() {
		// TODO Auto-generated constructor stub
	}

	@Bean
	public Queue queue() {
		logger.info("Created queue: " + queueName);
		return new Queue(queueName);
	}

	@Bean
	public FanoutExchange exchange() {
		return new FanoutExchange(exchangeName);
	}

	@Bean
	public Binding binding(Queue queue, FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(exchangeName);
		return rabbitTemplate;
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		while (true) {
			String message = "Message: " + count++;
			rabbitTemplate.convertAndSend(message);
			logger.info("Sending..." + message);
			Thread.sleep(sleepSeconds*1000);
		}
	}

}
