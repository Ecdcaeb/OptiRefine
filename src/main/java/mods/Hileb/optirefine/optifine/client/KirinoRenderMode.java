package mods.Hileb.optirefine.optifine.client;

import com.cleanroommc.kirino.KirinoClientCore;
import com.cleanroommc.kirino.KirinoCommonCore;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public final class KirinoRenderMode {
    private static final ConfigAccess CONFIG = findConfig();

    private KirinoRenderMode() {}

    public static boolean runsHeadlessly() {
        try {
            return (boolean) CONFIG.enabled.invokeExact()
                    && (!(boolean) CONFIG.renderDelegate.invokeExact() || KirinoClientCore.isRenderUnsupported());
        } catch (RuntimeException | Error failure) {
            throw failure;
        } catch (Throwable failure) {
            throw new IllegalStateException("Could not read Cleanroom's Kirino render settings", failure);
        }
    }

    private static ConfigAccess findConfig() {
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        try {
            Field hub;
            try {
                hub = KirinoCommonCore.class.getField("KIRINO_CONFIG_HUB");
            } catch (NoSuchFieldException ignored) {
                // Newer Cleanroom versions use Forge's restart-only config fields.
                Class<?> config = Class.forName("com.cleanroommc.kirino.config.KirinoConfig", false, KirinoCommonCore.class.getClassLoader());
                Field restart = config.getField("NEEDS_RESTART");
                MethodHandle settings = lookup.unreflectGetter(restart);
                return new ConfigAccess(
                        MethodHandles.filterReturnValue(settings, lookup.findGetter(restart.getType(), "enable", boolean.class)),
                        MethodHandles.filterReturnValue(settings, lookup.findGetter(restart.getType(), "enableRenderDelegate", boolean.class)));
            }
            MethodHandle settings = lookup.unreflectGetter(hub);
            MethodType predicate = MethodType.methodType(boolean.class);
            return new ConfigAccess(
                    MethodHandles.filterReturnValue(settings, lookup.findVirtual(hub.getType(), "isEnable", predicate)),
                    MethodHandles.filterReturnValue(settings, lookup.findVirtual(hub.getType(), "isEnableRenderDelegate", predicate)));
        } catch (ReflectiveOperationException failure) {
            throw new IllegalStateException("Could not resolve Cleanroom's Kirino render settings", failure);
        }
    }

    private record ConfigAccess(MethodHandle enabled, MethodHandle renderDelegate) {}
}
