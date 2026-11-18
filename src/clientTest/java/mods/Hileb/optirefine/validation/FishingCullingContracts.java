package mods.Hileb.optirefine.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;

import java.lang.reflect.Field;
import java.util.List;

final class FishingCullingContracts {
    private FishingCullingContracts() {}

    static void verify(Minecraft mc) throws ReflectiveOperationException {
        Field infos = field("renderInfos", "field_72755_R");
        Field startup = field("renderEntitiesStartupCounter", "field_72740_G");
        Object previousInfos = infos.get(mc.renderGlobal);
        int previousStartup = startup.getInt(mc.renderGlobal);
        int previousPass = MinecraftForgeClient.getRenderPass();
        EntityFishHook previousHook = mc.player.fishEntity;
        EntityFishHook hook = new EntityFishHook(mc.world, mc.player, mc.player.posX + 1.0D, mc.player.posY, mc.player.posZ);
        int id = Integer.MIN_VALUE + 6392;
        RenderManager manager = mc.getRenderManager();
        int[] draws = {0};
        var previousRenderer = manager.entityRenderMap.put(EntityFishHook.class, new RenderFish(manager) {
            @Override public void doRender(EntityFishHook entity, double x, double y, double z, float yaw, float partialTicks) {
                if (entity == hook) draws[0]++;
            }
        });
        try {
            mc.world.addEntityToWorld(id, hook);
            infos.set(mc.renderGlobal, List.of());
            startup.setInt(mc.renderGlobal, 0);
            ForgeHooksClient.setRenderPass(0);
            mc.renderGlobal.renderEntities(mc.getRenderViewEntity(), new LineCamera(false), 0.0F);
            if (draws[0] != 0) throw new AssertionError("Fishing hook outside both visible chunks and the line frustum was rendered");
            mc.renderGlobal.renderEntities(mc.getRenderViewEntity(), new LineCamera(true), 0.0F);
            if (draws[0] != 1) throw new AssertionError("Fishing line lost its renderer when the hook's section was culled");
        } finally {
            mc.world.removeEntityFromWorld(id);
            mc.player.fishEntity = previousHook;
            if (previousRenderer == null) manager.entityRenderMap.remove(EntityFishHook.class);
            else manager.entityRenderMap.put(EntityFishHook.class, previousRenderer);
            infos.set(mc.renderGlobal, previousInfos);
            startup.setInt(mc.renderGlobal, previousStartup);
            ForgeHooksClient.setRenderPass(previousPass);
        }
    }

    private record LineCamera(boolean lineVisible) implements ICamera {
        @Override public void setPosition(double x, double y, double z) {}
        @Override public boolean isBoundingBoxInFrustum(AxisAlignedBB box) { return lineVisible; }
    }

    private static Field field(String mcp, String srg) throws NoSuchFieldException {
        Field result;
        try {
            result = RenderGlobal.class.getDeclaredField(mcp);
        } catch (NoSuchFieldException absentInNamespace) {
            result = RenderGlobal.class.getDeclaredField(srg);
        }
        result.setAccessible(true);
        return result;
    }
}
