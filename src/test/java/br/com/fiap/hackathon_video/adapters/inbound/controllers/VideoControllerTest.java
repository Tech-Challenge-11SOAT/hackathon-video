package br.com.fiap.hackathon_video.adapters.inbound.controllers;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoResponseDTO;
import br.com.fiap.hackathon_video.application.ports.inbound.GetAuthenticatedUserUseCase;
import br.com.fiap.hackathon_video.application.usecases.VideoUseCases;
import br.com.fiap.hackathon_video.application.ports.outbound.TokenValidationPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VideoController.class)
@AutoConfigureMockMvc(addFilters = false)
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VideoUseCases videoUseCases;

    @MockitoBean
    private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @MockitoBean
    private TokenValidationPort tokenValidationPort;

    @Test
    void deveRetornarAcceptedQuandoUploadForComSucesso() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file", "meu_video.mp4", MediaType.MULTIPART_FORM_DATA_VALUE, "conteudo".getBytes()
        );

        String videoId = UUID.randomUUID().toString();
        VideoResponseDTO responseDTO = new VideoResponseDTO(videoId);

        when(getAuthenticatedUserUseCase.getAuthenticatedUsername()).thenReturn("teste_user");
        when(videoUseCases.uploadVideo(any(VideoUploadRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(multipart("/api/v1/videos/upload")
                        .file(file)
                        .param("title", "Meu Vídeo")
                        .param("description", "Descrição do meu vídeo")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.videoId").value(videoId));
    }

    @Test
    void deveRetornarBadRequestQuandoARequisicaoNaoPossuirOArquivo() throws Exception {

        mockMvc.perform(multipart("/api/v1/videos/upload")
                        .param("title", "Vídeo sem arquivo")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }
}