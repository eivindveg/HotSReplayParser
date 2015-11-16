package ninja.eivind.stormparser;

import ninja.eivind.mpq.MpqArchive;
import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.stormparser.builders.ReplayBuilder;
import ninja.eivind.stormparser.models.Replay;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author Eivind Vegsundvåg
 */
public class StormParser {
    public static final String REPLAY_INIT_DATA = "replay.initData";
    public static final String REPLAY_DETAILS = "replay.details";
    public static final String REPLAY_ATTRIBUTES_EVENTS = "replay.attributes.events";
    private File file;

    public StormParser(File file) {
        this.file = file;
    }

    public Replay parseReplay() {
        MpqArchive archive = new MpqArchive(file);

        try {
            Map<String, ByteBuffer> files = archive.getFiles(REPLAY_DETAILS, REPLAY_INIT_DATA, REPLAY_ATTRIBUTES_EVENTS);

            return new ReplayBuilder(files).build();
        } catch (IOException e) {
            throw new MpqException("Could not parse file", e);
        }
    }
}
