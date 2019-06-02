package de.kaleidox.botstats.jda;

import de.kaleidox.botstats.endpoints.Scope;
import de.kaleidox.botstats.model.JsonFactory;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.managers.AudioManager;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JDA implementation of the JsonFactory
 */
public class JDAJsonFactory extends JsonFactory {
    private final JDA jda;

    JDAJsonFactory(JDA jda) {
        super();
        this.jda = jda;
    }

    @Override
    public String getStatJson(Scope... scopes) {
        JSONObject node = new JSONObject();

        for (Scope scope : scopes) {
            @Nullable ShardManager shardManager = jda.asBot().getShardManager();

            switch (scope) {
                case SERVER_COUNT:
                    final int serverCount = shardManager == null
                            ? jda.getGuilds().size()
                            : shardManager.getGuilds().size();

                    node.put("server_count", serverCount);
                    node.put("guildCount", serverCount);
                    node.put("guilds", serverCount);

                    break;
                case SHARD_COUNT:
                    final int shardCount = shardManager == null
                            ? 1
                            : shardManager.getShardsTotal();

                    node.put("shards", shardCount);
                    node.put("shardCount", shardCount);

                    break;
                case SHARD_ARRAY:
                    JSONArray shards = new JSONArray();

                    if (shardManager == null) shards.put(jda.getGuilds().size());
                    else for (JDA shard : shardManager.getShards()) shards.put(shard.getGuilds().size());

                    node.put("shards", shards);

                    break;
                case SHARD_ID:
                    final int shardId = jda.getShardInfo().getShardId();

                    node.put("shard_id", shardId);
                    node.put("shardId", shardId);

                    break;
                case USER_COUNT:
                    final int userCount = shardManager == null
                            ? jda.getUsers().size()
                            : shardManager.getUsers().size();

                    node.put("users", userCount);

                    break;
                case VOICE_CONNECTION_COUNT:
                    final long voiceConnectionCount = jda.getAudioManagerCache()
                            .stream()
                            .filter(AudioManager::isConnected)
                            .count();

                    node.put("voice_connections", voiceConnectionCount);

                    break;
                default:
                    throw new AssertionError("Unimplemented scope: " + scope);
            }
        }

        return node.toString();
    }
}
