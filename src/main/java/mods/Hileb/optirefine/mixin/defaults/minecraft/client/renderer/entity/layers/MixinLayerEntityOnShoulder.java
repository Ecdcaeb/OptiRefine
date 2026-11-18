package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.entity.G5ShoulderParrots;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import java.util.UUID;

@Mixin(LayerEntityOnShoulder.class)
public abstract class MixinLayerEntityOnShoulder {

    @WrapMethod(method = "doRenderLayer(Lnet/minecraft/entity/player/EntityPlayer;FFFFFFF)V")
    private void optiRefine$renderShoulders(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks,
                                           float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original) {
        if (player.getLeftShoulderEntity().isEmpty() && player.getRightShoulderEntity().isEmpty()) {
            original.call(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            return;
        }
        boolean rescale = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);
        try {
            original.call(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        } finally {
            if (rescale) GlStateManager.enableRescaleNormal();
            else GlStateManager.disableRescaleNormal();
        }
    }

    @Coerce
    @WrapMethod(method = "renderEntityOnShoulder")
    private Object optiRefine$renderShoulder(EntityPlayer player, UUID previousId, NBTTagCompound tag,
                                             RenderLivingBase<?> renderer, ModelBase model, ResourceLocation texture,
                                             Class<?> entityClass, float limbSwing, float limbSwingAmount,
                                             float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
                                             float scale, boolean left, Operation<Object> original,
                                             @Share("shoulderMatrix") LocalBooleanRef matrixPushed) {
        RenderGlobal renderGlobal = Config.getRenderGlobal();
        Entity previous = optiRefine$getRenderGlobalRenderedEntity(renderGlobal);
        Entity shoulder = G5ShoulderParrots.getShoulder(player, left);
        Program program = shoulder != null && Config.isShaders() && Shaders.isRenderingWorld
                ? Shaders.activeProgram : null;
        int entityId = program != null ? Shaders.uniform_entityId.getValue() : 0;
        optiRefine$setRenderGlobalRenderedEntity(renderGlobal, shoulder);
        try {
            if (program != null) Shaders.nextEntity(shoulder);
            // TextureManager consults G5 RandomEntities during the original RenderParrot texture bind.
            return original.call(player, previousId, tag, renderer, model, texture, entityClass, limbSwing,
                    limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, left);
        } finally {
            try {
                if (matrixPushed.get()) GlStateManager.popMatrix();
            } finally {
                optiRefine$setRenderGlobalRenderedEntity(renderGlobal, previous);
                if (program != null) {
                    Shaders.useProgram(program);
                    if (Shaders.uniform_entityId.isDefined()) Shaders.uniform_entityId.setValue(entityId);
                }
            }
        }
    }

    @WrapOperation(method = "renderEntityOnShoulder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V"))
    private void optiRefine$pushShoulderMatrix(Operation<Void> original, @Share("shoulderMatrix") LocalBooleanRef matrixPushed) {
        original.call();
        matrixPushed.set(true);
    }

    @WrapOperation(method = "renderEntityOnShoulder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V"))
    private void optiRefine$popShoulderMatrix(Operation<Void> original, @Share("shoulderMatrix") LocalBooleanRef matrixPushed) {
        original.call();
        matrixPushed.set(false);
    }

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderedEntity Lnet.minecraft.entity.Entity;")
    private static native Entity optiRefine$getRenderGlobalRenderedEntity(RenderGlobal renderGlobal);
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderedEntity Lnet.minecraft.entity.Entity;")
    private static native void optiRefine$setRenderGlobalRenderedEntity(RenderGlobal renderGlobal, Entity entity);
}
