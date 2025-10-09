package com.eternalcode.multification.bukkit.util;

public class PacketEventsUtil {

    private static Boolean packetEventsLoaded = null;

    private static final String[] PACKETEVENTS_CLASSES = {
            "com.github.retrooper.packetevents.PacketEvents",
            "io.github.retrooper.packetevents.PacketEvents",
            "com.github.retrooper.packetevents.PacketEventsAPI",
            "io.github.retrooper.packetevents.PacketEventsAPI"
    };

    /**
     * Checks if PacketEvents is loaded via Class.forName()
     * Handles different relocations
     *
     * @return true if PacketEvents is available, false otherwise
     */
    public static boolean isPacketEventsLoaded() {
        if (packetEventsLoaded == null) {
            packetEventsLoaded = checkPacketEventsClass();
        }
        return packetEventsLoaded;
    }

    /**
     * Checks if any PacketEvents class exists
     *
     * @return true if PacketEvents class was found
     */
    private static boolean checkPacketEventsClass() {
        for (String className : PACKETEVENTS_CLASSES) {
            try {
                Class.forName(className);
                return true;
            }
            catch (ClassNotFoundException ignored) {}
        }
        return false;
    }
}
