package mods.Hileb.optirefine.mixin.defaults.minecraft.world.gen.layer;

import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerZoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Checked
@Mixin(GenLayerZoom.class)
public abstract class MixinGenLayerZoom extends GenLayer {
    @SuppressWarnings("unused")
    public MixinGenLayerZoom(long p_i2125_1_) {
        super(p_i2125_1_);
    }

    @Redirect(method = "getInts", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/layer/GenLayerZoom;selectRandom([I)I"))
    private int getInts$UseSelectRandom2(GenLayerZoom instance, int[] ints){
        return selectRandom2(ints[0], ints[1]);
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    protected int selectRandom2(int i0, int i1) {
        int index = this.nextInt(2);
        if (index == 0) {
            return i0;
        }
        return i1;
    }
}
/*
--- net/minecraft/world/gen/layer/GenLayerZoom.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/world/gen/layer/GenLayerZoom.java	Tue Aug 19 14:59:58 2025
@@ -23,14 +23,14 @@

          for (int var17 = var9[var15 + 0 + (var13 + 1) * var7]; var15 < var7 - 1; var15++) {
             this.initChunkSeed(var15 + var5 << 1, var13 + var6 << 1);
             int var18 = var9[var15 + 1 + (var13 + 0) * var7];
             int var19 = var9[var15 + 1 + (var13 + 1) * var7];
             var12[var14] = var16;
-            var12[var14++ + var10] = this.selectRandom(new int[]{var16, var17});
-            var12[var14] = this.selectRandom(new int[]{var16, var18});
+            var12[var14++ + var10] = this.selectRandom2(var16, var17);
+            var12[var14] = this.selectRandom2(var16, var18);
             var12[var14++ + var10] = this.selectModeOrRandom(var16, var18, var17, var19);
             var16 = var18;
             var17 = var19;
          }
       }

@@ -48,8 +48,13 @@

       for (int var5 = 0; var5 < var3; var5++) {
          var4 = new GenLayerZoom(var0 + var5, (GenLayer)var4);
       }

       return (GenLayer)var4;
+   }
+
+   protected int selectRandom2(int var1, int var2) {
+      int var3 = this.nextInt(2);
+      return var3 == 0 ? var1 : var2;
    }
 }
 */
