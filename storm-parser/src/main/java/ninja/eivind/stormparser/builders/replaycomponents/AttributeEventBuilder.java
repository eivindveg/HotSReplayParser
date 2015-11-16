package ninja.eivind.stormparser.builders.replaycomponents;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.models.AttributeEventType;
import ninja.eivind.stormparser.models.replaycomponents.AttributeEvent;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Eivind Vegsundvåg
 */
public class AttributeEventBuilder implements Builder<AttributeEvent> {
    private final ByteBuffer buffer;

    public AttributeEventBuilder(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public AttributeEvent build() throws IOException {
        AttributeEvent attributeEvent = new AttributeEvent();

        attributeEvent.setHeader(buffer.getInt());
        int type = buffer.getInt();
        attributeEvent.setType(AttributeEventType.ofType(type));
        attributeEvent.setPlayerId(buffer.get());
        byte[] value = new byte[4];
        buffer.get(value, 0, value.length);
        attributeEvent.setValue(value);

        return attributeEvent;
    }
}
