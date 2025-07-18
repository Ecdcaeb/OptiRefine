package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public interface IItemPropertyGetter {
   float apply(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3);
}
