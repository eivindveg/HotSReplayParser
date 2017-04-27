package ninja.eivind.stormparser.meta;

public class MetaInformation {
    private final String version;
    private final int majorVersion;
    private final int build;
    private final int frames;

    public MetaInformation(String version, int majorVersion, int build, int frames) {

        this.version = version;
        this.majorVersion = majorVersion;
        this.build = build;
        this.frames = frames;
    }

    public String getVersion() {
        return version;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getBuild() {
        return build;
    }

    public int getFrames() {
        return frames;
    }
}
