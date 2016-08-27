package ninja.eivind.stormparser;

import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.models.Replay;
import ninja.eivind.stormparser.models.replaycomponents.ReplayDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Eivind Vegsundvåg
 */
public class StandaloneBattleLobbyParser implements Function<File, Replay> {

    private static final List<String> REGIONS = Arrays.asList(
            "US",
            "EU",
            "KR",
            "CN",
            "XX"
    );

    private final String REGIONS_REGEX = "s2mh..(" + String.join("|", REGIONS) + ")";
    private final Pattern PLAYERS_PATTERN = Pattern.compile("(\\p{L}|\\d){3,24}#\\d{4,10}[zØ]?");
    private final Pattern REGIONS_PATTERN = Pattern.compile(REGIONS_REGEX);

    @Override
    public Replay apply(File file) {
        Replay replay = new Replay();

        try {
            final byte[] data = Files.readAllBytes(file.toPath());
            final String dataString = new String(data, "UTF-8");
            System.out.println(dataString);
            final int battleNetRegionId = getRegionId(dataString);

            List<Player> players = getPlayers(dataString, battleNetRegionId);
            ReplayDetails replayDetails = new ReplayDetails();
            replayDetails.setPlayers(players);
            replay.setReplayDetails(replayDetails);

            return replay;
        } catch (IOException e) {
            throw new MpqException("Could not read file", e);
        }
    }

    private List<Player> getPlayers(String dataString, int battleNetRegionId) {
        final Matcher playersMatcher = PLAYERS_PATTERN.matcher(dataString);

        return convertMatcherToStream(playersMatcher)
                .map(string -> string.split("#"))
                .map(getPlayerFunction(battleNetRegionId))
                .collect(Collectors.toList());
    }

    private Function<String[], Player> getPlayerFunction(int battleNetRegionId) {
        return stringArray -> {
            Player player = new Player();
            player.setShortName(stringArray[0]);
            player.setBattleNetRegionId(battleNetRegionId);
            player.setBattleTag(getBattleTag(stringArray[1]));

            return player;
        };
    }

    private int getBattleTag(String stringToParse) {
        final char[] battleTagChars = stringToParse.toCharArray();
        final int battleTag;
        if(battleTagChars[battleTagChars.length-1] == 'z' || battleTagChars[battleTagChars.length - 1] == 'Ø') {
            battleTag = Integer.parseInt(stringToParse.substring(0, stringToParse.length() - 2));
        } else {
            battleTag = Integer.parseInt(stringToParse);
        }
        return battleTag;
    }

    private Stream<String> convertMatcherToStream(Matcher matcher) {
        Stream.Builder<String> builder = Stream.builder();
        while(matcher.find()) {
            builder.add(matcher.group());
        }
        return builder.build();
    }

    private int getRegionId(String data) {
        final Matcher matcher = REGIONS_PATTERN.matcher(data);
        if (matcher.find()) {
            final String firstMatch = matcher.group().substring(6);
            switch (firstMatch) {
                case "US":
                    return 1;
                case "EU":
                    return 2;
                case "KR":
                    return 3;
                case "CN":
                    return 5;
            }
        }
        return 99;
    }
}
