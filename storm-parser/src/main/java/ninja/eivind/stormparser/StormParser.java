package ninja.eivind.stormparser;

import ninja.eivind.mpq.MpqArchive;
import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.stormparser.builders.ReplayBuilder;
import ninja.eivind.stormparser.models.Replay;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Eivind Vegsundvåg
 */
public class StormParser implements Function<File, Replay> {
    public static final String REPLAY_INIT_DATA = "replay.initData";
    public static final String REPLAY_DETAILS = "replay.details";
    public static final String REPLAY_ATTRIBUTES_EVENTS = "replay.attributes.events";

    @Override
    public Replay apply(File file) {
        MpqArchive archive = new MpqArchive(file);

        try {
            Map<String, ByteBuffer> files = archive.getFiles(REPLAY_DETAILS, REPLAY_INIT_DATA, REPLAY_ATTRIBUTES_EVENTS);

            return new ReplayBuilder(files).build();
        } catch (IOException e) {
            throw new MpqException("Could not parse file", e);
        }
    }
}
