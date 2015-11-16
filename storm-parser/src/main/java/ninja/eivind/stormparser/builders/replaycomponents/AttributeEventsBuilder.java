package ninja.eivind.stormparser.builders.replaycomponents;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.models.replaycomponents.AttributeEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class AttributeEventsBuilder implements Builder<List<AttributeEvent>> {

    public AttributeEventsBuilder(ByteBuffer buffer) {

    }

    @Override
    public List<AttributeEvent> build() throws IOException {
        return null;
    }
}
