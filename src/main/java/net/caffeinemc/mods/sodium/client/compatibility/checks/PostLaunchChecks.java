//
// Decompiled by Jadx - 4453ms
//
package me.jellysquid.mods.sodium.client.compatibility.checks;

import me.jellysquid.mods.sodium.client.gui.console.Console;
import me.jellysquid.mods.sodium.client.gui.console.message.MessageLevel;
import net.minecraft.class_2561;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostLaunchChecks {
    private static final Logger LOGGER = LoggerFactory.getLogger("Sodium-PostlaunchChecks");

    public static void onContextInitialized() {
        if (isUsingPojavLauncher()) {
            Console.instance().logMessage(MessageLevel.SEVERE, class_2561.method_43471("sodium.console.pojav_launcher"), 30.0d);
            LOGGER.error("It appears that PojavLauncher is being used with an OpenGL compatibility layer. This will likely cause severe performance issues, graphical issues, and crashes when used with Sodium. This configuration is not supported -- you are on your own!");
        }
    }

    private static boolean isUsingPojavLauncher() {
        if (System.getenv("POJAV_RENDERER") != null) {
            LOGGER.warn("Detected presence of environment variable POJAV_LAUNCHER, which seems to indicate we are running on Android");
            return true;
        }
        String property = System.getProperty("java.library.path", null);
        if (property != null) {
            String[] split = property.split(":");
            for (String str : split) {
                if (isKnownAndroidPathFragment(str)) {
                    LOGGER.warn("Found a library search path which seems to be hosted in an Android filesystem: {}", str);
                    return true;
                }
            }
        }
        String property2 = System.getProperty("user.home", null);
        if (property2 != null && isKnownAndroidPathFragment(property2)) {
            LOGGER.warn("Working directory seems to be hosted in an Android filesystem: {}", property2);
        }
        return false;
    }

    private static boolean isKnownAndroidPathFragment(String str) {
        return str.matches("/data/user/[0-9]+/net\\.kdt\\.pojavlaunch");
    }
}
