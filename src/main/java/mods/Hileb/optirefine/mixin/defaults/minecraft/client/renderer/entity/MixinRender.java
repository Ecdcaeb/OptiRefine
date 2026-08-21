package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// [AUDIT-FIXED 2026-08-21] @Implements prefix must be unique to the interface methods.
// Mixin 0.8.7 treats EVERY method starting with the prefix as an interface impl; optiRefine$customTexture
// (WrapOperation) was renamed to customTexture and crashed apply ("does not exist in IEntityRenderer").
@Implements(@Interface(iface = net.optifine.entity.model.IEntityRenderer.class, prefix = "ier$"))
@Mixin(Render.class)
public abstract class MixinRender {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    // [AUDIT-OK] OF-added fields entityClass/locationTextureCustom (OF Render:35-36)
    @Unique
    // [AUDIT-FIXED] three-way audit (P13): dropped '= null' initializer + added @Unique per convention
    // (cleanmix does not inject instance-field initializers; default null == OF:35 semantics)
    private Class<? extends Entity> entityClass;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private ResourceLocation locationTextureCustom;

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

    @Inject(method = "renderEntityOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()V", shift = At.Shift.AFTER))
    // [AUDIT-FIXED] cleanup AFTER Tessellator.draw (INVOKE fires pre-call by default; OF:163-164 cleans up after draw)
    public void after_renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci, @Share(namespace = "optirefine", value = "multitexture")LocalBooleanRef multitextureRef, @Local(ordinal = 0) BufferBuilder builder){
        if (multitextureRef.get()) {
            BufferBuilder_setBlockLayer(builder, null);
            GlStateManager_bindCurrentTexture();
        }
    }

    @Inject(method = "renderEntityOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    // [AUDIT-FIXED] 2026-08-05: OF:138 sets the per-iteration fire sprite before the quads
    // (multi-texture mode); missing -> fire quads carry a stale sprite. @Local ordinal=2 = the
    // loop-local sprite (textureatlassprite2), third TextureAtlasSprite local in the method.
    public void setFireSprite_renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci, @Local(ordinal = 2) net.minecraft.client.renderer.texture.TextureAtlasSprite sprite, @Local(ordinal = 0) BufferBuilder builder){
        if (Config.isMultiTexture()) {
            BufferBuilder_setSprite(builder, sprite);
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder setSprite (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
    private static native void BufferBuilder_setSprite(BufferBuilder builder, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite);

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager bindCurrentTexture ()V")
    private static native void GlStateManager_bindCurrentTexture();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] OF-added IEntityRenderer members (OF:334-346); prefix stripped on merge -> getEntityClass
    public Class<? extends Entity> ier$getEntityClass() {
        return this.entityClass;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void ier$setEntityClass(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public ResourceLocation ier$getLocationTextureCustom() {
        return this.locationTextureCustom;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void ier$setLocationTextureCustom(ResourceLocation locationTextureCustom) {
        this.locationTextureCustom = locationTextureCustom;
    }

    @WrapMethod(method = "renderShadow")
    // [AUDIT-OK] renderShadow baseline; shouldSkipDefaultShadow guard matches OF:172
    public void is_renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks, Operation<Void> original){
        if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow) {
            original.call(entityIn, x, y, z, shadowAlpha, partialTicks);
        }
    }


    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.locationTextureCustom = null;
    }
}
