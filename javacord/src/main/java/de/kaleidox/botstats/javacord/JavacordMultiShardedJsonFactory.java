package de.kaleidox.botstats.javacord;

import java.util.List;

import de.kaleidox.botstats.endpoints.Scope;
import de.kaleidox.botstats.model.JsonFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;

public class JavacordMultiShardedJsonFactory extends JsonFactory {
    private final List<DiscordApi> apis;

    public JavacordMultiShardedJsonFactory(List<DiscordApi> apis) {
        super();
        this.apis = apis;
    }

    @Override
    public String getStatJson(Scope... scopes) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        for (Scope scope : scopes) {
            switch (scope) {
                case SERVER_COUNT:
                    final int serverCount = apis.stream()
                            .mapToInt(api -> api.getServers().size())
                            .sum();

                    node.put("server_count", serverCount);
                    node.put("guildCount", serverCount);
                    node.put("guilds", serverCount);

                    break;
                case SHARD_COUNT:
                    final int shardCount = apis.size();

                    node.put("shards", shardCount);
                    node.put("shardCount", shardCount);

                    break;
                case SHARD_ARRAY:
                    break;
                case SHARD_ID:
                    // don't define shard ID on multisharded factory

                    break;
                case USER_COUNT:
                    final int userCount = apis.stream()
                            .mapToInt(api -> api.getCachedUsers().size())
                            .sum();

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
