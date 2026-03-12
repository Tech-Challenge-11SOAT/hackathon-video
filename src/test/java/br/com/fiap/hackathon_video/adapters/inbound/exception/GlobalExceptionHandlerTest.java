package br.com.fiap.hackathon_video.adapters.inbound.exception;

import br.com.fiap.hackathon_video.adapters.inbound.dto.response.ErrorResponseDTO;
import br.com.fiap.hackathon_video.domain.exception.*;
import br.com.fiap.hackathon_video.infrastructure.security.exception.ExpiredTokenException;
import br.com.fiap.hackathon_video.infrastructure.security.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest request;

    @BeforeEach
    void setUp() {
        when(request.getDescription(false)).thenReturn("uri=/api/v1/test");
    }

    @Test
    void deveTratarNotFoundExceptions() {
        VideoNotFoundException videoEx = new VideoNotFoundException("Vídeo não encontrado");
        UserNotFoundException userEx = new UserNotFoundException("Usuário não encontrado");
        ResourceNotFoundException resourceEx = new ResourceNotFoundException("Recurso não encontrado");

        assertErrorResponse(exceptionHandler.handleVideoNotFoundException(videoEx, request), HttpStatus.NOT_FOUND, "Vídeo não encontrado");
        assertErrorResponse(exceptionHandler.handleUserNotFoundException(userEx, request), HttpStatus.NOT_FOUND, "Usuário não encontrado: Usuário não encontrado");
        assertErrorResponse(exceptionHandler.handleResourceNotFoundException(resourceEx, request), HttpStatus.NOT_FOUND, "Recurso não encontrado");
    }

    @Test
    void deveTratarBusinessRuleException() {
        BusinessRuleException ex = new BusinessRuleException("Erro de negócio");
        assertErrorResponse(exceptionHandler.handleBusinessRuleException(ex, request), HttpStatus.UNPROCESSABLE_ENTITY, "Erro de negócio");
    }

    @Test
    void deveTratarBadRequestExceptions() {
        InvalidVideoException invalidVideoEx = new InvalidVideoException("Vídeo inválido");
        DomainException domainEx = new DomainException("Erro de domínio genérico"){};

        assertErrorResponse(exceptionHandler.handleInvalidVideoException(invalidVideoEx, request), HttpStatus.BAD_REQUEST, "Vídeo inválido");
        assertErrorResponse(exceptionHandler.handleDomainException(domainEx, request), HttpStatus.BAD_REQUEST, "Erro de domínio genérico");
    }

    @Test
    void deveTratarInvalidVideoFormatException() {
        InvalidVideoFormatException ex = new InvalidVideoFormatException("Formato errado");
        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleInvalidVideoFormatException(ex, request);

        assertErrorResponse(response, HttpStatus.BAD_REQUEST, "Formato de arquivo inválido: Formato errado. Formatos aceitos: mp4, avi, mkv, mov, flv");
        assertThat(response.getBody().getDetails()).contains("Formatos suportados");
    }

    @Test
    void deveTratarPayloadTooLargeExceptions() {
        VideoSizeLimitExceededException videoSizeEx = new VideoSizeLimitExceededException("Tamanho excedido");
        MaxUploadSizeExceededException maxUploadEx = new MaxUploadSizeExceededException(5000L);

        ResponseEntity<ErrorResponseDTO> response1 = exceptionHandler.handleVideoSizeLimitExceededException(videoSizeEx, request);
        assertErrorResponse(response1, HttpStatus.PAYLOAD_TOO_LARGE, "Tamanho excedido");

        ResponseEntity<ErrorResponseDTO> response2 = exceptionHandler.handleMaxUploadSizeExceededException(maxUploadEx, request);
        assertErrorResponse(response2, HttpStatus.PAYLOAD_TOO_LARGE, "Arquivo muito grande");
    }

    @Test
    void deveTratarInternalServerErrors() {
        S3UploadException s3Ex = new S3UploadException("Erro de S3");
        VideoProcessingException procEx = new VideoProcessingException("Erro no processamento");
        Exception genericEx = new Exception("Erro catastrófico");

        assertErrorResponse(exceptionHandler.handleS3UploadException(s3Ex, request), HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao fazer upload do arquivo");
        assertErrorResponse(exceptionHandler.handleVideoProcessingException(procEx, request), HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar vídeo: Erro no processamento");
        assertErrorResponse(exceptionHandler.handleGenericException(genericEx, request), HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
    }

    @Test
    void deveTratarSecurityExceptions() {
        InvalidTokenException invalidTokenEx = new InvalidTokenException("Token inválido");
        ExpiredTokenException expiredTokenEx = new ExpiredTokenException("Token expirado");
        BadCredentialsException badCredentialsEx = new BadCredentialsException("Senha incorreta");
        AccessDeniedException accessEx = new AccessDeniedException("Acesso negado");

        assertErrorResponse(exceptionHandler.handleTokenException(invalidTokenEx, request), HttpStatus.UNAUTHORIZED, "Token inválido");
        assertErrorResponse(exceptionHandler.handleTokenException(expiredTokenEx, request), HttpStatus.UNAUTHORIZED, "Token expirado");
        assertErrorResponse(exceptionHandler.handleBadCredentialsException(badCredentialsEx, request), HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        assertErrorResponse(exceptionHandler.handleAccessDeniedException(accessEx, request), HttpStatus.FORBIDDEN, "Acesso negado");
    }

    @Test
    void deveTratarMethodArgumentNotValidException() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("videoUploadRequestDTO", "title", "valor_errado", false, null, null, "Título é obrigatório");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler.handleValidationException(ex, request);

        assertErrorResponse(response, HttpStatus.BAD_REQUEST, "Erro de validação dos campos");

        List<ErrorResponseDTO.ValidationError> errors = response.getBody().getValidationErrors();
        assertThat(errors).hasSize(1);
        assertThat(errors.get(0).getField()).isEqualTo("title");
        assertThat(errors.get(0).getMessage()).isEqualTo("Título é obrigatório");
        assertThat(errors.get(0).getRejectedValue()).isEqualTo("valor_errado");
    }

    private void assertErrorResponse(ResponseEntity<ErrorResponseDTO> response, HttpStatus expectedStatus, String expectedMessage) {
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);

        ErrorResponseDTO body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(body.getError()).isEqualTo(expectedStatus.getReasonPhrase());
        assertThat(body.getMessage()).isEqualTo(expectedMessage);
        assertThat(body.getPath()).isEqualTo("/api/v1/test");
        assertThat(body.getTimestamp()).isNotNull();
    }
}