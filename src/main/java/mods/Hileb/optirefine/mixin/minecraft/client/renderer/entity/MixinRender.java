package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
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

@Mixin(Render.class)
public class MixinRender {
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private final Class<? extends Entity> entityClass = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private final ResourceLocation locationTextureCustom = null;

    @Inject(method = "renderEntityOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V"))
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
    public void after_renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci, @Share(namespace = "optirefine", value = "multitexture")LocalBooleanRef multitextureRef, @Local(ordinal = 0) BufferBuilder builder){
        if (multitextureRef.get()) {
            BufferBuilder_setBlockLayer(builder, null);
            GlStateManager_bindCurrentTexture();
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager bindCurrentTexture ()V")
    private static native void GlStateManager_bindCurrentTexture();

    @WrapMethod(method = "renderShadow")
    public void is_renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks, Operation<Void> original){
        if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow) {
            original.call(entityIn, x, y, z, shadowAlpha, partialTicks);
        }
    }

}
