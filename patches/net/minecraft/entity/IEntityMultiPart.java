package net.minecraft.entity;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IEntityMultiPart {
   World getWorld();

   boolean attackEntityFromPart(MultiPartEntityPart var1, DamageSource var2, float var3);
}
