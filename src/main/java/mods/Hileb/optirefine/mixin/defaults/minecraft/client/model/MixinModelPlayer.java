package mods.Hileb.optirefine.mixin.defaults.minecraft.client.model;

import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModelPlayer.class)
public class MixinModelPlayer {

}
/*
+++ net/minecraft/client/model/ModelPlayer.java	Tue Aug 19 14:59:58 2025
@@ -103,17 +103,12 @@
       super.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
       copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
       copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
       copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
       copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
       copyModelAngles(this.bipedBody, this.bipedBodyWear);
-      if (var7.isSneaking()) {
-         this.bipedCape.rotationPointY = 2.0F;
-      } else {
-         this.bipedCape.rotationPointY = 0.0F;
-      }
    }

    public void setVisible(boolean var1) {
       super.setVisible(var1);
       this.bipedLeftArmwear.showModel = var1;
       this.bipedRightArmwear.showModel = var1;
 */
