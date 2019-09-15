# Discord BotList Stats ![Build Status](https://github.com/burdoto/VBAN-API/workflows/Build%20Tests/badge.svg) [![Development Release](https://jitpack.io/v/burdoto/discordbotlist-stats.svg)](https://jitpack.io/#burdoto/discordbotlist-stats)
Easy-to-use library for posting stats on most available Discord Bot list.

### Currently Supported:
- [discordbots.org](https://discordbots.org/)
- [discord.bots.gg](https://discord.bots.gg/)
- [discordbotslist.com](https://discordbotlist.com/)
- [divinediscordbots.com](https://divinediscordbots.com/)
- [bots.ondiscord.xyz](https://bots.ondiscord.xyz/)

## Core Module [![Javadocs](http://javadoc.io/badge/de.kaleidox/discordbotlist-stats-core.svg)](http://javadoc.io/doc/de.kaleidox/discordbotlist-stats-core) [![Maven Central Release](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-core)
The core module holds basic implementation and is necessary for `discordbotlist-stats` to work properly.

The core module is the only dependency of each submodule, so you can just simply depend on the module that you need for the library you are using.
All other dependencies, like the REST-Client, or a JSON library are used from the respective library. Why depend on them if they are already there?  

## Importing ([Catnip](https://github.com/mewna/catnip)) [![Javadocs](http://javadoc.io/badge/de.kaleidox/discordbotlist-stats-catnip.svg)](http://javadoc.io/doc/de.kaleidox/discordbotlist-stats-catnip) [![Maven Central Release](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-catnip/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-catnip)

### Maven
```xml
<dependency>
  <groupId>de.kaleidox</groupId>
  <artifactId>discordbotslist-stats-catnip</artifactId>
  <version>0.1.0</version>
</dependency>
```

### Gradle
```groovy
dependencies {
    implementation 'de.kaleidox:discordbotlist-stats-catnip:0.1.0'
}
```

## Importing ([Discord4J](https://github.com/Discord4J/Discord4J)) [![Javadocs](http://javadoc.io/badge/de.kaleidox/discordbotlist-stats-discord4j.svg)](http://javadoc.io/doc/de.kaleidox/discordbotlist-stats-discord4j) [![Maven Central Release](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-discord4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-discord4j)

### Maven
```xml
<dependency>
  <groupId>de.kaleidox</groupId>
  <artifactId>discordbotslist-stats-discord4j</artifactId>
  <version>0.1.3</version>
</dependency>
```

### Gradle
```groovy
dependencies {
    implementation 'de.kaleidox:discordbotlist-stats-discord4j:0.1.3'
}
```

## Importing ([Javacord](https://github.com/Javacord/Javacord)) [![Javadocs](http://javadoc.io/badge/de.kaleidox/discordbotlist-stats-javacord.svg)](http://javadoc.io/doc/de.kaleidox/discordbotlist-stats-javacord) [![Maven Central Release](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-javacord/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-javacord)

### Maven
```xml
<dependency>
  <groupId>de.kaleidox</groupId>
  <artifactId>discordbotslist-stats-javacord</artifactId>
  <version>0.1.3</version>
</dependency>
```

### Gradle
```groovy
dependencies {
    implementation 'de.kaleidox:discordbotlist-stats-javacord:0.1.3'
}
```

## Importing ([JDA](https://github.com/DV8FromTheWorld/JDA)) [![Javadocs](http://javadoc.io/badge/de.kaleidox/discordbotlist-stats-jda.svg)](http://javadoc.io/doc/de.kaleidox/discordbotlist-stats-jda) [![Maven Central Release](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-jda/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordbotlist-stats-jda)

### Maven
```xml
<dependency>
  <groupId>de.kaleidox</groupId>
  <artifactId>discordbotslist-stats-jda</artifactId>
  <version>0.1.3</version>
</dependency>
```

### Gradle
```groovy
dependencies {
    implementation 'de.kaleidox:discordbotlist-stats-jda:0.1.3'
}
```

## Usage example: Catnip
With Catnip, using this library becomes easy-as-pie!
You only need to do two additional steps:

1. Build a `BotListSettings` object.
2. Load the `CatnipStatsExtension` in Catnip.

##### Example code:
```java
Catnip catnip = Catnip.catnip("token");

/* ... */

BotListSettings botListSettings = BotListSettings.builder()
        /* define a file from which tokens will be scanned */
        .tokenFile(new File("list_tokens.properties"))
        /* 
            define a Supplier<Boolean> to tell the library when updating should be disabled
            in this case, we check for the OS using another class
        */
        .postStatsTester(OSValidator::isUnix)
        .build();

// load the extension
catnip.loadExtension(new CatnipStatsExtension(botListSettings));
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
