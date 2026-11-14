package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;


import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.Program;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerEntityOnShoulder.class)
public abstract class MixinLayerEntityOnShoulder {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @WrapMethod(method = "doRenderLayer(Lnet/minecraft/entity/player/EntityPlayer;FFFFFFF)V")
    private void optiRefine$renderShoulders(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks,
                                           float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original) {
        RenderGlobal renderGlobal = Config.getRenderGlobal();
        Entity oldEntity = RenderGlobal_renderedEntity_get(renderGlobal);
        Program oldProgram = Config.isShaders() ? Shaders.activeProgram : null;
        try {
            original.call(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        } finally {
            RenderGlobal_renderedEntity_set(renderGlobal, oldEntity);
            if (oldProgram != null) {
                Shaders.setEntityId(oldEntity);
                Shaders.useProgram(oldProgram);
            }
        }
    }

    @WrapOperation(method = "renderEntityOnShoulder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    // [AUDIT-OK] renderEntityOnShoulder baseline (private, 15 args); bindTexture wrap + renderedEntity/nextEntity set matches OF:124-136
    public void texture_renderEntityOnShoulder(RenderLivingBase<?> instance, ResourceLocation location, Operation<Void> original,
                                               @Share(namespace = "optirefine", value = "entity") LocalRef<Entity> entityLocalRef,
                                               @Share(namespace = "optirefine", value = "program") LocalRef<Program> programLocalRef,
                                               @Local(argsOnly = true) EntityPlayer p_192864_1_, @Local(argsOnly = true) boolean leftShoulder) {
        // [AUDIT-OK] renderedEntity / entityShoulderLeft / entityShoulderRight are OF-added (not in tsrg) — MCP names correct; provided by MixinRenderGlobal @Public and MixinAbstractClientPlayer
        Entity renderedEntityOld = RenderGlobal_renderedEntity_get(Config.getRenderGlobal());
        entityLocalRef.set(renderedEntityOld);
        programLocalRef.set(Config.isShaders() ? Shaders.activeProgram : null);
        if (p_192864_1_ instanceof AbstractClientPlayer acp) {
            Entity entityShoulder = leftShoulder ? AbstractClientPlayer_entityShoulderLeft_get(acp) : AbstractClientPlayer_entityShoulderRight_get(acp);
            if (entityShoulder != null) {
                RenderGlobal_renderedEntity_set(Config.getRenderGlobal(), entityShoulder);
                if (Config.isShaders()) {
                    Shaders.nextEntity(entityShoulder);
                }
            }
        }
        original.call(instance, location);
    }

    @WrapOperation(method = "renderEntityOnShoulder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void end_renderEntityOnShoulder(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original,
                                               @Share(namespace = "optirefine", value = "entity") LocalRef<Entity> entityLocalRef,
                                               @Share(namespace = "optirefine", value = "program") LocalRef<Program> programLocalRef) {
        try {
            original.call(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        } finally {
            RenderGlobal_renderedEntity_set(Config.getRenderGlobal(), entityLocalRef.get());
            if (programLocalRef.get() != null) {
                Shaders.setEntityId(entityLocalRef.get());
                Shaders.useProgram(programLocalRef.get());
            }
        }
    }



    
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderedEntity Lnet.minecraft.entity.Entity;")
    private static native Entity RenderGlobal_renderedEntity_get(RenderGlobal renderGlobal);

    
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderedEntity Lnet.minecraft.entity.Entity;")
    private static native void RenderGlobal_renderedEntity_set(RenderGlobal renderGlobal, Entity entity);

    
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.entity.AbstractClientPlayer entityShoulderLeft Lnet.minecraft.entity.passive.EntityShoulderRiding;")
    private static native EntityShoulderRiding AbstractClientPlayer_entityShoulderLeft_get(AbstractClientPlayer abstractClientPlayer);

    
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.entity.AbstractClientPlayer entityShoulderRight Lnet.minecraft.entity.passive.EntityShoulderRiding;")
    private static native EntityShoulderRiding AbstractClientPlayer_entityShoulderRight_get(AbstractClientPlayer abstractClientPlayer);
}
