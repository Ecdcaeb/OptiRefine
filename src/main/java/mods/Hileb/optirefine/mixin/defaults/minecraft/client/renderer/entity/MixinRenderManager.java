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

    @Unique
    public Render<?> renderRender = null;

    @Shadow
    @Final
    private Map<String, RenderPlayer> skinMap;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(TextureManager p_i46180_1, RenderItem p_i46180_2, CallbackInfo ci){
        PlayerItemsLayer.register(this.skinMap);
    }

    @WrapOperation(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;doRender(Lnet/minecraft/entity/Entity;DDDFF)V"))
    public void beforeDoRender(Render<Entity> instance, Entity entity, double x, double y, double z, float entityYaw, float partialTicks, Operation<Void> original){
        if (CustomEntityModels.isActive()) {
            this.renderRender = instance;
        }
        original.call(instance, entity, x, y, z, entityYaw, partialTicks);
    }

    @WrapMethod(method = "renderDebugBoundingBox")
    public void blockRenderDebugBoundingBox(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks, Operation<Void> original){
        if (!Shaders.isShadowPass) {
            original.call(entityIn, x, y, z, entityYaw, partialTicks);
        }
    }

    @Shadow @Final
    public Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap;

    @Unique
    public Map<Class<? extends Entity>, Render<? extends Entity>> getEntityRenderMap() {
        return this.entityRenderMap;
    }
}
