package com.eternalcode.multification.bukkit.notice.resolver.sound;

import java.lang.reflect.Method;
import org.bukkit.Sound;

public final class SoundAccessor {

    private static final Method VALUE_OF_METHOD;
    private static final Method nameMethod;

    static {
        try {
            VALUE_OF_METHOD = Sound.class.getMethod("valueOf", String.class);
            VALUE_OF_METHOD.setAccessible(true);

            nameMethod = Sound.class.getMethod("name");
            nameMethod.setAccessible(true);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new RuntimeException(noSuchMethodException);
        }
    }

    private SoundAccessor() {
    }

    public static Sound valueOf(String name) {
        try {
            return (Sound) VALUE_OF_METHOD.invoke(null, name);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            throw new RuntimeException(reflectiveOperationException);
        }
    }

    public static String name(Sound sound) {
        try {
            return (String) nameMethod.invoke(sound);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            throw new RuntimeException(reflectiveOperationException);
        }
    }

}
