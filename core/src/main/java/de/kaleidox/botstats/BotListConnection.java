package de.kaleidox.botstats;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class BotListConnection {
    private final long botID;
    private final Set<BotList> supported;

    public BotListConnection(long botID, BotList... supported) {
        this.botID = botID;
        this.supported = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(supported)));
    }

    public Set<BotList> getSupported() {
        return supported;
    }
}
