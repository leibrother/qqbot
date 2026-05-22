package cc.rapidev.qqbot.message.model;

/**
 * @author leibrother
 */
public record MessageAttachmentGeneric(
        String id,
        String filename,
        String type,
        String url,
        int size,
        int width,
        int height,
        String wavUrl,
        String asrText
) {

    public boolean isImage() {
        return this.type.startsWith("image");
    }

    public boolean isVideo() {
        return this.type.startsWith("video");
    }

    public boolean isVoice() {
        return this.type.startsWith("voice");
    }

}
