# Discord BotList Stats [![Build Status](https://travis-ci.com/burdoto/discordbotlist-stats.svg?branch=master)](https://travis-ci.com/burdoto/discordbotlist-stats) [![Javadocs](http://javadoc.io/badge/de.kaleidox/discordbotlist-stats.svg)](http://javadoc.io/doc/de.kaleidox/discordbotlist-stats) [![Maven Central Release](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats) [![Development Release](https://jitpack.io/v/burdoto/discordbotlist-stats.svg)](https://jitpack.io/#burdoto/discordbotlist-stats)
Easy-to-use library for posting stats on most available Discord Bot list.

### Currently Supported:
- [discordbots.org](https://discordbots.org/)
- [discord.bots.gg](https://discord.bots.gg/)
- [discordbotslist.com](https://discordbotlist.com/)
- [divinediscordbots.com](https://divinediscordbots.com/)
- [bots.ondiscord.xyz](https://bots.ondiscord.xyz/)


## Importing (Javacord)

### Maven
```xml
<dependency>
  <groupId>de.kaleidox</groupId>
  <artifactId>discordbotslist-stats-javacord</artifactId>
  <version>0.1.2</version>
</dependency>
```

### Gradle
```groovy
dependencies {
    implementation 'de.kaleidox:discordbotlist-stats-javacord:0.1.2'
}
```

## Importing (Discord4J)

### Maven
```xml
<dependency>
  <groupId>de.kaleidox</groupId>
  <artifactId>discordbotslist-stats-discord4j</artifactId>
  <version>0.1.2</version>
</dependency>
```

### Gradle
```groovy
dependencies {
    implementation 'de.kaleidox:discordbotlist-stats-discord4j:0.1.2'
}
```

## Importing (JDA)

### Maven
```xml
<dependency>
  <groupId>de.kaleidox</groupId>
  <artifactId>discordbotslist-stats-jda</artifactId>
  <version>0.1.2</version>
</dependency>
```

### Gradle
```groovy
dependencies {
    implementation 'de.kaleidox:discordbotlist-stats-jda:0.1.2'
}
```

## Usage example: Javacord
You have two possible ways of using this library.
In these examples, we will be using Javacord for demonstration purposes.

#### Creating a Single-Sharded stats connection
To create a single sharded stats connection, you must first define your Javacord `DiscordApi` object, and then pass that to the `JavacordStatsClient` constructor.

You will also need to create a `BotListSettings` object, which will serve as your token carrier.
This object can be built using the included builder structure.
If you do not set a token for a bot list service in the builder, no stats will be posted to that service. 
```java
DiscordApi api = new DiscordApiBuilder()
        .setToken(/* token */)
        .login()
        .join();

BotListSettings settings = BotListSettings.builder()
        .postStatsTester(OSValidator::isUnix)
        /* define all tokens that you want to use */
        .discordbots_org_token(/* token */)
        .divinediscordbots_com_token(/* token */)
        .build();
StatsClient stats = new JavacordStatsClient(settings, API);
```
That's it already! The `StatsClient` object does the rest for you.
In every library, it will register itself as a `GuildJoin` and `GuildLeave` listener, 
and update the stats everytime the bot joins or leaves a guild.
