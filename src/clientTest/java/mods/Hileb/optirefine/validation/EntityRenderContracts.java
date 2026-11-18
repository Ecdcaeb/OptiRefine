package mods.Hileb.optirefine.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;

import java.lang.reflect.Field;
import java.util.Map;

final class EntityRenderContracts {
    private EntityRenderContracts() {}

    static void verify(Minecraft mc) throws ReflectiveOperationException {
        Field currentEntity = RenderGlobal.class.getField("renderedEntity");
        Field renderCounter = RenderGlobal.class.getField("renderEntitiesCounter");
        Entity previousEntity = (Entity)currentEntity.get(mc.renderGlobal);
        int previousCounter = renderCounter.getInt(null);
        int previousPass = MinecraftForgeClient.getRenderPass();
        boolean previousProfiling = mc.profiler.profilingEnabled;
        EntityArmorStand subject = new EntityArmorStand(mc.world, mc.player.posX, mc.player.posY, mc.player.posZ);
        int subjectId = Integer.MIN_VALUE + 425;
        RuntimeException fault = new IllegalStateException("Intentional entity renderer failure");
        RenderManager manager = mc.getRenderManager();
        Map<Class<? extends Entity>, Render<? extends Entity>> renderers = manager.entityRenderMap;
        Render<? extends Entity> previousRenderer = renderers.put(EntityArmorStand.class, new Render<EntityArmorStand>(manager) {
            @Override public void doRender(EntityArmorStand stand, double x, double y, double z, float yaw, float ticks) {
                if (stand == subject) throw fault;
            }

            @Override protected ResourceLocation getEntityTexture(EntityArmorStand stand) { return null; }
        });
        ICamera camera = new ICamera() {
            @Override public void setPosition(double x, double y, double z) {}
            @Override public boolean isBoundingBoxInFrustum(AxisAlignedBB box) { return true; }
        };
        mc.profiler.profilingEnabled = true;
        mc.profiler.startSection("entityRenderContract");
        String section = mc.profiler.getNameOfLastSection();
        try {
            currentEntity.set(mc.renderGlobal, mc.player);
            mc.world.addEntityToWorld(subjectId, subject);
            ForgeHooksClient.setRenderPass(0);
            try {
                mc.renderGlobal.renderEntities(mc.getRenderViewEntity(), camera, 0.0F);
                throw new AssertionError("Faulting entity renderer was not called");
            } catch (RuntimeException failure) {
                Throwable cause = failure;
                while (cause != null && cause != fault) cause = cause.getCause();
                if (cause != fault) throw failure;
            }
            if (currentEntity.get(mc.renderGlobal) != mc.player || renderCounter.getInt(null) != previousCounter
                    || !section.equals(mc.profiler.getNameOfLastSection()) || MinecraftForgeClient.getRenderPass() != 0) {
                throw new AssertionError("Entity renderer failure left stale entity, counter, profiler or Forge pass state");
            }
        } finally {
            mc.world.removeEntityFromWorld(subjectId);
            if (previousRenderer == null) renderers.remove(EntityArmorStand.class);
            else renderers.put(EntityArmorStand.class, previousRenderer);
            currentEntity.set(mc.renderGlobal, previousEntity);
            renderCounter.setInt(null, previousCounter);
            ForgeHooksClient.setRenderPass(previousPass);
            mc.profiler.endSection();
            mc.profiler.profilingEnabled = previousProfiling;
        }
    }
}
