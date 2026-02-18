CREATE TABLE videos (
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    
    original_filename VARCHAR(255) NOT NULL,
    s3_video_key VARCHAR(500) NOT NULL,
    s3_zip_key VARCHAR(500) NULL,
    
    status VARCHAR(20) NOT NULL,
    error_message VARCHAR(1000) NULL,
    
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_videos PRIMARY KEY (id),
    CONSTRAINT chk_videos_status CHECK (status IN ('PENDING','PROCESSING','COMPLETED','FAILED'))
);

CREATE INDEX idx_videos_user_id ON videos(user_id);
CREATE INDEX idx_videos_status ON videos(status);
CREATE INDEX idx_videos_created_at ON videos(created_at);
CREATE INDEX idx_videos_deleted ON videos(deleted);
