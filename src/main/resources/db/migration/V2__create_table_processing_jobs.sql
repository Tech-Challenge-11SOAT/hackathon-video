CREATE TABLE processing_jobs (
    id BINARY(16) NOT NULL,
    video_id BINARY(16) NOT NULL,

    s3_video_key VARCHAR(500) NOT NULL,
    s3_zip_key VARCHAR(500) NULL,

    status VARCHAR(20) NOT NULL,
    frame_count INT NULL,
    error_message VARCHAR(1000) NULL,

    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_processing_jobs PRIMARY KEY (id),
    CONSTRAINT fk_processing_jobs_video_id FOREIGN KEY (video_id) REFERENCES videos(id),
    CONSTRAINT chk_processing_jobs_status CHECK (status IN ('PENDING','PROCESSING','COMPLETED','FAILED'))
);

CREATE INDEX idx_processing_jobs_video_id ON processing_jobs(video_id);
CREATE INDEX idx_processing_jobs_status ON processing_jobs(status);
CREATE INDEX idx_processing_jobs_created_at ON processing_jobs(created_at);
