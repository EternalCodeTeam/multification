<div align="center">
  
![](/assets/readme-banner.png)
# Supports cdn and okaeri configs

[![Patreon](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/donate/patreon-plural_vector.svg)](https://www.patreon.com/eternalcode)
[![Website](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/documentation/website_vector.svg)](https://eternalcode.pl/)
[![Discord](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/social/discord-plural_vector.svg)](https://discord.gg/FQ7jmGBd6c)

[![Gradle](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/built-with/gradle_vector.svg)](https://gradle.org/)
[![Java](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/built-with/java17_vector.svg)](https://www.java.com/)

</div>

# Introduction

Multification is a spigot-library that allows you to easily create configurable notifications and messages inside your minecraft plugin.
The library supports sending notifications via one or multiple options:
- customizable messages,
- action bar notifications,
- title and subtitle,
- boss bar notifications,
- and sounds,

messages can be sent to:
- multiple players,
- one player,
- or the console.

Your messages can also contain placeholders :P

## Setup

To use the library, you need to add the following repository and dependency to your `build.gradle` file:

```gradle
implementation("com.eternalcode:multification-bukkit:1.1.4")
implementation("com.eternalcode:multification-cdn:1.1.4")
```

Then create a new instance of the `Multification` class and use it to send notifications:

```java
public class YourMultification extends BukkitMultification<MessagesConfig> {

    private final MessagesConfig messagesConfig;
    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public YourMultification(MessagesConfig messagesConfig, AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.messagesConfig = messagesConfig;
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    @Override
    protected @NotNull TranslationProvider<MessagesConfig> translationProvider() {
        return locale -> this.messagesConfig;
    }

    @Override
    protected @NotNull ComponentSerializer<Component, Component, String> serializer() {
        return this.miniMessage;
    }

    @Override
    protected @NotNull AudienceConverter<CommandSender> audienceConverter() {
        return  commandSender -> {
            if (commandSender instanceof Player player) {
                return this.audienceProvider.player(player.getUniqueId());
            }

            return this.audienceProvider.console();
        };
    }

}
```

Then on enable, you can create a new instance of the `YourMultification` class and use it to send notifications:

```java
    private AudienceProvider audienceProvider = BukkitAudiences.create(this);
    MiniMessage miniMessage = MiniMessage.miniMessage();
    
    MessagesConfig messagesConfig = new MessagesConfig();
    YourMultification multification = new YourMultification(messagesConfig, audienceProvider, miniMessage);
```

After that, you can use the `multification` instance to send notifications:

```java
    multification.create()
        .player(player.getUniqueId())
        .notice(messages -> messages.yourMessage)
        .send();
```

Setting up configuration is easy both in cdn and okaeri configs. To add messages to the configuration, create variable in config with class `Notice` or `BukktiNotice`. You can also use builder. After plugin deploy you can find messages in configuration file.

### To see more examples open the [example plugin module](https://github.com/EternalCodeTeam/multification/tree/master/examples/bukkit).
