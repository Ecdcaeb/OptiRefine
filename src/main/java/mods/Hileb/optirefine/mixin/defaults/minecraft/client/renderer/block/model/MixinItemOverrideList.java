package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.ItemOverrideCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemOverrideList.class)
public abstract class MixinItemOverrideList {
    @Shadow @Final
    private List<ItemOverride> overrides = Lists.newArrayList();
    @Unique
    private ItemOverrideCache itemOverrideCache;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    public void init(List<ItemOverride> overrides, CallbackInfo ci){
        if (this.overrides.size() > 65) {
            this.itemOverrideCache = ItemOverrideCache.make(this.overrides);
        }
    }

    @WrapMethod(method = "applyOverride")
    public ResourceLocation applyCache(ItemStack stack, World worldIn, EntityLivingBase entityIn, Operation<ResourceLocation> original){
        if (!this.overrides.isEmpty()) {
            if (this.itemOverrideCache != null) {
                ResourceLocation locationCached = this.itemOverrideCache.getModelLocation(stack, worldIn, entityIn);
                if (locationCached != null) {
                    return locationCached == ItemOverrideCache.LOCATION_NULL ? null : locationCached;
                }
            }

            ResourceLocation resourceLocation = original.call(stack, worldIn, entityIn);
            if (resourceLocation != null) {
                if (this.itemOverrideCache != null) {
                    this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, resourceLocation);
                }

                return resourceLocation;
            }

            if (this.itemOverrideCache != null) {
                this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, ItemOverrideCache.LOCATION_NULL);
            }
        }

        return null;
    }


}
/*
+++ net/minecraft/client/renderer/block/model/ItemOverrideList.java	Tue Aug 19 14:59:58 2025
@@ -1,36 +1,76 @@
 package net.minecraft.client.renderer.block.model;

+import com.google.common.collect.ImmutableList;
 import com.google.common.collect.Lists;
 import java.util.List;
 import javax.annotation.Nullable;
+import net.minecraft.client.Minecraft;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.world.World;
+import net.optifine.ItemOverrideCache;
+import net.optifine.reflect.Reflector;

 public class ItemOverrideList {
    public static final ItemOverrideList NONE = new ItemOverrideList();
    private final List<ItemOverride> overrides = Lists.newArrayList();
+   private ItemOverrideCache itemOverrideCache;

    private ItemOverrideList() {
    }

    public ItemOverrideList(List<ItemOverride> var1) {
       for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
          this.overrides.add((ItemOverride)var1.get(var2));
       }
+
+      if (this.overrides.size() > 65) {
+         this.itemOverrideCache = ItemOverrideCache.make(this.overrides);
+      }
    }

    @Nullable
    public ResourceLocation applyOverride(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
       if (!this.overrides.isEmpty()) {
+         if (this.itemOverrideCache != null) {
+            ResourceLocation var4 = this.itemOverrideCache.getModelLocation(var1, var2, var3);
+            if (var4 != null) {
+               return var4 == ItemOverrideCache.LOCATION_NULL ? null : var4;
+            }
+         }
+
          for (ItemOverride var5 : this.overrides) {
             if (var5.matchesItemStack(var1, var2, var3)) {
+               if (this.itemOverrideCache != null) {
+                  this.itemOverrideCache.putModelLocation(var1, var2, var3, var5.getLocation());
+               }
+
                return var5.getLocation();
             }
          }
+
+         if (this.itemOverrideCache != null) {
+            this.itemOverrideCache.putModelLocation(var1, var2, var3, ItemOverrideCache.LOCATION_NULL);
+         }
       }

       return null;
+   }
+
+   public IBakedModel handleItemState(IBakedModel var1, ItemStack var2, @Nullable World var3, @Nullable EntityLivingBase var4) {
+      if (!var2.isEmpty() && var2.getItem().hasCustomProperties()) {
+         ResourceLocation var5 = this.applyOverride(var2, var3, var4);
+         if (var5 != null && Reflector.ModelLoader_getInventoryVariant.exists()) {
+            ModelResourceLocation var6 = (ModelResourceLocation)Reflector.call(Reflector.ModelLoader_getInventoryVariant, new Object[]{var5.toString()});
+            return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(var6);
+         }
+      }
+
+      return var1;
+   }
+
+   public ImmutableList<ItemOverride> getOverrides() {
+      return ImmutableList.copyOf(this.overrides);
    }
 }
 */
