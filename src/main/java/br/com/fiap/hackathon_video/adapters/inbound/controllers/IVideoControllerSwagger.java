package br.com.fiap.hackathon_video.adapters.inbound.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoResponseDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoWithProcessingStatusResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Videos", description = "Endpoints para gerenciamento de vídeos")
@SecurityRequirement(name = "Bearer Authentication")
public interface IVideoControllerSwagger {

	/**
	 * Realiza upload de um arquivo de vídeo
	 * 
	 * O arquivo é validado quanto a:
	 * - Formato (mp4, avi, mkv, mov, flv, webm, m4v)
	 * - Tamanho máximo (5GB)
	 * - Presença de conteúdo
	 * 
	 * O vídeo é persistido no banco de dados com status PENDING
	 * e fica aguardando processamento na fila de processamento
	 * 
	 * @param videoDTO Contém o arquivo de vídeo e metadados
	 * @return Resposta contendo o ID do vídeo criado
	 */
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Upload de vídeo", description = "Realiza o upload de um arquivo de vídeo. O arquivo é validado e persistido com status PENDING.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Vídeo aceito para processamento", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VideoResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Erro de validação do arquivo. Possíveis motivos:\n" +
					"- Arquivo vazio\n" +
					"- Nome do arquivo inválido\n" +
					"- Formato não suportado\n" +
					"- Arquivo muito grande (máximo 5GB)"),
			@ApiResponse(responseCode = "401", description = "Token JWT ausente, expirado ou inválido"),
			@ApiResponse(responseCode = "413", description = "Arquivo excede o tamanho máximo permitido (5GB)"),
			@ApiResponse(responseCode = "500", description = "Erro ao processar o upload ou salvar no banco de dados")
	})
	ResponseEntity<VideoResponseDTO> upload(@Valid @ModelAttribute VideoUploadRequestDTO videoDTO);

	/**
	 * Lista os videos do usuario autenticado com o status de processamento
	 *
	 * @return Lista de videos com status de processamento
	 */
	@GetMapping
	@Operation(summary = "Listar videos do usuario", description = "Retorna os videos do usuario autenticado com o status de processamento.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de videos retornada com sucesso", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VideoWithProcessingStatusResponseDTO.class))),
			@ApiResponse(responseCode = "401", description = "Token JWT ausente, expirado ou invalido"),
			@ApiResponse(responseCode = "500", description = "Erro ao buscar videos")
	})
	ResponseEntity<List<VideoWithProcessingStatusResponseDTO>> listUserVideos();
}
