package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemOverrideList {
   public static final ItemOverrideList NONE = new ItemOverrideList();
   private final List<ItemOverride> overrides = Lists.newArrayList();

   private ItemOverrideList() {
   }

   public ItemOverrideList(List<ItemOverride> var1) {
      for (int ☃ = ☃.size() - 1; ☃ >= 0; ☃--) {
         this.overrides.add(☃.get(☃));
      }
   }

   @Nullable
   public ResourceLocation applyOverride(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
      if (!this.overrides.isEmpty()) {
         for (ItemOverride ☃ : this.overrides) {
            if (☃.matchesItemStack(☃, ☃, ☃)) {
               return ☃.getLocation();
            }
         }
      }

      return null;
   }
}
