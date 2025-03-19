package hw6.integration.image.component;

public enum ImageUploadType {
    PROFILE("profiles"),
    POST("posts");

    private final String directoryName;

    ImageUploadType(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
