package de.kaleidox.botstats.catnip;

import java.util.Objects;

import de.kaleidox.botstats.BotListSettings;

import com.mewna.catnip.extension.AbstractExtension;
import com.mewna.catnip.extension.Extension;

/**
 * Catnip extension implementing functionality of the DiscordBotList-Stats library.
 */
public class CatnipStatsExtension extends AbstractExtension implements Extension {
    private final BotListSettings settings;

    private CatnipStatsClient client;

    /**
     * Constructor.
     * The defined {@linkplain BotListSettings settings object} can be modified itself to change tokens on-the-go.
     *
     * @param settings The settings object for initialization.
     */
    public CatnipStatsExtension(BotListSettings settings) {
        super("DiscordBotList-Stats (Catnip)");

        this.settings = Objects.requireNonNull(settings, "Cannot use " + name() + " without defined BotListSettings!");
    }

    @Override
    public void start() throws IllegalStateException {
        this.client = new CatnipStatsClient(settings, catnip());
    }

    @Override
    public void stop() {
        client.close();

        //noinspection ConstantConditions
        client = null;
    }
}
