package de.kaleidox.botstats;

import java.util.function.Supplier;

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
    private @Builder.Default final Supplier<Boolean> postStatsTester = () -> true;
}
