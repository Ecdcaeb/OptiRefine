package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.player.PlayerItemsLayer;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
@Mixin(RenderManager.class)
public abstract class MixinRenderManager {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    // [AUDIT-OK] OF-added field (OF:142 public); nit: '= null' initializer on @Unique instance field (AGENT.md §4 convention)
    public Render<?> renderRender = null;

    @Shadow
    @Final
    // [AUDIT-OK] baseline member skinMap (private final) declared in RenderManager (deobf:122)
    private Map<String, RenderPlayer> skinMap;

    @Inject(method = "<init>", at = @At("RETURN"))
    // [AUDIT-OK] target <init>(TextureManager,RenderItem) baseline; PlayerItemsLayer.register matches OF:231
    public void init(TextureManager p_i46180_1, RenderItem p_i46180_2, CallbackInfo ci){
        PlayerItemsLayer.register(this.skinMap);
    }

    @WrapOperation(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;doRender(Lnet/minecraft/entity/Entity;DDDFF)V"))
    // [AUDIT-OK] target renderEntity + Render.doRender INVOKE baseline; renderRender capture matches OF:358
    public void beforeDoRender(Render<Entity> instance, Entity entity, double x, double y, double z, float entityYaw, float partialTicks, Operation<Void> original){
        if (CustomEntityModels.isActive()) {
            this.renderRender = instance;
        }
        original.call(instance, entity, x, y, z, entityYaw, partialTicks);
    }

    @WrapMethod(method = "renderDebugBoundingBox")
    // [AUDIT-OK] target renderDebugBoundingBox baseline (deobf:457); shadow-pass guard matches OF:421
    public void blockRenderDebugBoundingBox(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks, Operation<Void> original){
        if (!Shaders.isShadowPass) {
            original.call(entityIn, x, y, z, entityYaw, partialTicks);
        }
    }

    @Shadow @Final
    // [AUDIT-OK] baseline member entityRenderMap (public in Forge 1.12.2, deobf:121)
    public Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap;

    @Unique
    // [AUDIT-OK] OF-added member (OF:517)
    public Map<Class<? extends Entity>, Render<? extends Entity>> getEntityRenderMap() {
        return this.entityRenderMap;
    }
}
