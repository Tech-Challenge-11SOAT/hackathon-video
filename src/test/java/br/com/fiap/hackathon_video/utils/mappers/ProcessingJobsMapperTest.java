package br.com.fiap.hackathon_video.utils.mappers;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaProcessingJobsEntity;
import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobs;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ProcessingJobsMapperTest {

    private final ProcessingJobsMapperImpl mapper = new ProcessingJobsMapperImpl();

    @Test
    void shouldMapCorrectly() {
        assertThat(mapper.domainToJpa(null)).isNull();
        assertThat(mapper.jpaToDomain(null)).isNull();

        ProcessingJobs domain = new ProcessingJobs();
        JpaProcessingJobsEntity jpa = mapper.domainToJpa(domain);
        assertThat(jpa).isNotNull();

        JpaProcessingJobsEntity jpa2 = new JpaProcessingJobsEntity();
        ProcessingJobs domain2 = mapper.jpaToDomain(jpa2);
        assertThat(domain2).isNotNull();
    }
}