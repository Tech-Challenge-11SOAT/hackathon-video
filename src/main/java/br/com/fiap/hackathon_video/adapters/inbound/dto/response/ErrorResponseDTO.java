package br.com.fiap.hackathon_video.adapters.inbound.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {

	private LocalDateTime timestamp;
	private Integer status;
	private String error;
	private String message;
	private String details;
	private String path;
	private List<ValidationError> validationErrors;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ValidationError {
		private String field;
		private String message;
		private Object rejectedValue;
	}
}
