package com.customcluehunt;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface CustomClueHuntConfig extends Config {
    @ConfigItem(keyName = "greeting", name = "Welcome Greeting", description = "The message to show to the user when they login")
    default String greeting() {
        return "Will this work?";
    }

    @ConfigItem(keyName = "custom", name = "My own text", description = "Custom message to see whenever")
    default String custom() {
        return "Mom its working";
    }
}
