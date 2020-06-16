package de.kaleidox.botstats.endpoint;

import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.intellij.lang.annotations.Language;

public enum DiscordBotsGGEndpoint implements AccessibleEndpoint {
    STATS("/bots/%d/stats", "\\d+?");

    private final String extension;
    private final String[] regExGroups;

    @Override
    public String getUrlBase() {
        return "https://discord.bots.gg/api/v1";
    }

    @Override
    public String getUrlExtension() {
        return extension;
    }

    @Override
    public String[] getRegExpGroups() {
        return regExGroups;
    }

    DiscordBotsGGEndpoint(String extension, @Language("RegExp") String... regExGroups) {
        this.extension = extension;
        this.regExGroups = regExGroups;
    }
}
