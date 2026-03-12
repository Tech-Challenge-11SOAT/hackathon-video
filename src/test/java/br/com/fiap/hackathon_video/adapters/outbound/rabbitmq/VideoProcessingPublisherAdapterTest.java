package br.com.fiap.hackathon_video.adapters.outbound.rabbitmq;

import br.com.fiap.hackathon_video.application.ports.outbound.dto.VideoProcessingMessage;
import br.com.fiap.hackathon_video.infrastructure.config.RabbitMqProperties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoProcessingPublisherAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RabbitMqProperties rabbitMqProperties;

    @InjectMocks
    private VideoProcessingPublisherAdapter adapter;

    @Test
    void devePublicarMensagemComSucesso() {

        UUID videoId = UUID.randomUUID();
        VideoProcessingMessage message = VideoProcessingMessage.builder()
                .videoId(videoId)
                .build();

        String exchange = "video.exchange";
        String routingKey = "video.routing.key";

        when(rabbitMqProperties.getExchange()).thenReturn(exchange);
        when(rabbitMqProperties.getRoutingKey()).thenReturn(routingKey);

        adapter.publishVideoProcessingRequest(message);

        verify(rabbitTemplate).convertAndSend(exchange, routingKey, message);
    }
}