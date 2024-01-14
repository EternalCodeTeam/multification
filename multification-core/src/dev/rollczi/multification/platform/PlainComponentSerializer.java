package dev.rollczi.multification.platform;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

class PlainComponentSerializer implements ComponentSerializer<Component, Component, String> {
    @Override
    public @NotNull Component deserialize(@NotNull String input) {
        return Component.text(input);
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {
        return componentToText(component);
    }

    private String componentToText(Component component) {
        StringBuilder builder = new StringBuilder();

        if (component instanceof TextComponent textComponent) {
            builder.append(textComponent.content());
        }

        for (Component child : component.children()) {
            builder.append(componentToText(child));
        }

        return builder.toString();
    }

}
