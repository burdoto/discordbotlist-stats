package de.kaleidox.botstats;

public enum Scope {
    /**
     * Number of servers the bot is in.
     */
    SERVER_COUNT,

    /**
     * Array of {@code shardId -> server count}.
     */
    SHARD_ARRAY,

    /**
     * ID of the shard sharing those informations.
     */
    SHARD_ID,

    /**
     * Number of the shards that the bot has.
     */
    SHARD_COUNT,

    /**
     * Number of all users that the bot can see.
     */
    USER_COUNT,

    /**
     * Number of all active voice connections that the bot has open.
     */
    VOICE_CONNECTION_COUNT
}
