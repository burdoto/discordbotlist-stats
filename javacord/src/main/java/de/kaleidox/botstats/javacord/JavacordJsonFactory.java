package de.kaleidox.botstats.javacord;

import de.kaleidox.botstats.endpoints.Scope;
import de.kaleidox.botstats.model.JsonFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;

/**
 * Javacord implementation of the JsonFactory.
 */
public class JavacordJsonFactory extends JsonFactory {
    private final DiscordApi api;

    JavacordJsonFactory(DiscordApi api) {
        this.api = api;
    }

    @Override
    public String getStatJson(Scope... scopes) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        for (Scope scope : scopes) {
            switch (scope) {
                case SERVER_COUNT:
                    final int serverCount = api.getServers().size();

                    node.put("server_count", serverCount);
                    node.put("guildCount", serverCount);
                    node.put("guilds", serverCount);

                    break;
                case SHARD_COUNT:
                    final int shardCount = api.getTotalShards();

                    node.put("shards", shardCount);
                    node.put("shardCount", shardCount);

                    break;
                case SHARD_ARRAY:
                    break;
                case SHARD_ID:
                    final int shardId = api.getCurrentShard();

                    node.put("shard_id", shardId);
                    node.put("shardId", shardId);

                    break;
                case USER_COUNT:
                    final int userCount = api.getCachedUsers().size();

                    node.put("users", userCount);

                    break;
                case VOICE_CONNECTION_COUNT:
                    final int voiceConnectionCount;

                    // todo: Unsupported by Javacord in 3.0.4; field name: voice_connections

                    break;
                default:
                    throw new AssertionError("Unimplemented scope: " + scope);
            }
        }

        return node.toString();
    }
}
