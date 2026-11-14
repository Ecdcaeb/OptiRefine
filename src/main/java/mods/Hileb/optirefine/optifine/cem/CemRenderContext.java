package mods.Hileb.optirefine.optifine.cem;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Arrays;

/** Render-thread context. Every render boundary restores its caller in a finally block. */
public final class CemRenderContext {
    private static Entity[] entities = new Entity[8];
    private static World[] worlds = new World[8];
    private static float[] partialTicks = new float[8];
    private static int depth;
    private static int resourceGeneration;
    private static long previousFrameNanos;
    private static float frameTime;
    private static int frameCounter = -1;

    private CemRenderContext() {}

    public static void push(Entity entity, World world, float partial) {
        if (depth == entities.length) {
            entities = Arrays.copyOf(entities, depth * 2);
            worlds = Arrays.copyOf(worlds, depth * 2);
            partialTicks = Arrays.copyOf(partialTicks, depth * 2);
        }
        entities[depth] = entity;
        worlds[depth] = world;
        partialTicks[depth] = partial;
        ++depth;
    }

    public static void pop() {
        --depth;
        entities[depth] = null;
        worlds[depth] = null;
    }

    public static Entity entity() {
        return depth == 0 ? null : entities[depth - 1];
    }

    public static World world() {
        return depth == 0 ? Minecraft.getMinecraft().world : worlds[depth - 1];
    }

    public static float partialTicks() {
        return depth == 0 ? Minecraft.getMinecraft().getRenderPartialTicks() : partialTicks[depth - 1];
    }

    public static void beginFrame(long nowNanos) {
        frameTime = previousFrameNanos == 0 ? 0 : (nowNanos - previousFrameNanos) * 1.0E-9F;
        previousFrameNanos = nowNanos;
        if (++frameCounter >= 27720) {
            frameCounter = 0;
        }
    }

    public static float frameTime() {
        return Minecraft.getMinecraft().isGamePaused() ? 0 : frameTime;
    }

    public static int frameCounter() {
        return frameCounter;
    }

    public static int resourceGeneration() {
        return resourceGeneration;
    }

    public static void reload() {
        ++resourceGeneration;
    }

    public static CemVariableStore variables(boolean create) {
        Entity entity = entity();
        return entity == null ? null : ((EntityCemVariables) entity).optiRefine$cemVariables(create);
    }
}
