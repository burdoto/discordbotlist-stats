package de.kaleidox.botstats;

import org.comroid.common.info.Dependent;
import org.comroid.common.ref.Named;
import org.comroid.restless.endpoint.AccessibleEndpoint;

import java.util.Optional;

public interface BotList extends Dependent<BotListConnection>, Named {
    AccessibleEndpoint[] getEndpoints();

    Optional<AccessibleEndpoint> getEndpoint(Scope scope);
}
