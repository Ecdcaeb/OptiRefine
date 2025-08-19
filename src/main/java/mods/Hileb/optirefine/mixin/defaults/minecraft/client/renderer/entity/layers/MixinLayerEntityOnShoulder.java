package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

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
/*
+++ net/minecraft/client/renderer/entity/layers/LayerEntityOnShoulder.java	Tue Aug 19 14:59:58 2025
@@ -1,22 +1,26 @@
 package net.minecraft.client.renderer.entity.layers;

 import java.util.UUID;
 import javax.annotation.Nullable;
+import net.minecraft.client.entity.AbstractClientPlayer;
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.model.ModelParrot;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.RenderLivingBase;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.entity.RenderParrot;
+import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityList;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.passive.EntityParrot;
+import net.minecraft.entity.passive.EntityShoulderRiding;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.shaders.Shaders;

 public class LayerEntityOnShoulder implements LayerRenderer<EntityPlayer> {
    private final RenderManager renderManager;
    protected RenderLivingBase<? extends EntityLivingBase> leftRenderer;
    private ModelBase leftModel;
    private ResourceLocation leftResource;
@@ -116,25 +120,42 @@
             var4 = new RenderParrot(this.renderManager);
             var5 = new ModelParrot();
             var6 = RenderParrot.PARROT_TEXTURES[var3.getInteger("Variant")];
          }
       }

+      Entity var16 = Config.getRenderGlobal().renderedEntity;
+      if (var1 instanceof AbstractClientPlayer) {
+         AbstractClientPlayer var17 = (AbstractClientPlayer)var1;
+         EntityShoulderRiding var18 = var2 == this.leftUniqueId ? var17.entityShoulderLeft : var17.entityShoulderRight;
+         if (var18 != null) {
+            Config.getRenderGlobal().renderedEntity = var18;
+            if (Config.isShaders()) {
+               Shaders.nextEntity(var18);
+            }
+         }
+      }
+
       ((RenderLivingBase)var4).bindTexture(var6);
       GlStateManager.pushMatrix();
-      float var16 = var1.isSneaking() ? -1.3F : -1.5F;
-      float var17 = var15 ? 0.4F : -0.4F;
-      GlStateManager.translate(var17, var16, 0.0F);
+      float var19 = var1.isSneaking() ? -1.3F : -1.5F;
+      float var20 = var15 ? 0.4F : -0.4F;
+      GlStateManager.translate(var20, var19, 0.0F);
       if (var7 == EntityParrot.class) {
          var11 = 0.0F;
       }

-      ((ModelBase)var5).setLivingAnimations(var1, var8, var9, var10);
-      ((ModelBase)var5).setRotationAngles(var8, var9, var11, var12, var13, var14, var1);
-      ((ModelBase)var5).render(var1, var8, var9, var11, var12, var13, var14);
+      var5.setLivingAnimations(var1, var8, var9, var10);
+      var5.setRotationAngles(var8, var9, var11, var12, var13, var14, var1);
+      var5.render(var1, var8, var9, var11, var12, var13, var14);
       GlStateManager.popMatrix();
+      Config.getRenderGlobal().renderedEntity = var16;
+      if (Config.isShaders()) {
+         Shaders.nextEntity(var16);
+      }
+
       return new LayerEntityOnShoulder.DataHolder(var2, (RenderLivingBase<? extends EntityLivingBase>)var4, (ModelBase)var5, var6, var7);
    }

    public boolean shouldCombineTextures() {
       return false;
    }
 */
