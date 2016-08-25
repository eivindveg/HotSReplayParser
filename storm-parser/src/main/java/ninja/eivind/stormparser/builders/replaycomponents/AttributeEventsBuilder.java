package ninja.eivind.stormparser.builders.replaycomponents;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.models.replaycomponents.AttributeEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class AttributeEventsBuilder implements Builder<List<AttributeEvent>> {

    private ByteBuffer buffer;

    public AttributeEventsBuilder(ByteBuffer buffer) {
        this.buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    public List<AttributeEvent> build() throws IOException {
        final int headerSize = 5;

        final int numberOfAttributes = buffer.get(headerSize) & 0xFF;

        AttributeEvent[] attributes = new AttributeEvent[numberOfAttributes];

        int initialOffset = 4 + headerSize;
        for (int i = 0; i < attributes.length; i++) {
            int currentOffset = initialOffset + (i * 13);
            buffer.position(currentOffset);
            attributes[i] = new AttributeEventBuilder().apply(buffer);
        }

        return Arrays.asList(attributes);
    }
}
