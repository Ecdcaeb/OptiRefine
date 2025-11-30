package mods.Hileb.optirefine.mixin.defaults.minecraft.client.model;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Checked
@Mixin(ModelPlayer.class)
public class MixinModelPlayer {
    @WrapOperation(method = "setRotationAngles", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelRenderer;rotationPointY:F", opcode = Opcodes.PUTFIELD))
    public void removeThebipedCape_rotationPointYSet(ModelRenderer instance, float value, Operation<Void> original){

    }

    @Redirect(method = "setRotationAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean removeThebipedCape_rotationPointYSet_if(Entity instance){
        return false;
    }

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
