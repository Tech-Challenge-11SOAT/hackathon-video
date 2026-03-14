package br.com.fiap.hackathon_video.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoResponseDTO;
import br.com.fiap.hackathon_video.application.ports.inbound.GetAuthenticatedUserUseCase;
import br.com.fiap.hackathon_video.application.ports.outbound.S3StoragePort;
import br.com.fiap.hackathon_video.application.ports.outbound.VideoProcessingPublisherPort;
import br.com.fiap.hackathon_video.application.usecases.ProcessingJobsUseCases;
import br.com.fiap.hackathon_video.domain.exception.InvalidVideoException;
import br.com.fiap.hackathon_video.domain.exception.InvalidVideoFormatException;
import br.com.fiap.hackathon_video.domain.video.Video;
import br.com.fiap.hackathon_video.domain.video.VideoRepository;

@ExtendWith(MockitoExtension.class)
class VideoServiceImplTest {

	@Mock
	private VideoRepository videoRepository;
	@Mock
	private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
	@Mock
	private S3StoragePort s3StoragePort;
	@Mock
	private VideoProcessingPublisherPort videoProcessingPublisherPort;
	@Mock
	private ProcessingJobsUseCases processingJobsUseCases;

	@InjectMocks
	private VideoServiceImpl videoService;

	@Captor
	private ArgumentCaptor<Video> videoCaptor;

	@Test
	void deveFazerUploadDoVideoComSucesso() {

		String userId = UUID.randomUUID().toString();

		MockMultipartFile file = new MockMultipartFile(
				"file", "meu_video.mp4", "video/mp4", "conteudo_fake_do_video".getBytes());
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(file, "Meu Título", "Minha Descrição");

		when(getAuthenticatedUserUseCase.getAuthenticatedUserId()).thenReturn(userId);

		Video savedVideo = new Video();
		savedVideo.setId(UUID.randomUUID());
		savedVideo.setUserId(UUID.fromString(userId));
		savedVideo.setOriginalFileName("meu_video.mp4");
		savedVideo.setS3VideoKey("videos/fake-key/meu_video.mp4");
		savedVideo.setCreatedAt(LocalDateTime.now());

		when(videoRepository.save(any(Video.class))).thenReturn(savedVideo);

		VideoResponseDTO response = videoService.uploadVideo(request);

		assertThat(response).isNotNull();
		assertThat(response.videoId()).isEqualTo(savedVideo.getId().toString());

		verify(s3StoragePort).uploadVideo(eq(file), anyString());

		verify(videoProcessingPublisherPort).publishVideoProcessingRequest(any());

		verify(videoRepository).save(videoCaptor.capture());
		Video capturedVideo = videoCaptor.getValue();
		assertThat(capturedVideo.getUserId().toString()).isEqualTo(userId);
		assertThat(capturedVideo.getOriginalFileName()).isEqualTo("meu_video.mp4");
	}

	@Test
	void deveLancarExcecaoQuandoFormatoForInvalido() {

		MockMultipartFile file = new MockMultipartFile(
				"file", "documento.pdf", "application/pdf", "conteudo fake".getBytes());
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(file, "Título", "Desc");

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoFormatException.class)
				.hasMessageContaining("Formato de arquivo inválido");

		verifyNoInteractions(s3StoragePort, videoRepository, videoProcessingPublisherPort,
				processingJobsUseCases);
	}

	@Test
	void deveCriarVideo() {
		Video video = new Video();
		video.setUserId(UUID.randomUUID());
		video.setOriginalFileName("teste.mp4");

		when(videoRepository.save(video)).thenReturn(video);

		Video result = videoService.createVideo(video);

		assertThat(result).isNotNull();
		verify(videoRepository).save(video);
	}

	@Test
	void deveFalharAoCriarVideoSemUserId() {
		Video video = new Video();
		video.setOriginalFileName("teste.mp4");

		assertThatThrownBy(() -> videoService.createVideo(video))
				.isInstanceOf(InvalidVideoException.class)
				.hasMessageContaining("UserId e OriginalFileName são obrigatórios");
	}

	@Test
	void deveLancarExcecaoQuandoArquivoForNulo() {
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(null, "Título", "Desc");

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoException.class)
				.hasMessageContaining("Arquivo de vídeo é obrigatório");
	}

	@Test
	void deveLancarExcecaoQuandoArquivoEstiverVazio() {
		MockMultipartFile emptyFile = new MockMultipartFile(
				"file", "video.mp4", "video/mp4", new byte[0] // array de bytes vazio
		);
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(emptyFile, "Título", "Desc");

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoException.class)
				.hasMessageContaining("Arquivo de vídeo é obrigatório");
	}

	@Test
	void deveLancarExcecaoQuandoONomeDoArquivoForNulo() {
		MockMultipartFile fileWithoutName = new MockMultipartFile(
				"file", null, "video/mp4", "conteudo".getBytes());
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(fileWithoutName, "Título", "Desc");

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoException.class)
				.hasMessageContaining("Nome do arquivo é inválido");
	}

	@Test
	void deveLancarExcecaoQuandoOArquivoExcederOLimite() {

		MockMultipartFile hugeFile = new MockMultipartFile("file", "video.mp4", "video/mp4",
				"conteudo".getBytes()) {
			@Override
			public long getSize() {
				return 5_000_000_001L; // 5GB + 1 byte
			}
		};
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(hugeFile, "Título", "Desc");

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoException.class)
				.hasMessageContaining("Arquivo muito grande");
	}

	@Test
	void deveLancarExcecaoQuandoOArquivoNaoTiverExtensao() {
		MockMultipartFile fileWithoutExtension = new MockMultipartFile(
				"file", "video_sem_ponto", "video/mp4", "conteudo".getBytes());
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(fileWithoutExtension, "Título", "Desc");

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoFormatException.class)
				.hasMessageContaining("Formato de arquivo inválido");
	}

	@Test
	void deveCairNoCathGenericoDeExceptionELancarInvalidVideoException() {

		MockMultipartFile file = new MockMultipartFile(
				"file", "video.mp4", "video/mp4", "conteudo".getBytes());
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(file, "Título", "Desc");

		when(getAuthenticatedUserUseCase.getAuthenticatedUserId()).thenReturn(UUID.randomUUID().toString());

		doThrow(new RuntimeException("Falha catastrófica no S3")).when(s3StoragePort).uploadVideo(any(),
				anyString());

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoException.class)
				.hasMessageContaining("Erro ao processar upload do vídeo");
	}

	@Test
	void deveLancarExcecaoQuandoOArquivoTerminarComPonto() {
		// Simula a condição lastDot == fileName.length() - 1
		MockMultipartFile fileEndingWithDot = new MockMultipartFile(
				"file", "meu_video.", "video/mp4", "conteudo".getBytes());
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(fileEndingWithDot, "Título", "Desc");

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoFormatException.class);
	}

	@Test
	void deveLancarExcecaoQuandoNomeDoArquivoForVazio() {
		MockMultipartFile fileWithEmptyName = new MockMultipartFile(
				"file", "", "video/mp4", "conteudo".getBytes());
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(fileWithEmptyName, "Título", "Desc");

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoException.class)
				.hasMessageContaining("Nome do arquivo é inválido");
	}

	@Test
	void deveFalharAoCriarVideoComNullFileName() {
		Video video = new Video();
		video.setUserId(UUID.randomUUID());
		video.setOriginalFileName(null);

		assertThatThrownBy(() -> videoService.createVideo(video))
				.isInstanceOf(InvalidVideoException.class)
				.hasMessageContaining("UserId e OriginalFileName são obrigatórios");
	}

	@Test
	void deveLancarExcecaoQuandoTamanhoDoArquivoEExplicitamenteZero() {
		MockMultipartFile weirdFile = new MockMultipartFile("file", "video.mp4", "video/mp4", new byte[0]) {
			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public long getSize() {
				return 0;
			}
		};
		VideoUploadRequestDTO request = new VideoUploadRequestDTO(weirdFile, "Título", "Desc");

		assertThatThrownBy(() -> videoService.uploadVideo(request))
				.isInstanceOf(InvalidVideoException.class)
				.hasMessageContaining("Arquivo está vazio");
	}
}