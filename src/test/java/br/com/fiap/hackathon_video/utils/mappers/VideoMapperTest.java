package br.com.fiap.hackathon_video.utils.mappers;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaVideoEntity;
import br.com.fiap.hackathon_video.domain.video.Video;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VideoMapperTest {

    private final VideoMapperImpl mapper = new VideoMapperImpl();

    @Test
    void shouldMapCorrectly() {
        assertThat(mapper.domainToJpa(null)).isNull();
        assertThat(mapper.jpaToDomain(null)).isNull();

        Video domain = new Video();
        JpaVideoEntity jpa = mapper.domainToJpa(domain);
        assertThat(jpa).isNotNull();

        JpaVideoEntity jpa2 = new JpaVideoEntity();
        Video domain2 = mapper.jpaToDomain(jpa2);
        assertThat(domain2).isNotNull();
    }
}