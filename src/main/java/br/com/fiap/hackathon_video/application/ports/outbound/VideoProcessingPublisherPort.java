package br.com.fiap.hackathon_video.application.ports.outbound;

import br.com.fiap.hackathon_video.application.ports.outbound.dto.VideoProcessingMessage;

/**
 * Porta de saída para publicação de mensagens de processamento de vídeo
 */
public interface VideoProcessingPublisherPort {

	/**
	 * Publica mensagem para iniciar o processamento de vídeo
	 *
	 * @param message dados do vídeo a ser processado
	 */
	void publishVideoProcessingRequest(VideoProcessingMessage message);
}
