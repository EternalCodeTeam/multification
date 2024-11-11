<div align="center">
  
![](/assets/readme-banner.png)
### Multification
Powerful library for sending custom notifications based on adventure.

[![Patreon](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/donate/patreon-plural_vector.svg)](https://www.patreon.com/eternalcode)
[![Website](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/documentation/website_vector.svg)](https://eternalcode.pl/)
[![Discord](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/social/discord-plural_vector.svg)](https://discord.gg/FQ7jmGBd6c)

[![Gradle](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/built-with/gradle_vector.svg)](https://gradle.org/)
[![Java](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/built-with/java17_vector.svg)](https://www.java.com/)

</div>

## About

Multification makes it simple to create customizable notifications and messages within large plugins that require handling a high volume of messages (while the setup may not be easy, it’s worthwhile for extensive plugins). It offers a range of features, including:

- 💭 Chat messages
- 📕 Title & Subtitle
- 🎬 ActionBar
- 🍫 BossBar
- 🔊 Sounds
- 🎨 Adventure support
- 🌈 MiniMessage support (including gradients, hex colors, hover, click and more)
- 📥 Placeholders
- 🛠️ Formatter support
- 🌎 Flexible messages providing (easy to implement i18n)
- 📦 Configuration support (CDN, Okaeri Configs)
- Cross-platform support (currently we support Bukkit, but it's easy to add support for other adventure-based platforms)

Messages can be sent to any audience, such as players or the console.

## Getting Started

To use the library, you need to add the following repository and dependency to your `build.gradle` file:

```kts
maven("https://repo.eternalcode.pl/releases")
```

```kts
implementation("com.eternalcode:multification-bukkit:1.1.4")
```

> **Note:** If you want to use the library with other platforms, then you need to use the `multification-core` dependency.

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

Then in init method such as `onEnable`,
you can create a new instance of the `YourMultification` class and use it to send notifications:

```java
AudienceProvider audienceProvider = BukkitAudiences.create(this);
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

## Configuration Support

Multification currently supports two configuration libraries:
- [CDN](https://github.com/dzikoysk/cdn) _Simple and fast property-based configuration library for JVM apps_
- [Okaeri Configs](https://github.com/OkaeriPoland/okaeri-configs) _Simple Java/POJO config library written with love and Lombok_

To use the Multification library with one of the configuration libraries, you need to:

### [CDN](https://github.com/dzikoysk/cdn)

#### (CDN) 1. Add dependency to your `build.gradle` file:
```gradle
implementation("com.eternalcode:multification-cdn:1.1.4")
implementation("net.dzikoysk:cdn:1.14.5")
```

#### (CDN) 2. Create configuration class:
```java
public class MessagesConfig {
    @Description("# My first message")
    public Notice firstMessage = Notice.chat("<gradient:red:blue>Multification is awesome!");
}
```

#### (CDN) 3. Create a new instance of the `Cdn` with the `MultificationNoticeCdnComposer`:
```java
Cdn cdn = CdnFactory.createYamlLike()
    .getSettings()
    .withComposer(Notice.class, new MultificationNoticeCdnComposer(multification.getNoticeRegistry()))
    .build();
```

#### (CDN) 4. Load the configuration:

To load and create the config file, use the following code in the init method such as `onEnable`:

```java
MessagesConfig messages = new MessagesConfig();
Resource resource = Source.of(this.dataFolder, "messages.yml");
        
cdn.load(resource, messages)
    .orThrow(cause -> cause);

cdn.render(config, resource)
    .orThrow(cause -> cause);
```

Checkout example with CDN! [example plugin](https://github.com/EternalCodeTeam/multification/tree/master/examples/bukkit).

### [Okaeri Configs](https://github.com/OkaeriPoland/okaeri-configs)

#### (Okaeri) 1. Add the following dependency to your `build.gradle` file:

```gradle
implementation("com.eternalcode:multification-okaeri:1.1.4")
```

Probably also you will need to add additional dependencies for your platform, e.g. :
```gradle
implementation("eu.okaeri:okaeri-configs-serdes-commons:5.0.5")
implementation("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.5")
implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.5")
```
See [Okaeri Configs](https://github.com/OkaeriPoland/okaeri-configs) for more information.

#### (Okaeri) 2. Create configuration class:

```java
public class MessagesConfig extends OkaeriConfig {
    @Comment("My first message")
    public Notice firstMessage = Notice.chat("<gradient:red:blue>Multification is awesome!");
}
```

#### (Okaeri) 3. Load the configuration:

```java
MessagesConfig config = (MessagesConfig) ConfigManager.create(MessagesConfig.class)
    .withConfigurer(new MultificationSerdesPack(multification.getNoticeRegistry()))
    .withConfigurer(new SerdesCommons(), new YamlBukkitConfigurer(), new SerdesBukkit()) // specify configurers for your platform
    .withBindFile(new File(dataFolder, "messages.yml"))
    .withRemoveOrphans(true) // automatic removal of undeclared keys
    .saveDefaults() // save file if does not exists
    .load(true);
```

