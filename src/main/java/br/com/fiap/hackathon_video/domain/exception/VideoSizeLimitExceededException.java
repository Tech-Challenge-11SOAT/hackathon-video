package br.com.fiap.hackathon_video.domain.exception;

/**
 * Exceção lançada quando o tamanho do vídeo excede o limite permitido
 * HTTP Status: 413 PAYLOAD TOO LARGE
 */
public class VideoSizeLimitExceededException extends FileUploadException {

	private static final long serialVersionUID = 1L;

	private static final long MAX_VIDEO_SIZE = 5_000_000_000L; // 5GB em bytes

	public VideoSizeLimitExceededException(long fileSize) {
		super(String.format(
				"Tamanho do arquivo excede o limite permitido. Tamanho: %s, Limite: %s",
				formatBytes(fileSize),
				formatBytes(MAX_VIDEO_SIZE)));
	}

	public VideoSizeLimitExceededException(String message) {
		super(message);
	}

	private static String formatBytes(long bytes) {
		if (bytes <= 0)
			return "0 B";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
		return String.format("%.2f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
	}
}
