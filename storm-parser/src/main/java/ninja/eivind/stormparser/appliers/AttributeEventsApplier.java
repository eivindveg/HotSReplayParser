package ninja.eivind.stormparser.appliers;

import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.models.PlayerType;
import ninja.eivind.stormparser.models.Replay;
import ninja.eivind.stormparser.models.replaycomponents.AttributeEvent;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class AttributeEventsApplier {

    public void apply(Replay replay, List<AttributeEvent> build) {
        for (AttributeEvent attributeEvent : build) {
            if(attributeEvent == null || attributeEvent.getType() == null) {
                continue;
            }
            switch(attributeEvent.getType()) {
                case PLAYER_TYPE:
                    Player player = replay.getReplayDetails().getPlayers().get(attributeEvent.getPlayerId() - 1);
                    try {
                        String fromBytes = new String(attributeEvent.getValue(), "UTF-8").toLowerCase();
                        String type = reverseString(fromBytes);
                        switch (type) {
                            case "comp":
                                player.setPlayerType(PlayerType.COMPUTER);
                                break;
                            case "humn":
                                player.setPlayerType(PlayerType.PLAYER);
                                break;
                            default:
                                throw new UnsupportedOperationException("Unknown player type " + type);
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
            }
        }
    }

    private String reverseString(String fromBytes) {
        char[] chars = fromBytes.toCharArray();
        char[] reversed = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            reversed[i] = chars[chars.length - 1 - i];
        }
        return new String(reversed);
    }
}
