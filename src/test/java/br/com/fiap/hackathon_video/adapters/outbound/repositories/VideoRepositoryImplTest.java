package br.com.fiap.hackathon_video.adapters.outbound.repositories;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaVideoEntity;
import br.com.fiap.hackathon_video.domain.video.Video;
import br.com.fiap.hackathon_video.utils.mappers.VideoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoRepositoryImplTest {

    @Mock
    private JpaVideoRepository jpaVideoRepository;

    @Mock
    private VideoMapper videoMapper;

    @InjectMocks
    private VideoRepositoryImpl videoRepositoryImpl;

    @Test
    void deveSalvarOVideo() {

        Video domainVideo = new Video();
        domainVideo.setId(UUID.randomUUID());

        JpaVideoEntity jpaEntity = new JpaVideoEntity();
        jpaEntity.setId(domainVideo.getId());

        when(videoMapper.domainToJpa(domainVideo)).thenReturn(jpaEntity);
        when(jpaVideoRepository.save(jpaEntity)).thenReturn(jpaEntity);
        when(videoMapper.jpaToDomain(jpaEntity)).thenReturn(domainVideo);

        Video savedVideo = videoRepositoryImpl.save(domainVideo);

        assertThat(savedVideo).isNotNull();
        assertThat(savedVideo.getId()).isEqualTo(domainVideo.getId());

        verify(videoMapper).domainToJpa(domainVideo);
        verify(jpaVideoRepository).save(jpaEntity);
        verify(videoMapper).jpaToDomain(jpaEntity);
    }

    @Test
    void deveEncontrarOVideoPeloId() {

        UUID id = UUID.randomUUID();
        JpaVideoEntity jpaEntity = new JpaVideoEntity();
        jpaEntity.setId(id);
        Video domainVideo = new Video();
        domainVideo.setId(id);

        when(jpaVideoRepository.findById(id)).thenReturn(Optional.of(jpaEntity));
        when(videoMapper.jpaToDomain(jpaEntity)).thenReturn(domainVideo);

        Video foundVideo = videoRepositoryImpl.findById(id);

        assertThat(foundVideo).isNotNull();
        assertThat(foundVideo.getId()).isEqualTo(id);
        verify(jpaVideoRepository).findById(id);
    }

    @Test
    void deveRetornarNullQuandoOVideoNaoForEncontrado() {

        UUID id = UUID.randomUUID();

        when(jpaVideoRepository.findById(id)).thenReturn(Optional.empty());

        Video foundVideo = videoRepositoryImpl.findById(id);

        assertThat(foundVideo).isNull();
        verify(jpaVideoRepository).findById(id);

        verify(videoMapper, never()).jpaToDomain(any());
    }

    @Test

    void deveEncontrarTodosOsVideos() {

        JpaVideoEntity entity1 = new JpaVideoEntity();
        JpaVideoEntity entity2 = new JpaVideoEntity();
        Video video1 = new Video();
        Video video2 = new Video();

        when(jpaVideoRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(videoMapper.jpaToDomain(entity1)).thenReturn(video1);
        when(videoMapper.jpaToDomain(entity2)).thenReturn(video2);

        List<Video> videos = videoRepositoryImpl.findAll();

        assertThat(videos).hasSize(2);
        verify(jpaVideoRepository).findAll();

        verify(videoMapper, times(2)).jpaToDomain(any(JpaVideoEntity.class));
    }

    @Test
    void deveDeletarOVideoPeloId() {

        UUID id = UUID.randomUUID();

        videoRepositoryImpl.deleteById(id);

        verify(jpaVideoRepository).deleteById(id);
    }
}