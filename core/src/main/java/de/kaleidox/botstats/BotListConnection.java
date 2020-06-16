package de.kaleidox.botstats;

import org.comroid.restless.HttpAdapter;
import org.comroid.uniform.SerializationAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class BotListConnection {
    public static SerializationAdapter<?,?,?> SERIALIZATION;
    public static HttpAdapter HTTP;

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
