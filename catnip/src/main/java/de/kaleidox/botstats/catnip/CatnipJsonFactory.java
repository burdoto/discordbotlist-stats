package de.kaleidox.botstats.catnip;

import de.kaleidox.botstats.endpoints.Scope;
import de.kaleidox.botstats.model.JsonFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mewna.catnip.Catnip;
import com.mewna.catnip.cache.EntityCache;
import com.mewna.catnip.shard.manager.ShardManager;

public class CatnipJsonFactory extends JsonFactory {
    private final Catnip api;
    CatnipStatsClient client;

    public CatnipJsonFactory(Catnip api) {
        super();

        this.api = api;
    }

    @Override
    public String getStatJson(Scope... scopes) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        for (Scope scope : scopes) {
            ShardManager shardManager = api.shardManager();
            EntityCache cache = api.cache();

            switch (scope) {
                case SERVER_COUNT:
                    final long serverCount = cache.guilds().count(any -> true);

                    node.put("server_count", serverCount);
                    node.put("guildCount", serverCount);
                    node.put("guilds", serverCount);

                    break;
                case SHARD_COUNT:
                    final int shardCount = shardManager.shardCount();

                    node.put("shards", shardCount);
                    node.put("shardCount", shardCount);

                    break;
                case SHARD_ARRAY:
                    // TODO catnip v1 does not support fetching guilds per-shard

                    break;
                case SHARD_ID:
                    final int shardId = 0; // catnip handles sharding automatically

                    node.put("shard_id", shardId);
                    node.put("shardId", shardId);

                    break;
                case USER_COUNT:
                    final long userCount = cache.users().count(any -> true);

                    node.put("users", userCount);

                    break;
                case VOICE_CONNECTION_COUNT:
                    final long finalOwnId = client.getOwnId();

                    long voiceConnectionCount = api.cache().voiceStates().count(vs -> vs.userIdAsLong() == finalOwnId);

                    node.put("voice_connections", voiceConnectionCount);

                    break;
                default:
                    throw new AssertionError("Unimplemented scope: " + scope);
            }
        }

        return node.toString();
    }
}
