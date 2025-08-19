package mods.Hileb.optirefine.mixin.defaults.minecraft.potion;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(PotionUtils.class)
public abstract class MixinPotionUtil {

    @Redirect(method = "getPotionColorFromEffectList", at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getLiquidColor()I"))
    private static int injectGetPotionColorFromEffectList(Potion instance){
        int k = instance.getLiquidColor();
        if (Config.isCustomColors()) {
            return CustomColors.getPotionColor(instance, k);
        }
        return k;
    }

    @WrapMethod(method = "getPotionColorFromEffectList")
    private static int wrapGetPotionColorFromEffectList(Collection<PotionEffect> p_185181_0_, Operation<Integer> original){
        if (p_185181_0_.isEmpty()) {
            return Config.isCustomColors() ? CustomColors.getPotionColor(null, 3694022) : 3694022;
        } else return original.call(p_185181_0_);
    }



}
/*
-- net/minecraft/potion/PotionUtils.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/potion/PotionUtils.java	Tue Aug 19 14:59:58 2025
@@ -15,12 +15,13 @@
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.nbt.NBTTagList;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.Tuple;
 import net.minecraft.util.text.TextFormatting;
 import net.minecraft.util.text.translation.I18n;
+import net.optifine.CustomColors;

 public class PotionUtils {
    public static List<PotionEffect> getEffectsFromStack(ItemStack var0) {
       return getEffectsFromTag(var0.getTagCompound());
    }

@@ -75,22 +76,26 @@
       return var0 == PotionTypes.EMPTY ? 16253176 : getPotionColorFromEffectList(var0.getEffects());
    }

    public static int getPotionColorFromEffectList(Collection<PotionEffect> var0) {
       int var1 = 3694022;
       if (var0.isEmpty()) {
-         return 3694022;
+         return Config.isCustomColors() ? CustomColors.getPotionColor(null, var1) : 3694022;
       } else {
          float var2 = 0.0F;
          float var3 = 0.0F;
          float var4 = 0.0F;
          int var5 = 0;

          for (PotionEffect var7 : var0) {
             if (var7.doesShowParticles()) {
                int var8 = var7.getPotion().getLiquidColor();
+               if (Config.isCustomColors()) {
+                  var8 = CustomColors.getPotionColor(var7.getPotion(), var8);
+               }
+
                int var9 = var7.getAmplifier() + 1;
                var2 += var9 * (var8 >> 16 & 0xFF) / 255.0F;
                var3 += var9 * (var8 >> 8 & 0xFF) / 255.0F;
                var4 += var9 * (var8 >> 0 & 0xFF) / 255.0F;
                var5 += var9;
             }
@@ -113,19 +118,19 @@

    public static PotionType getPotionTypeFromNBT(@Nullable NBTTagCompound var0) {
       return var0 == null ? PotionTypes.EMPTY : PotionType.getPotionTypeForName(var0.getString("Potion"));
    }

    public static ItemStack addPotionToItemStack(ItemStack var0, PotionType var1) {
-      ResourceLocation var2 = PotionType.REGISTRY.getNameForObject(var1);
+      ResourceLocation var2 = (ResourceLocation)PotionType.REGISTRY.getNameForObject(var1);
       if (var1 == PotionTypes.EMPTY) {
          if (var0.hasTagCompound()) {
             NBTTagCompound var3 = var0.getTagCompound();
             var3.removeTag("Potion");
             if (var3.isEmpty()) {
-               var0.setTagCompound(null);
+               var0.setTagCompound((NBTTagCompound)null);
             }
          }
       } else {
          NBTTagCompound var4 = var0.hasTagCompound() ? var0.getTagCompound() : new NBTTagCompound();
          var4.setString("Potion", var2.toString());
          var0.setTagCompound(var4);
@@ -165,13 +170,13 @@
             if (!var9.isEmpty()) {
                for (Entry var11 : var9.entrySet()) {
                   AttributeModifier var12 = (AttributeModifier)var11.getValue();
                   AttributeModifier var13 = new AttributeModifier(
                      var12.getName(), var8.getAttributeModifierAmount(var6.getAmplifier(), var12), var12.getOperation()
                   );
-                  var4.add(new Tuple<>(((IAttribute)var11.getKey()).getName(), var13));
+                  var4.add(new Tuple(((IAttribute)var11.getKey()).getName(), var13));
                }
             }

             if (var6.getAmplifier() > 0) {
                var7 = var7 + " " + I18n.translateToLocal("potion.potency." + var6.getAmplifier()).trim();
             }
 */
