package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGiantZombie extends EntityMob {
   public EntityGiantZombie(World var1) {
      super(☃);
      this.setSize(this.width * 6.0F, this.height * 6.0F);
   }

   public static void registerFixesGiantZombie(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityGiantZombie.class);
   }

   @Override
   public float getEyeHeight() {
      return 10.440001F;
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0);
   }

   @Override
   public float getBlockPathWeight(BlockPos var1) {
      return this.world.getLightBrightness(☃) - 0.5F;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_GIANT;
   }
}
