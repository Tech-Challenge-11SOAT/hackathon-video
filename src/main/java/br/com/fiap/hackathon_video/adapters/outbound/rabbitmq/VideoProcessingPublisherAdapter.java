package br.com.fiap.hackathon_video.adapters.outbound.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import br.com.fiap.hackathon_video.application.ports.outbound.VideoProcessingPublisherPort;
import br.com.fiap.hackathon_video.application.ports.outbound.dto.VideoProcessingMessage;
import br.com.fiap.hackathon_video.infrastructure.config.RabbitMqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingPublisherAdapter implements VideoProcessingPublisherPort {

	private final RabbitTemplate rabbitTemplate;
	private final RabbitMqProperties rabbitMqProperties;

	@Override
	public void publishVideoProcessingRequest(VideoProcessingMessage message) {
		log.info("Publicando mensagem para processamento de vídeo: videoId={}, exchange={}, routingKey={}",
				message.getVideoId(), rabbitMqProperties.getExchange(), rabbitMqProperties.getRoutingKey());

		rabbitTemplate.convertAndSend(
				rabbitMqProperties.getExchange(),
				rabbitMqProperties.getRoutingKey(),
				message);
	}
}
