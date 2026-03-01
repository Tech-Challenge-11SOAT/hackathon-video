package br.com.fiap.hackathon_video.adapters.outbound.repositories;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaProcessingJobsEntity;
import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobs;
import br.com.fiap.hackathon_video.utils.mappers.ProcessingJobsMapper;
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
class ProcessingJobsRepositoryImplTest {

    @Mock
    private JpaProcessingJobsRepository jpaProcessingJobsRepository;

    @Mock
    private ProcessingJobsMapper processingJobsMapper;

    @InjectMocks
    private ProcessingJobsRepositoryImpl processingJobsRepositoryImpl;

    @Test
    void deveSalvarJobDeProcessamento() {

        ProcessingJobs domainJob = new ProcessingJobs();
        domainJob.setId(UUID.randomUUID());

        JpaProcessingJobsEntity jpaEntity = new JpaProcessingJobsEntity();
        jpaEntity.setId(domainJob.getId());

        when(processingJobsMapper.domainToJpa(domainJob)).thenReturn(jpaEntity);

        when(jpaProcessingJobsRepository.save(jpaEntity)).thenReturn(jpaEntity);
        when(processingJobsMapper.jpaToDomain(jpaEntity)).thenReturn(domainJob);

        ProcessingJobs savedJob = processingJobsRepositoryImpl.save(domainJob);

        assertThat(savedJob).isNotNull();
        assertThat(savedJob.getId()).isEqualTo(domainJob.getId());

        verify(processingJobsMapper).domainToJpa(domainJob);
        verify(jpaProcessingJobsRepository).save(jpaEntity);
        verify(processingJobsMapper).jpaToDomain(jpaEntity);
    }

    @Test
    void deveEncontrarJobDeProcessamentoPorId() {

        UUID id = UUID.randomUUID();
        JpaProcessingJobsEntity jpaEntity = new JpaProcessingJobsEntity();
        jpaEntity.setId(id);
        ProcessingJobs domainJob = new ProcessingJobs();
        domainJob.setId(id);

        when(jpaProcessingJobsRepository.findById(id)).thenReturn(Optional.of(jpaEntity));
        when(processingJobsMapper.jpaToDomain(jpaEntity)).thenReturn(domainJob);

        ProcessingJobs foundJob = processingJobsRepositoryImpl.findById(id);

        assertThat(foundJob).isNotNull();
        assertThat(foundJob.getId()).isEqualTo(id);
        verify(jpaProcessingJobsRepository).findById(id);
        verify(processingJobsMapper).jpaToDomain(jpaEntity);
    }

    @Test
    void deveRetornarNullQuandoNaoEncontraJobDeProcessamentoPorId() {

        UUID id = UUID.randomUUID();
        when(jpaProcessingJobsRepository.findById(id)).thenReturn(Optional.empty());

        ProcessingJobs foundJob = processingJobsRepositoryImpl.findById(id);

        assertThat(foundJob).isNull();
        verify(jpaProcessingJobsRepository).findById(id);
        verify(processingJobsMapper, never()).jpaToDomain(any());
    }

    @Test
    void deveListarTodosJobsDeProcessamentoMapeados() {

        JpaProcessingJobsEntity entity1 = new JpaProcessingJobsEntity();
        JpaProcessingJobsEntity entity2 = new JpaProcessingJobsEntity();
        ProcessingJobs job1 = new ProcessingJobs();
        ProcessingJobs job2 = new ProcessingJobs();

        when(jpaProcessingJobsRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(processingJobsMapper.jpaToDomain(entity1)).thenReturn(job1);
        when(processingJobsMapper.jpaToDomain(entity2)).thenReturn(job2);

        List<ProcessingJobs> jobs = processingJobsRepositoryImpl.findAll();

        assertThat(jobs).hasSize(2);
        verify(jpaProcessingJobsRepository).findAll();
        verify(processingJobsMapper, times(2)).jpaToDomain(any(JpaProcessingJobsEntity.class));
    }

    @Test
    void deveDeletarJobDeProcessamentoPorId() {

        UUID id = UUID.randomUUID();

        processingJobsRepositoryImpl.deleteById(id);

        verify(jpaProcessingJobsRepository).deleteById(id);
    }
}