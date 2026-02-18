package br.com.fiap.hackathon_video.adapters.inbound.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import br.com.fiap.hackathon_video.adapters.inbound.dto.response.ErrorResponseDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.ErrorResponseDTO.ValidationError;
import br.com.fiap.hackathon_video.domain.exception.BusinessRuleException;
import br.com.fiap.hackathon_video.domain.exception.DomainException;
import br.com.fiap.hackathon_video.domain.exception.FileUploadException;
import br.com.fiap.hackathon_video.domain.exception.InvalidVideoException;
import br.com.fiap.hackathon_video.domain.exception.InvalidVideoFormatException;
import br.com.fiap.hackathon_video.domain.exception.ResourceNotFoundException;
import br.com.fiap.hackathon_video.domain.exception.S3UploadException;
import br.com.fiap.hackathon_video.domain.exception.UserNotFoundException;
import br.com.fiap.hackathon_video.domain.exception.VideoNotFoundException;
import br.com.fiap.hackathon_video.domain.exception.VideoProcessingException;
import br.com.fiap.hackathon_video.domain.exception.VideoSizeLimitExceededException;
import br.com.fiap.hackathon_video.infrastructure.security.exception.ExpiredTokenException;
import br.com.fiap.hackathon_video.infrastructure.security.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Trata exceções de vídeo não encontrado
	 * HTTP 404 - NOT FOUND
	 */
	@ExceptionHandler(VideoNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleVideoNotFoundException(
			VideoNotFoundException ex,
			WebRequest request) {

		log.error("Vídeo não encontrado: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error(HttpStatus.NOT_FOUND.getReasonPhrase())
				.message(ex.getMessage())
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	/**
	 * Trata exceções de usuário não encontrado
	 * HTTP 404 - NOT FOUND
	 */
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(
			UserNotFoundException ex,
			WebRequest request) {

		log.error("Usuário não encontrado: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error(HttpStatus.NOT_FOUND.getReasonPhrase())
				.message(ex.getMessage())
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	/**
	 * Trata exceções de recurso não encontrado
	 * HTTP 404 - NOT FOUND
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
			ResourceNotFoundException ex,
			WebRequest request) {

		log.error("Recurso não encontrado: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error(HttpStatus.NOT_FOUND.getReasonPhrase())
				.message(ex.getMessage())
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	/**
	 * Trata exceções de regra de negócio
	 * HTTP 422 - UNPROCESSABLE ENTITY
	 */
	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<ErrorResponseDTO> handleBusinessRuleException(
			BusinessRuleException ex,
			WebRequest request) {

		log.error("Violação de regra de negócio: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.UNPROCESSABLE_ENTITY.value())
				.error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
				.message(ex.getMessage())
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
	}

	/**
	 * Trata exceções de vídeo inválido
	 * HTTP 400 - BAD REQUEST
	 */
	@ExceptionHandler(InvalidVideoException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidVideoException(
			InvalidVideoException ex,
			WebRequest request) {

		log.error("Vídeo inválido: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	/**
	 * Trata exceções de formato de vídeo inválido
	 * HTTP 400 - BAD REQUEST
	 */
	@ExceptionHandler(InvalidVideoFormatException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidVideoFormatException(
			InvalidVideoFormatException ex,
			WebRequest request) {

		log.error("Formato de vídeo inválido: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.details("Formatos suportados: mp4, avi, mkv, mov, flv")
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	/**
	 * Trata exceções de tamanho de vídeo excedido
	 * HTTP 413 - PAYLOAD TOO LARGE
	 */
	@ExceptionHandler(VideoSizeLimitExceededException.class)
	public ResponseEntity<ErrorResponseDTO> handleVideoSizeLimitExceededException(
			VideoSizeLimitExceededException ex,
			WebRequest request) {

		log.error("Tamanho de vídeo excedido: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.PAYLOAD_TOO_LARGE.value())
				.error(HttpStatus.PAYLOAD_TOO_LARGE.getReasonPhrase())
				.message(ex.getMessage())
				.details("O tamanho máximo permitido para vídeos é 5GB")
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
	}

	/**
	 * Trata exceções de erro no upload para S3
	 * HTTP 500 - INTERNAL SERVER ERROR
	 */
	@ExceptionHandler(S3UploadException.class)
	public ResponseEntity<ErrorResponseDTO> handleS3UploadException(
			S3UploadException ex,
			WebRequest request) {

		log.error("Erro ao fazer upload para S3: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.message("Erro ao fazer upload do arquivo")
				.details("Ocorreu um erro ao fazer upload para o servidor. Tente novamente mais tarde")
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	/**
	 * Trata exceções de processamento de vídeo
	 * HTTP 500 - INTERNAL SERVER ERROR
	 */
	@ExceptionHandler(VideoProcessingException.class)
	public ResponseEntity<ErrorResponseDTO> handleVideoProcessingException(
			VideoProcessingException ex,
			WebRequest request) {

		log.error("Erro ao processar vídeo: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.message(ex.getMessage())
				.details("Erro ao processar o vídeo. O administrador foi notificado")
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	/**
	 * Trata exceções de validação de campos
	 * HTTP 400 - BAD REQUEST
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> handleValidationException(
			MethodArgumentNotValidException ex,
			WebRequest request) {

		log.error("Erro de validação: {}", ex.getMessage());

		List<ValidationError> validationErrors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(this::mapFieldError)
				.collect(Collectors.toList());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message("Erro de validação dos campos")
				.path(getPath(request))
				.validationErrors(validationErrors)
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	/**
	 * Trata exceções de token inválido
	 * HTTP 401 - UNAUTHORIZED
	 */
	@ExceptionHandler({ InvalidTokenException.class, ExpiredTokenException.class })
	public ResponseEntity<ErrorResponseDTO> handleTokenException(
			RuntimeException ex,
			WebRequest request) {

		log.error("Erro de autenticação: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED.value())
				.error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
				.message(ex.getMessage())
				.details("Realize login novamente para obter um novo token")
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	/**
	 * Trata exceções de credenciais inválidas
	 * HTTP 401 - UNAUTHORIZED
	 */
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(
			BadCredentialsException ex,
			WebRequest request) {

		log.error("Credenciais inválidas: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED.value())
				.error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
				.message("Credenciais inválidas")
				.details("Verifique o usuário e senha")
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	/**
	 * Trata exceções de acesso negado
	 * HTTP 403 - FORBIDDEN
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(
			AccessDeniedException ex,
			WebRequest request) {

		log.error("Acesso negado: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.FORBIDDEN.value())
				.error(HttpStatus.FORBIDDEN.getReasonPhrase())
				.message("Acesso negado")
				.details("Você não tem permissão para acessar este recurso")
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

	/**
	 * Trata exceções de tamanho máximo de upload excedido
	 * HTTP 413 - PAYLOAD TOO LARGE
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponseDTO> handleMaxUploadSizeExceededException(
			MaxUploadSizeExceededException ex,
			WebRequest request) {

		log.error("Tamanho máximo de upload excedido: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.PAYLOAD_TOO_LARGE.value())
				.error(HttpStatus.PAYLOAD_TOO_LARGE.getReasonPhrase())
				.message("Arquivo muito grande")
				.details("O tamanho máximo permitido para upload foi excedido")
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
	}

	/**
	 * Trata todas as exceções de domínio não tratadas especificamente
	 * HTTP 400 - BAD REQUEST
	 */
	@ExceptionHandler(DomainException.class)
	public ResponseEntity<ErrorResponseDTO> handleDomainException(
			DomainException ex,
			WebRequest request) {

		log.error("Erro de domínio: {}", ex.getMessage());

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	/**
	 * Trata exceções genéricas não previstas
	 * HTTP 500 - INTERNAL SERVER ERROR
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGenericException(
			Exception ex,
			WebRequest request) {

		log.error("Erro interno do servidor: ", ex);

		ErrorResponseDTO error = ErrorResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.message("Erro interno do servidor")
				.details("Ocorreu um erro inesperado. Por favor, tente novamente mais tarde")
				.path(getPath(request))
				.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	/**
	 * Mapeia FieldError para ValidationError
	 */
	private ValidationError mapFieldError(FieldError fieldError) {
		return ValidationError.builder()
				.field(fieldError.getField())
				.message(fieldError.getDefaultMessage())
				.rejectedValue(fieldError.getRejectedValue())
				.build();
	}

	/**
	 * Extrai o path da requisição
	 */
	private String getPath(WebRequest request) {
		return request.getDescription(false).replace("uri=", "");
	}
}
