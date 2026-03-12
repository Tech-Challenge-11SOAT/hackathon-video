package br.com.fiap.hackathon_video.adapters.outbound.s3;

import br.com.fiap.hackathon_video.domain.exception.S3UploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3StorageAdapterTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    @InjectMocks
    private S3StorageAdapter s3StorageAdapter;

    private final String BUCKET_NAME = "meu-bucket-teste";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3StorageAdapter, "bucketName", BUCKET_NAME);
    }

    @Test
    void deveFazerUploadComSucesso() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "conteudo".getBytes());
        String s3Key = "videos/video.mp4";

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        String result = s3StorageAdapter.uploadVideo(file, s3Key);

        assertThat(result).isEqualTo(s3Key);
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void deveLancarS3UploadExceptionQuandoOcorrerIOException() throws IOException {

        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException("Erro forçado de IO"));

        assertThatThrownBy(() -> s3StorageAdapter.uploadVideo(file, "chave"))
                .isInstanceOf(S3UploadException.class)
                .hasMessageContaining("Erro ao ler arquivo para upload no S3");
    }

    @Test
    void deveLancarS3UploadExceptionQuandoOcorrerErroNaAPIdoS3DuranteUpload() {
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "conteudo".getBytes());
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenThrow(new RuntimeException("S3 Offline"));

        assertThatThrownBy(() -> s3StorageAdapter.uploadVideo(file, "chave"))
                .isInstanceOf(S3UploadException.class)
                .hasMessageContaining("Erro ao fazer upload do vídeo para S3");
    }

    @Test
    void deveDeletarArquivoDoS3ComSucesso() {
        s3StorageAdapter.deleteFile("chave");
        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void deveLancarS3UploadExceptionAoFalharNaDelecao() {
        when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                .thenThrow(new RuntimeException("Sem permissão"));

        assertThatThrownBy(() -> s3StorageAdapter.deleteFile("chave"))
                .isInstanceOf(S3UploadException.class)
                .hasMessageContaining("Erro ao deletar arquivo do S3");
    }

    @Test
    void deveGerarURLPresignedComSucesso() throws MalformedURLException {

        String s3Key = "videos/video.mp4";
        PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);
        URL url = new URL("https://s3.amazonaws.com/meu-bucket/video.mp4");

        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);
        when(presignedRequest.url()).thenReturn(url);

        String result = s3StorageAdapter.generatePresignedUrl(s3Key, 60);

        assertThat(result).isEqualTo(url.toString());
        verify(s3Presigner).presignGetObject(any(GetObjectPresignRequest.class));
    }

    @Test
    void deveLancarS3UploadExceptionAoFalharNaGeracaoDaURL() {
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenThrow(new RuntimeException("Credenciais inválidas"));

        assertThatThrownBy(() -> s3StorageAdapter.generatePresignedUrl("chave", 60))
                .isInstanceOf(S3UploadException.class)
                .hasMessageContaining("Erro ao gerar URL de download");
    }
}