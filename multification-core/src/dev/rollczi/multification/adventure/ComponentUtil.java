package dev.rollczi.multification.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public final class ComponentUtil {

    private ComponentUtil() {
    }

    public static String componentToText(Component component) {
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
