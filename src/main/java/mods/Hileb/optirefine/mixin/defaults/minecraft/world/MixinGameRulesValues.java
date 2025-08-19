package mods.Hileb.optirefine.mixin.defaults.minecraft.world;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.GameRules$Value")
public abstract class MixinGameRulesValues {

    @SuppressWarnings("unused")
    @Shadow
    private String valueString;

    @SuppressWarnings("unused")
    @Shadow
    private boolean valueBoolean;

    @Inject(method = "setValue", at = @At("HEAD"), cancellable = true)
    public void __setValue(String value, CallbackInfo ci){
        this.valueString = value;
        if (value != null) {
            if (value.equals("false")) {
                this.valueBoolean = false;
                ci.cancel();
            }
            if (value.equals("true")) {
                this.valueBoolean = true;
                ci.cancel();
            }
        }
    }

}

/*
+++ net/minecraft/world/GameRules.java	Tue Aug 19 14:59:58 2025
@@ -71,14 +71,14 @@
       }

       return var1;
    }

    public void readFromNBT(NBTTagCompound var1) {
-      for (String var4 : var1.getKeySet()) {
-         this.setOrCreateGameRule(var4, var1.getString(var4));
+      for (String var3 : var1.getKeySet()) {
+         this.setOrCreateGameRule(var3, var1.getString(var3));
       }
    }

    public String[] getRules() {
       Set var1 = this.rules.keySet();
       return var1.toArray(new String[var1.size()]);
@@ -104,12 +104,24 @@
          this.type = var2;
          this.setValue(var1);
       }

       public void setValue(String var1) {
          this.valueString = var1;
+         if (var1 != null) {
+            if (var1.equals("false")) {
+               this.valueBoolean = false;
+               return;
+            }
+
+            if (var1.equals("true")) {
+               this.valueBoolean = true;
+               return;
+            }
+         }
+
          this.valueBoolean = Boolean.parseBoolean(var1);
          this.valueInteger = this.valueBoolean ? 1 : 0;

          try {
             this.valueInteger = Integer.parseInt(var1);
          } catch (NumberFormatException var4) {
 */
