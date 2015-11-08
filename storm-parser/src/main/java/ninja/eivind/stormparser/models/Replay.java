package ninja.eivind.stormparser.models;

import ninja.eivind.stormparser.models.replaycomponents.InitData;
import ninja.eivind.stormparser.models.replaycomponents.ReplayDetails;

/**
 * @author Eivind Vegsundvåg
 */
public class Replay {
    private InitData initData;
    private ReplayDetails replayDetails;

    public void setInitData(InitData initData) {
        this.initData = initData;
    }


    public void setReplayDetails(ReplayDetails replayDetails) {
        this.replayDetails = replayDetails;
    }

    public InitData getInitData() {
        return initData;
    }
}
