package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityCaveSpider extends EntitySpider {
   public EntityCaveSpider(World var1) {
      super(☃);
      this.setSize(0.7F, 0.5F);
   }

   public static void registerFixesCaveSpider(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityCaveSpider.class);
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0);
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      if (super.attackEntityAsMob(☃)) {
         if (☃ instanceof EntityLivingBase) {
            int ☃ = 0;
            if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
               ☃ = 7;
            } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
               ☃ = 15;
            }

            if (☃ > 0) {
               ((EntityLivingBase)☃).addPotionEffect(new PotionEffect(MobEffects.POISON, ☃ * 20, 0));
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      return ☃;
   }

   @Override
   public float getEyeHeight() {
      return 0.45F;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_CAVE_SPIDER;
   }
}
