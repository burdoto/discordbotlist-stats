package de.kaleidox.botstats.discord4j;

import de.kaleidox.botstats.endpoints.Scope;
import de.kaleidox.botstats.model.JsonFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import discord4j.core.DiscordClient;

/**
 * Discord4J implementation of the JsonFactory.
 */
public class Discord4JJsonFactory extends JsonFactory {
    private final DiscordClient d4j;

    Discord4JJsonFactory(DiscordClient d4j) {
        super();
        this.d4j = d4j;
    }

    @Override
    public String getStatJson(Scope... scopes) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        for (Scope scope : scopes) {
            switch (scope) {
                case SERVER_COUNT:
                    d4j.getGuilds().count().subscribe(serverCount -> {
                        node.put("server_count", serverCount);
                        node.put("guildCount", serverCount);
                        node.put("guilds", serverCount);
                    });

                    break;
                case SHARD_COUNT:
                    final int shardCount = d4j.getConfig().getShardCount();

                    node.put("shards", shardCount);
                    node.put("shardCount", shardCount);

                    break;
                case SHARD_ID:
                    final int shardId = d4j.getConfig().getShardIndex();

                    node.put("shard_id", shardId);
                    node.put("shardId", shardId);

                    break;
                case USER_COUNT:
                    d4j.getUsers().count().subscribe(userCount ->
                            node.put("users", userCount));

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
