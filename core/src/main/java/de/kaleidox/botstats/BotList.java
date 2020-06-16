package de.kaleidox.botstats;

import de.kaleidox.botstats.net.StatsRequestBody;
import org.comroid.common.info.Dependent;
import org.comroid.common.ref.Named;
import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.NotNull;

public interface BotList extends Dependent<BotListConnection>, Named {
    AccessibleEndpoint[] getEndpoints();

    @NotNull
    AccessibleEndpoint getStatsEndpoint();

    GroupBind<StatsRequestBody, BotListConnection> getStatsBind();
}
