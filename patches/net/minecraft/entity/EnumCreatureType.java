package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;

public enum EnumCreatureType {
   MONSTER(IMob.class, 70, Material.AIR, false, false),
   CREATURE(EntityAnimal.class, 10, Material.AIR, true, true),
   AMBIENT(EntityAmbientCreature.class, 15, Material.AIR, true, false),
   WATER_CREATURE(EntityWaterMob.class, 5, Material.WATER, true, false);

   private final Class<? extends IAnimals> creatureClass;
   private final int maxNumberOfCreature;
   private final Material creatureMaterial;
   private final boolean isPeacefulCreature;
   private final boolean isAnimal;

   private EnumCreatureType(Class<? extends IAnimals> var3, int var4, Material var5, boolean var6, boolean var7) {
      this.creatureClass = ☃;
      this.maxNumberOfCreature = ☃;
      this.creatureMaterial = ☃;
      this.isPeacefulCreature = ☃;
      this.isAnimal = ☃;
   }

   public Class<? extends IAnimals> getCreatureClass() {
      return this.creatureClass;
   }

   public int getMaxNumberOfCreature() {
      return this.maxNumberOfCreature;
   }

   public boolean getPeacefulCreature() {
      return this.isPeacefulCreature;
   }

   public boolean getAnimal() {
      return this.isAnimal;
   }
}
