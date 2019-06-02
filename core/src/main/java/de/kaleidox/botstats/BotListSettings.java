package de.kaleidox.botstats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * This object carries the settings for posting stats to bot lists.
 * <p>
 * It can be built with either an all-args constructor, or with the generated builder class.
 * <p>
 * If a token was set to {@code null}, no authenticated requests to that website will work.
 */
@Builder
@Getter
@AllArgsConstructor
public class BotListSettings {
    private final String discordbots_org_token;
    private final String discord_bots_gg_token;
    private final String discordbotlist_com_token;
    private final String divinediscordbots_com_token;
    private final String bots_ondiscord_xyz_token;

    /**
     * Returns how many tokens are actually defined.
     *
     * @return The count of defined tokens.
     */
    public int definedTokenCount() {
        int c = 0;

        if (discordbots_org_token != null) c++;
        if (discord_bots_gg_token != null) c++;
        if (discordbotlist_com_token != null) c++;
        if (divinediscordbots_com_token != null) c++;
        if (bots_ondiscord_xyz_token != null) c++;

        return c;
    }
}
