package ninja.eivind.stormparser.models;

import ninja.eivind.stormparser.meta.MetaInformation;
import ninja.eivind.stormparser.models.replaycomponents.InitData;
import ninja.eivind.stormparser.models.replaycomponents.ReplayDetails;

/**
 * @author Eivind Vegsundvåg
 */
public class Replay {
    private InitData initData;
    private ReplayDetails replayDetails;
    private MetaInformation metaInformation;

    public InitData getInitData() {
        return initData;
    }

    public void setInitData(InitData initData) {
        this.initData = initData;
    }

    public ReplayDetails getReplayDetails() {
        return replayDetails;
    }

    public void setReplayDetails(ReplayDetails replayDetails) {
        this.replayDetails = replayDetails;
    }

    public void setMetaInformation(MetaInformation metaInformation) {
        this.metaInformation = metaInformation;
    }

    public MetaInformation getMetaInformation() {
        return metaInformation;
    }
}
