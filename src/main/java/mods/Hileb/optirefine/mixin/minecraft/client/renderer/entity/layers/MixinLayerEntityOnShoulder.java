package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(LayerEntityOnShoulder.class)
public abstract class MixinLayerEntityOnShoulder {
    @Shadow
    private UUID leftUniqueId;

    @WrapOperation(method = "renderEntityOnShoulder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    public void texture_renderEntityOnShoulder(RenderLivingBase<?> instance, ResourceLocation location, Operation<Void> original,
                                               @Share(namespace = "optirefine", value = "entity")LocalRef<Entity> entityLocalRef, @Local(argsOnly = true) EntityPlayer p_192864_1_, @Local(argsOnly = true) UUID p_192864_2_){
        Entity renderedEntityOld = RenderGlobal_renderedEntity_get(Config.getRenderGlobal());
        if (p_192864_1_ instanceof AbstractClientPlayer acp) {
            Entity entityShoulder = p_192864_2_ == this.leftUniqueId ? AbstractClientPlayer_entityShoulderLeft_get(acp) : AbstractClientPlayer_entityShoulderRight_get(acp);
            if (entityShoulder != null) {
                RenderGlobal_renderedEntity_set(Config.getRenderGlobal(), entityShoulder);
                if (Config.isShaders()) {
                    Shaders.nextEntity(entityShoulder);
                }
            }
        }
        entityLocalRef.set(renderedEntityOld);
    }

    @WrapOperation(method = "renderEntityOnShoulder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V"))
    public void end_renderEntityOnShoulder(Operation<Void> original,
                                               @Share(namespace = "optirefine", value = "entity")LocalRef<Entity> entityLocalRef){
        original.call();
        RenderGlobal_renderedEntity_set(Config.getRenderGlobal(), entityLocalRef.get());
        if (Config.isShaders()) {
            Shaders.nextEntity(entityLocalRef.get());
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
