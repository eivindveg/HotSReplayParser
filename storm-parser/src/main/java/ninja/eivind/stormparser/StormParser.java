package ninja.eivind.stormparser;

import ninja.eivind.mpq.MpqArchive;
import ninja.eivind.stormparser.builders.PlayerBuilder;
import ninja.eivind.stormparser.builders.ReplayBuilder;
import ninja.eivind.stormparser.builders.TrackerEventStructureBuilder;
import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.models.Replay;
import ninja.eivind.stormparser.models.TrackerEventStructure;
import ninja.eivind.stormparser.readers.BitReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Eivind Vegsundvåg
 */
public class StormParser {
    public static final String REPLAY_DETAILS = "replay.details";
    public static final String REPLAY_INIT_DATA = "replay.initData";
    private File file;

    public StormParser(File file) {
        this.file = file;
    }

    public Replay parseReplay() {
        MpqArchive archive = new MpqArchive(file);

        try {
            Map<String, ByteBuffer> files = archive.getFiles(REPLAY_DETAILS, REPLAY_INIT_DATA);

            return new ReplayBuilder(files).build();
        } catch (IOException e) {
            throw new MpqException("Could not parse file", e);
        }
    }
}
