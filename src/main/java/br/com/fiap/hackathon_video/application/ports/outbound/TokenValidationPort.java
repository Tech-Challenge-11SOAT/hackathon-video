package br.com.fiap.hackathon_video.application.ports.outbound;

public interface TokenValidationPort {

	/**
	 * Valida um token JWT
	 * 
	 * @param token o token a ser validado
	 * @return true se o token é válido, false caso contrário
	 */
	boolean validateToken(String token);

	/**
	 * Extrai o username do token
	 * 
	 * @param token o token JWT
	 * @return username contido no token
	 */
	String extractUsername(String token);

	/**
	 * Extrai o ID do usuário do token
	 * 
	 * @param token o token JWT
	 * @return ID do usuário contido no token
	 */
	String extractUserId(String token);
}
