package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Implements;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Implements(net.optifine.entity.model.IEntityRenderer.class)
@Mixin(Render.class)
public abstract class MixinRender {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    // [AUDIT-OK] OF-added fields entityClass/locationTextureCustom (OF Render:35-36)
    private Class<? extends Entity> entityClass = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private ResourceLocation locationTextureCustom = null;

    @SuppressWarnings("unused")
    @AccessTransformer(name = "shadowSize", access = org.objectweb.asm.Opcodes.ACC_PUBLIC)
    // [AUDIT-OK] AT shadowSize: vanilla protected (deobf:33) -> public, matches OF:32
    public float acc_shadowSize;

    @WrapOperation(method = "bindEntityTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;getEntityTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/ResourceLocation;"))
    // [AUDIT-OK] bindEntityTexture/getEntityTexture baseline; locationTextureCustom override matches OF:91-96
    private ResourceLocation optiRefine$customTexture(Render instance, Entity entity, Operation<ResourceLocation> original) {
        return this.locationTextureCustom != null ? this.locationTextureCustom : original.call(instance, entity);
    }

    @Inject(method = "renderEntityOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V"))
    // [AUDIT-OK] renderEntityOnFire baseline; setBlockLayer(SOLID) before begin matches OF:129-131
    public void before_renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci, @Share(namespace = "optirefine", value = "multitexture")LocalBooleanRef multitextureRef, @Local(ordinal = 0) BufferBuilder builder){
        boolean multitexture = Config.isMultiTexture();
        if (multitexture) {
            BufferBuilder_setBlockLayer(builder, BlockRenderLayer.SOLID);
        }
        multitextureRef.set(multitexture);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder setBlockLayer (Lnet.minecraft.util.BlockRenderLayer;)V")
    private static native void BufferBuilder_setBlockLayer(BufferBuilder builder, BlockRenderLayer layer);

    @Inject(method = "renderEntityOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()V"))
    // [AUDIT-OK] setBlockLayer(null)+bindCurrentTexture after draw matches OF:163-164
    public void after_renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci, @Share(namespace = "optirefine", value = "multitexture")LocalBooleanRef multitextureRef, @Local(ordinal = 0) BufferBuilder builder){
        if (multitextureRef.get()) {
            BufferBuilder_setBlockLayer(builder, null);
            GlStateManager_bindCurrentTexture();
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager bindCurrentTexture ()V")
    private static native void GlStateManager_bindCurrentTexture();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added IEntityRenderer members (OF:334-346)
    public Class<? extends Entity> getEntityClass() {
        return this.entityClass;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void setEntityClass(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    public ResourceLocation getLocationTextureCustom() {
        return this.locationTextureCustom;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void setLocationTextureCustom(ResourceLocation locationTextureCustom) {
        this.locationTextureCustom = locationTextureCustom;
    }

    @WrapMethod(method = "renderShadow")
    // [AUDIT-OK] renderShadow baseline; shouldSkipDefaultShadow guard matches OF:172
    public void is_renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks, Operation<Void> original){
        if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow) {
            original.call(entityIn, x, y, z, shadowAlpha, partialTicks);
        }
    }

}
