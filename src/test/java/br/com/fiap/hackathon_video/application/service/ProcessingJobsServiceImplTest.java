package br.com.fiap.hackathon_video.application.service;

import br.com.fiap.hackathon_video.domain.enums.EStatus;
import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobs;
import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessingJobsServiceImplTest {

    @Mock
    private ProcessingJobsRepository processingJobsRepository;

    @InjectMocks
    private ProcessingJobsServiceImpl processingJobsService;

    @Captor
    private ArgumentCaptor<ProcessingJobs> processingJobsCaptor;

    @Test
    void deveCriarProcessingJob() {

        UUID videoId = UUID.randomUUID();
        String s3VideoKey = "videos/meu_video.mp4";

        ProcessingJobs savedJob = new ProcessingJobs(UUID.randomUUID(), videoId, s3VideoKey, null, EStatus.PENDING, null, null, null, null, LocalDateTime.now());
        when(processingJobsRepository.save(any(ProcessingJobs.class))).thenReturn(savedJob);

        ProcessingJobs result = processingJobsService.createProcessingJob(videoId, s3VideoKey);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedJob.getId());

        verify(processingJobsRepository).save(processingJobsCaptor.capture());

        ProcessingJobs capturedJob = processingJobsCaptor.getValue();
        assertThat(capturedJob.getId()).isNotNull();
        assertThat(capturedJob.getVideoId()).isEqualTo(videoId);
        assertThat(capturedJob.getS3VideoKey()).isEqualTo(s3VideoKey);
        assertThat(capturedJob.getStatus()).isEqualTo(EStatus.PENDING);
        assertThat(capturedJob.getCreatedAt()).isNotNull();
    }
}