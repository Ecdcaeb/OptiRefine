package mods.Hileb.optirefine.optifine.cem;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Arrays;

/** Render-thread context. Every render boundary restores its caller in a finally block. */
public final class CemRenderContext {
    private static final byte GUI = 1;
    private static final byte SHOULDER = 2;

    private static Entity[] entities = new Entity[8];
    private static World[] worlds = new World[8];
    private static float[] partialTicks = new float[8];
    private static byte[] renderFlags = new byte[8];
    private static int depth;
    private static int resourceGeneration;
    private static long previousFrameNanos;
    private static float frameTime;
    private static long frameNumber = -1;

    private CemRenderContext() {}

    public static void push(Entity entity, World world, float partial) {
        byte flags = entity != null && entity == entity() ? renderFlags[depth - 1] : 0;
        push(entity, world, partial, flags);
    }

    public static void pushGui(Entity entity, World world, float partial) {
        push(entity, world, partial, GUI);
    }

    public static void pushShoulder(Entity entity, World world, float partial) {
        push(entity, world, partial, inGui() ? (byte) (GUI | SHOULDER) : SHOULDER);
    }

    private static void push(Entity entity, World world, float partial, byte flags) {
        if (depth == entities.length) {
            entities = Arrays.copyOf(entities, depth * 2);
            worlds = Arrays.copyOf(worlds, depth * 2);
            partialTicks = Arrays.copyOf(partialTicks, depth * 2);
            renderFlags = Arrays.copyOf(renderFlags, depth * 2);
        }
        entities[depth] = entity;
        worlds[depth] = world;
        partialTicks[depth] = partial;
        renderFlags[depth] = flags;
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

    public static boolean inGui() {
        return depth != 0 && (renderFlags[depth - 1] & GUI) != 0;
    }

    public static boolean onShoulder() {
        return depth != 0 && (renderFlags[depth - 1] & SHOULDER) != 0;
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
        ++frameNumber;
    }

    public static float frameTime() {
        return Minecraft.getMinecraft().isGamePaused() ? 0 : frameTime;
    }

    public static int frameCounter() {
        return (int) (frameNumber % 27720);
    }

    static boolean isNthFrame(int interval) {
        return interval > 0 && frameNumber >= 0 && frameNumber % interval == 0;
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
