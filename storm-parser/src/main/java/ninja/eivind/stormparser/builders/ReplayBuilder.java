package ninja.eivind.stormparser.builders;

import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.stormparser.appliers.AttributeEventsApplier;
import ninja.eivind.stormparser.builders.replaycomponents.AttributeEventsBuilder;
import ninja.eivind.stormparser.builders.replaycomponents.InitDataBuilder;
import ninja.eivind.stormparser.builders.replaycomponents.ReplayDetailsBuilder;
import ninja.eivind.stormparser.meta.MetaInformation;
import ninja.eivind.stormparser.models.Replay;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.function.BiFunction;

import static ninja.eivind.stormparser.StormParser.*;

public class ReplayBuilder implements BiFunction<MetaInformation, Map<String, ByteBuffer>, Replay> {


    @Override
    public Replay apply(MetaInformation metaInformation, Map<String, ByteBuffer> mpqFiles) {
        try {
            Replay replay = new Replay();

            if (mpqFiles.containsKey(REPLAY_INIT_DATA)) {
                replay.setInitData(new InitDataBuilder().apply(metaInformation, mpqFiles.get(REPLAY_INIT_DATA)));
            }
            if (mpqFiles.containsKey(REPLAY_DETAILS)) {
                replay.setReplayDetails(new ReplayDetailsBuilder(mpqFiles.get(REPLAY_DETAILS)).build());
            }

            if (mpqFiles.containsKey(REPLAY_ATTRIBUTES_EVENTS)) {
                new AttributeEventsApplier().apply(replay, new AttributeEventsBuilder(mpqFiles.get(REPLAY_ATTRIBUTES_EVENTS)).build());
            }

            replay.setMetaInformation(metaInformation);

            return replay;
        } catch (IOException e) {
            throw new MpqException("Failed to parse replay", e);
        }
    }
}
