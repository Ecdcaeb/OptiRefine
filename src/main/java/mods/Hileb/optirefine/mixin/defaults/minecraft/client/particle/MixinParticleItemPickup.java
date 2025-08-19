package mods.Hileb.optirefine.mixin.defaults.minecraft.client.particle;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.particle.ParticleItemPickup;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleItemPickup.class)
public abstract class MixinParticleItemPickup {
    @Shadow
    @Final
    private Entity item;

    @Inject(method = "renderParticle", at = @At("HEAD"))
    public void preInjectRenderParticle(BufferBuilder buffer,
                                        Entity entityIn,
                                        float partialTicks,
                                        float rotationX, float rotationZ, float rotationYZ,
                                        float rotationXY, float rotationXZ,
                                        CallbackInfo ci, @Share("oldShadersProgram")LocalRef<Program> oldShadersProgram){
        oldShadersProgram.set(null);
        if (Config.isShaders()) {
            oldShadersProgram.set(Shaders.activeProgram);
            Shaders.nextEntity(this.item);
        }

    }

    @Inject(method = "renderParticle", at = @At("RETURN"))
    public void postInjectRenderParticle(BufferBuilder buffer,
                                        Entity entityIn,
                                        float partialTicks,
                                        float rotationX, float rotationZ, float rotationYZ,
                                        float rotationXY, float rotationXZ,
                                        CallbackInfo ci, @Share("oldShadersProgram")LocalRef<Program> oldShadersProgram){
        if (Config.isShaders()) {
            Shaders.setEntityId(null);
            Shaders.useProgram(oldShadersProgram.get());
        }
    }

}
/*
+++ net/minecraft/client/particle/ParticleItemPickup.java	Tue Aug 19 14:59:58 2025
@@ -4,12 +4,14 @@
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.entity.Entity;
 import net.minecraft.world.World;
+import net.optifine.shaders.Program;
+import net.optifine.shaders.Shaders;

 public class ParticleItemPickup extends Particle {
    private final Entity item;
    private final Entity target;
    private int age;
    private final int maxAge;
@@ -22,33 +24,43 @@
       this.target = var3;
       this.maxAge = 3;
       this.yOffset = var4;
    }

    public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
-      float var9 = (this.age + var3) / this.maxAge;
-      var9 *= var9;
-      double var10 = this.item.posX;
-      double var12 = this.item.posY;
-      double var14 = this.item.posZ;
-      double var16 = this.target.lastTickPosX + (this.target.posX - this.target.lastTickPosX) * var3;
-      double var18 = this.target.lastTickPosY + (this.target.posY - this.target.lastTickPosY) * var3 + this.yOffset;
-      double var20 = this.target.lastTickPosZ + (this.target.posZ - this.target.lastTickPosZ) * var3;
-      double var22 = var10 + (var16 - var10) * var9;
-      double var24 = var12 + (var18 - var12) * var9;
-      double var26 = var14 + (var20 - var14) * var9;
-      int var28 = this.getBrightnessForRender(var3);
-      int var29 = var28 % 65536;
-      int var30 = var28 / 65536;
-      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var29, var30);
+      Program var9 = null;
+      if (Config.isShaders()) {
+         var9 = Shaders.activeProgram;
+         Shaders.nextEntity(this.item);
+      }
+
+      float var10 = (this.age + var3) / this.maxAge;
+      var10 *= var10;
+      double var11 = this.item.posX;
+      double var13 = this.item.posY;
+      double var15 = this.item.posZ;
+      double var17 = this.target.lastTickPosX + (this.target.posX - this.target.lastTickPosX) * var3;
+      double var19 = this.target.lastTickPosY + (this.target.posY - this.target.lastTickPosY) * var3 + this.yOffset;
+      double var21 = this.target.lastTickPosZ + (this.target.posZ - this.target.lastTickPosZ) * var3;
+      double var23 = var11 + (var17 - var11) * var10;
+      double var25 = var13 + (var19 - var13) * var10;
+      double var27 = var15 + (var21 - var15) * var10;
+      int var29 = this.getBrightnessForRender(var3);
+      int var30 = var29 % 65536;
+      int var31 = var29 / 65536;
+      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var30, var31);
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-      var22 -= interpPosX;
-      var24 -= interpPosY;
-      var26 -= interpPosZ;
+      var23 -= interpPosX;
+      var25 -= interpPosY;
+      var27 -= interpPosZ;
       GlStateManager.enableLighting();
-      this.renderManager.renderEntity(this.item, var22, var24, var26, this.item.rotationYaw, var3, false);
+      this.renderManager.renderEntity(this.item, var23, var25, var27, this.item.rotationYaw, var3, false);
+      if (Config.isShaders()) {
+         Shaders.setEntityId(null);
+         Shaders.useProgram(var9);
+      }
    }

    public void onUpdate() {
       this.age++;
       if (this.age == this.maxAge) {
          this.setExpired();
 */
