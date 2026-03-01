package br.com.fiap.hackathon_video.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.assertj.core.api.Assertions.assertThat;

class AwsS3ConfigTest {

    @Test

    void shouldCreateS3Beans() {
        AwsS3Config config = new AwsS3Config();
        ReflectionTestUtils.setField(config, "awsRegion", "us-east-1");
        ReflectionTestUtils.setField(config, "accessKeyId", "fake-key");
        ReflectionTestUtils.setField(config, "secretAccessKey", "fake-secret");

        //Testa a branch com endpoint nulo
        ReflectionTestUtils.setField(config, "s3Endpoint", null);
        S3Client client1 = config.s3Client();
        S3Presigner presigner = config.s3Presigner();
        assertThat(client1).isNotNull();
        assertThat(presigner).isNotNull();

        //Testa a branch com endpoint vazio
        ReflectionTestUtils.setField(config, "s3Endpoint", "");
        S3Client client2 = config.s3Client();
        assertThat(client2).isNotNull();

        //Testa a branch com endpoint preenchido
        ReflectionTestUtils.setField(config, "s3Endpoint", "http://localhost:4566");
        S3Client client3 = config.s3Client();
        assertThat(client3).isNotNull();
    }
}