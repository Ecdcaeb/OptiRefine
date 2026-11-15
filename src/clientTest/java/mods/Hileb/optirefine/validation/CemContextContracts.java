package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ReportedException;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

import java.lang.reflect.Field;

/** Exercises the installed resource-pack model through the real item and TESR render paths. */
public final class CemContextContracts {
    private CemContextContracts() {}

    public static void verify(Minecraft mc) throws Exception {
        TileEntityRendererDispatcher dispatcher = TileEntityRendererDispatcher.instance;
        TileEntityChestRenderer renderer = (TileEntityChestRenderer) dispatcher.<TileEntityChest>getRenderer(TileEntityChest.class);
        ModelChest model = (ModelChest) field(TileEntityChestRenderer.class, "simpleChest", "field_147510_h").get(renderer);
        ModelRenderer probe = (ModelRenderer) ModelRenderer.class.getMethod("getChildDeep", String.class)
                .invoke(model.chestBelow, "context_probe");
        if (probe == null) throw new AssertionError("Actual chest JEM was not installed by resource refresh");
        TileEntityItemStackRenderer itemRenderer = TileEntityItemStackRenderer.instance;
        TileEntityChest itemChest = (TileEntityChest) field(TileEntityItemStackRenderer.class,
                "chestBasic", "field_147717_b").get(itemRenderer);
        if (itemChest.getWorld() != null) throw new AssertionError("Item chest unexpectedly has a world");

        WorldClient enclosing = new WorldClient(mc.getConnection(),
                new WorldSettings(137L, GameType.CREATIVE, false, false, WorldType.FLAT),
                -1, EnumDifficulty.NORMAL, mc.profiler);
        enclosing.setWorldTime(192345L);
        TileEntityChest chest = new TileEntityChest();
        long clientTime = mc.world.getWorldTime();
        try {
            mc.world.setWorldTime(72345L);
            // No enclosing context: the ordinary item renderer must inherit the client world.
            renderItem(itemRenderer);
            animation(probe, mc.world, 0);
            if (CemRenderContext.entity() != null || CemRenderContext.world() != mc.world) {
                throw new AssertionError("Item render leaked its CEM context");
            }

            // A different real world makes client-world-only fallbacks observable.
            CemRenderContext.push(mc.player, enclosing, 0.75F);
            try {
                renderItem(itemRenderer);
                animation(probe, enclosing, 0);
                restored(mc, enclosing);
                render(dispatcher, chest, 0.25F, -1);
                animation(probe, enclosing, 0.25F);
                restored(mc, enclosing);

                // Even an enclosing boundary without a world must retain the available client world.
                CemRenderContext.push(mc.player, null, 0.5F);
                try {
                    render(dispatcher, chest, 0.5F, -1);
                    animation(probe, mc.world, 0.5F);
                    if (CemRenderContext.world() != null || CemRenderContext.entity() != mc.player
                            || CemRenderContext.partialTicks() != 0.5F) {
                        throw new AssertionError("Worldless enclosing boundary was not restored");
                    }
                } finally {
                    CemRenderContext.pop();
                }

                // A TileEntity's own world takes precedence over its enclosing render boundary.
                chest.setWorld(mc.world);
                render(dispatcher, chest, 0.125F, -1);
                animation(probe, mc.world, 0.125F);
                restored(mc, enclosing);
                chest.setWorld(null);

                // The real renderer rejects an out-of-range crack texture before pushing a GL matrix.
                try {
                    render(dispatcher, chest, 0.25F, 10);
                    throw new AssertionError("Invalid native chest destroy stage unexpectedly rendered");
                } catch (ReportedException error) {
                    if (!(error.getCause() instanceof ArrayIndexOutOfBoundsException)) throw error;
                }
                restored(mc, enclosing);
            } finally {
                CemRenderContext.pop();
            }
            if (CemRenderContext.entity() != null || CemRenderContext.world() != mc.world) {
                throw new AssertionError("CEM render context did not unwind to the client");
            }
        } finally {
            mc.world.setWorldTime(clientTime);
            GlStateManager.color(1, 1, 1, 1);
        }
    }

    private static void renderItem(TileEntityItemStackRenderer renderer) {
        GlStateManager.pushMatrix();
        try {
            renderer.renderByItem(new ItemStack(Blocks.CHEST), 1);
        } finally {
            GlStateManager.popMatrix();
        }
    }

    private static void render(TileEntityRendererDispatcher dispatcher, TileEntityChest chest,
                               float partialTicks, int destroyStage) {
        dispatcher.render(chest, 0, 0, 0, partialTicks, destroyStage, 1);
    }

    private static void animation(ModelRenderer probe, World world, float partialTicks) {
        float time = (world.getWorldTime() % 24000L + partialTicks) / 24000F;
        if (Math.abs(probe.rotationPointX - time) > 0.00001F
                || probe.rotationPointY != world.getWorldTime() / 24000L
                || probe.rotationPointZ != world.provider.getDimension()) {
            throw new AssertionError("Chest JEM world parameters: " + probe.rotationPointX + ", "
                    + probe.rotationPointY + ", " + probe.rotationPointZ + "; expected " + time + ", "
                    + world.getWorldTime() / 24000L + ", " + world.provider.getDimension());
        }
    }

    private static void restored(Minecraft mc, World world) {
        if (CemRenderContext.entity() != mc.player || CemRenderContext.world() != world
                || CemRenderContext.partialTicks() != 0.75F) {
            throw new AssertionError("Tile render failed to restore enclosing entity/world/partial ticks");
        }
    }

    private static Field field(Class<?> owner, String mcp, String srg) throws NoSuchFieldException {
        Field field;
        try {
            field = owner.getDeclaredField(mcp);
        } catch (NoSuchFieldException ignored) {
            field = owner.getDeclaredField(srg);
        }
        field.setAccessible(true);
        return field;
    }
}
