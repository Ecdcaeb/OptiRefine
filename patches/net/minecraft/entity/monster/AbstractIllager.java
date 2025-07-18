package net.minecraft.entity.monster;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class AbstractIllager extends EntityMob {
   protected static final DataParameter<Byte> AGGRESSIVE = EntityDataManager.createKey(AbstractIllager.class, DataSerializers.BYTE);

   public AbstractIllager(World var1) {
      super(☃);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(AGGRESSIVE, (byte)0);
   }

   protected boolean isAggressive(int var1) {
      int ☃ = this.dataManager.get(AGGRESSIVE);
      return (☃ & ☃) != 0;
   }

   protected void setAggressive(int var1, boolean var2) {
      int ☃ = this.dataManager.get(AGGRESSIVE);
      if (☃) {
         ☃ |= ☃;
      } else {
         ☃ &= ~☃;
      }

      this.dataManager.set(AGGRESSIVE, (byte)(☃ & 0xFF));
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ILLAGER;
   }

   public AbstractIllager.IllagerArmPose getArmPose() {
      return AbstractIllager.IllagerArmPose.CROSSED;
   }

   public static enum IllagerArmPose {
      CROSSED,
      ATTACKING,
      SPELLCASTING,
      BOW_AND_ARROW;
   }
}
