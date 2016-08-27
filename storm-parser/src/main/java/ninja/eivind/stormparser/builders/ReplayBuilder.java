package ninja.eivind.stormparser.builders;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.appliers.AttributeEventsApplier;
import ninja.eivind.stormparser.builders.replaycomponents.AttributeEventsBuilder;
import ninja.eivind.stormparser.builders.replaycomponents.InitDataBuilder;
import ninja.eivind.stormparser.builders.replaycomponents.ReplayDetailsBuilder;
import ninja.eivind.stormparser.models.Replay;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import static ninja.eivind.stormparser.StormParser.*;

public class ReplayBuilder implements Builder<Replay> {

    private Map<String, ByteBuffer> mpqFiles;

    public ReplayBuilder(Map<String, ByteBuffer> mpqFiles) {

        this.mpqFiles = mpqFiles;
    }

    @Override
    public Replay build() throws IOException {
        Replay replay = new Replay();

        if (mpqFiles.containsKey(REPLAY_INIT_DATA)) {
            replay.setInitData(new InitDataBuilder(mpqFiles.get(REPLAY_INIT_DATA)).build());
        }
        if (mpqFiles.containsKey(REPLAY_DETAILS)) {
            replay.setReplayDetails(new ReplayDetailsBuilder(mpqFiles.get(REPLAY_DETAILS)).build());
        }

        if (mpqFiles.containsKey(REPLAY_ATTRIBUTES_EVENTS)) {
            new AttributeEventsApplier().apply(replay, new AttributeEventsBuilder(mpqFiles.get(REPLAY_ATTRIBUTES_EVENTS)).build());
        }

        return replay;
    }
}
