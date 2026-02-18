package br.com.fiap.hackathon_video.application.ports.inbound;

public interface GetAuthenticatedUserUseCase {

	/**
	 * Obtém o username do usuário autenticado
	 * 
	 * @return username do usuário ou null se não autenticado
	 */
	String getAuthenticatedUsername();

	/**
	 * Obtém o ID do usuário autenticado
	 * 
	 * @return ID do usuário ou null se não disponível
	 */
	String getAuthenticatedUserId();
}
