package de.kaleidox.botstats.model;

import de.kaleidox.botstats.BotListConnection;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;
import org.comroid.varbind.container.DataContainerBuilder;

public interface StatsRequestBody extends DataContainer<BotListConnection> {
    final class Builder extends DataContainerBuilder<Builder, StatsRequestBody, BotListConnection> {
        @Override
        protected StatsRequestBody mergeVarCarrier(DataContainer<BotListConnection> dataContainer) {
            return null;
        }
    }

    interface Bind {
        GroupBind<StatsRequestBody, BotListConnection> Root
                = new GroupBind<>(BotListConnection.SERIALIZATION, "stats");
    }
}
