package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RenderPotion extends RenderSnowball<EntityPotion> {
   public RenderPotion(RenderManager var1, RenderItem var2) {
      super(☃, Items.POTIONITEM, ☃);
   }

   public ItemStack getStackToRender(EntityPotion var1) {
      return ☃.getPotion();
   }
}
