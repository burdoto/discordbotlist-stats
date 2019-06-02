package de.kaleidox.botstats.discord4j;

import java.util.List;

import de.kaleidox.botstats.endpoints.Scope;
import de.kaleidox.botstats.model.JsonFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import discord4j.core.DiscordClient;

public class Discord4JMultiShardedJsonFactory extends JsonFactory {
    private final List<DiscordClient> d4js;

    public Discord4JMultiShardedJsonFactory(List<DiscordClient> d4js) {
        super();
        this.d4js = d4js;
    }

    @Override
    public String getStatJson(Scope... scopes) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        for (Scope scope : scopes) {
            switch (scope) {
                case SERVER_COUNT:
                    final long serverCount = d4js.stream()
                            .mapToLong(d4j -> d4j.getGuilds().count().block())
                            .sum();

                    node.put("server_count", serverCount);
                    node.put("guildCount", serverCount);
                    node.put("guilds", serverCount);

                    break;
                case SHARD_COUNT:
                    final int shardCount = d4js.size();

                    node.put("shards", shardCount);
                    node.put("shardCount", shardCount);

                    break;
                case SHARD_ARRAY:
                    break;
                case SHARD_ID:
                    // don't define shard ID on multisharded factory

                    break;
                case USER_COUNT:
                    final long userCount = d4js.stream()
                            .mapToLong(d4j -> d4j.getUsers().count().block())
                            .sum();

                    node.put("users", userCount);

                    break;
                case VOICE_CONNECTION_COUNT:
                    final int voiceConnectionCount;

                    // todo: Unsupported by Discord4J in 3.0.6; field name: voice_connections

                    break;
                default:
                    throw new AssertionError("Unimplemented scope: " + scope);
            }
        }

        return node.toString();
    }
}
