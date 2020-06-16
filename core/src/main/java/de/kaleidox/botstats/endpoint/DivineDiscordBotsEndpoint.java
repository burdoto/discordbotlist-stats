package de.kaleidox.botstats.endpoint;

import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.intellij.lang.annotations.Language;

public enum DivineDiscordBotsEndpoint implements AccessibleEndpoint {
    STATS("/bots/%d/guilds", "\\d+?");

    private final String extension;
    private final String[] regExGroups;

    @Override
    public String getUrlBase() {
        return "https://bots.ondiscord.xyz/bot-api";
    }

    @Override
    public String getUrlExtension() {
        return extension;
    }

    @Override
    public String[] getRegExpGroups() {
        return regExGroups;
    }

    DivineDiscordBotsEndpoint(String extension, @Language("RegExp") String... regExGroups) {
        this.extension = extension;
        this.regExGroups = regExGroups;
    }
}
