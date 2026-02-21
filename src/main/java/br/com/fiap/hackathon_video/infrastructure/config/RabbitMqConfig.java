package br.com.fiap.hackathon_video.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

	@Bean
	public Queue videoProcessingQueue(RabbitMqProperties properties) {
		return new Queue(properties.getQueue(), true);
	}

	@Bean
	public DirectExchange videoProcessingExchange(RabbitMqProperties properties) {
		return new DirectExchange(properties.getExchange());
	}

	@Bean
	public Binding videoProcessingBinding(Queue videoProcessingQueue, DirectExchange videoProcessingExchange,
			RabbitMqProperties properties) {
		return BindingBuilder.bind(videoProcessingQueue)
				.to(videoProcessingExchange)
				.with(properties.getRoutingKey());
	}

	@Bean
	public MessageConverter messageConverter() {
		return new JacksonJsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter);
		return template;
	}
}
