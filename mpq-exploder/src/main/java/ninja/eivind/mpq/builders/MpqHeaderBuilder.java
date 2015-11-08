package ninja.eivind.mpq.builders;

import ninja.eivind.mpq.MpqHeader;

import java.nio.ByteBuffer;

/**
 * @author Eivind Vegsundvåg
 */
public class MpqHeaderBuilder implements Builder<MpqHeader> {
    private final ByteBuffer buffer;
    private final int length;
    private final int position;

    public MpqHeaderBuilder(ByteBuffer buffer, int length, int position) {
        this.buffer = buffer;
        this.length = length;
        this.position = position;
    }


    @Override
    public MpqHeader build() {
        MpqHeader header = new MpqHeader();
        header.setPosition(position);
        header.setLength(length);
        header.setArchiveLength(Integer.toUnsignedLong(buffer.getInt()));
        header.setFormat(buffer.getShort());
        header.setBlockSize(buffer.getShort());
        header.setHashTablePosition(Integer.toUnsignedLong(buffer.getInt()));
        header.setBlockTablePosition(Integer.toUnsignedLong(buffer.getInt()));
        header.setNumberOfHashEntries(buffer.getInt());
        header.setNumberOfBlockEntries(buffer.getInt());

        if(header.getFormat() != 3) {
            throw new UnsupportedOperationException("Only Version 3 Headers are supported. Header format: " + header.getFormat());
        }

        return header;
    }
}
