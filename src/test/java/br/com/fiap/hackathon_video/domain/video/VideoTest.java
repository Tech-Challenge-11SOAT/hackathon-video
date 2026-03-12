package br.com.fiap.hackathon_video.domain.video;

import br.com.fiap.hackathon_video.domain.enums.EStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class VideoTest {

    @Test
    void testVideoGettersAndSetters() {

        Video video = new Video();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        video.setId(id);
        video.setUserId(userId);
        video.setOriginalFileName("meu_video.mp4");
        video.setS3VideoKey("videos/meu_video.mp4");
        video.setS3ZipKey("zips/meu_video.zip");
        video.setStatus(EStatus.PENDING);
        video.setErrorMessage("Sem erro");
        video.setCreatedAt(now);
        video.setUpdatedAt(now);

        assertThat(video.getId()).isEqualTo(id);
        assertThat(video.getUserId()).isEqualTo(userId);
        assertThat(video.getOriginalFileName()).isEqualTo("meu_video.mp4");
        assertThat(video.getS3VideoKey()).isEqualTo("videos/meu_video.mp4");
        assertThat(video.getS3ZipKey()).isEqualTo("zips/meu_video.zip");
        assertThat(video.getStatus()).isEqualTo(EStatus.PENDING);
        assertThat(video.getErrorMessage()).isEqualTo("Sem erro");
        assertThat(video.getCreatedAt()).isEqualTo(now);
        assertThat(video.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testVideoAllArgsConstructor() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Video video = new Video(id, userId, "video.mp4", "key/video.mp4", "key/video.zip",
                EStatus.COMPLETED, null, now, now);

        assertThat(video.getId()).isEqualTo(id);
        assertThat(video.getStatus()).isEqualTo(EStatus.COMPLETED);
        assertThat(video.getErrorMessage()).isNull();
    }
}