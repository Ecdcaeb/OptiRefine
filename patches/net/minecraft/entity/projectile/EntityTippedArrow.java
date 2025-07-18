package net.minecraft.entity.projectile;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityTippedArrow extends EntityArrow {
   private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityTippedArrow.class, DataSerializers.VARINT);
   private PotionType potion = PotionTypes.EMPTY;
   private final Set<PotionEffect> customPotionEffects = Sets.newHashSet();
   private boolean fixedColor;

   public EntityTippedArrow(World var1) {
      super(☃);
   }

   public EntityTippedArrow(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public EntityTippedArrow(World var1, EntityLivingBase var2) {
      super(☃, ☃);
   }

   public void setPotionEffect(ItemStack var1) {
      if (☃.getItem() == Items.TIPPED_ARROW) {
         this.potion = PotionUtils.getPotionFromItem(☃);
         Collection<PotionEffect> ☃ = PotionUtils.getFullEffectsFromItem(☃);
         if (!☃.isEmpty()) {
            for (PotionEffect ☃x : ☃) {
               this.customPotionEffects.add(new PotionEffect(☃x));
            }
         }

         int ☃x = getCustomColor(☃);
         if (☃x == -1) {
            this.refreshColor();
         } else {
            this.setFixedColor(☃x);
         }
      } else if (☃.getItem() == Items.ARROW) {
         this.potion = PotionTypes.EMPTY;
         this.customPotionEffects.clear();
         this.dataManager.set(COLOR, -1);
      }
   }

   public static int getCustomColor(ItemStack var0) {
      NBTTagCompound ☃ = ☃.getTagCompound();
      return ☃ != null && ☃.hasKey("CustomPotionColor", 99) ? ☃.getInteger("CustomPotionColor") : -1;
   }

   private void refreshColor() {
      this.fixedColor = false;
      this.dataManager.set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects)));
   }

   public void addEffect(PotionEffect var1) {
      this.customPotionEffects.add(☃);
      this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects)));
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(COLOR, -1);
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.world.isRemote) {
         if (this.inGround) {
            if (this.timeInGround % 5 == 0) {
               this.spawnPotionParticles(1);
            }
         } else {
            this.spawnPotionParticles(2);
         }
      } else if (this.inGround && this.timeInGround != 0 && !this.customPotionEffects.isEmpty() && this.timeInGround >= 600) {
         this.world.setEntityState(this, (byte)0);
         this.potion = PotionTypes.EMPTY;
         this.customPotionEffects.clear();
         this.dataManager.set(COLOR, -1);
      }
   }

   private void spawnPotionParticles(int var1) {
      int ☃ = this.getColor();
      if (☃ != -1 && ☃ > 0) {
         double ☃x = (☃ >> 16 & 0xFF) / 255.0;
         double ☃xx = (☃ >> 8 & 0xFF) / 255.0;
         double ☃xxx = (☃ >> 0 & 0xFF) / 255.0;

         for (int ☃xxxx = 0; ☃xxxx < ☃; ☃xxxx++) {
            this.world
               .spawnParticle(
                  EnumParticleTypes.SPELL_MOB,
                  this.posX + (this.rand.nextDouble() - 0.5) * this.width,
                  this.posY + this.rand.nextDouble() * this.height,
                  this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
                  ☃x,
                  ☃xx,
                  ☃xxx
               );
         }
      }
   }

   public int getColor() {
      return this.dataManager.get(COLOR);
   }

   private void setFixedColor(int var1) {
      this.fixedColor = true;
      this.dataManager.set(COLOR, ☃);
   }

   public static void registerFixesTippedArrow(DataFixer var0) {
      EntityArrow.registerFixesArrow(☃, "TippedArrow");
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      if (this.potion != PotionTypes.EMPTY && this.potion != null) {
         ☃.setString("Potion", PotionType.REGISTRY.getNameForObject(this.potion).toString());
      }

      if (this.fixedColor) {
         ☃.setInteger("Color", this.getColor());
      }

      if (!this.customPotionEffects.isEmpty()) {
         NBTTagList ☃ = new NBTTagList();

         for (PotionEffect ☃x : this.customPotionEffects) {
            ☃.appendTag(☃x.writeCustomPotionEffectToNBT(new NBTTagCompound()));
         }

         ☃.setTag("CustomPotionEffects", ☃);
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("Potion", 8)) {
         this.potion = PotionUtils.getPotionTypeFromNBT(☃);
      }

      for (PotionEffect ☃ : PotionUtils.getFullEffectsFromTag(☃)) {
         this.addEffect(☃);
      }

      if (☃.hasKey("Color", 99)) {
         this.setFixedColor(☃.getInteger("Color"));
      } else {
         this.refreshColor();
      }
   }

   @Override
   protected void arrowHit(EntityLivingBase var1) {
      super.arrowHit(☃);

      for (PotionEffect ☃ : this.potion.getEffects()) {
         ☃.addPotionEffect(new PotionEffect(☃.getPotion(), Math.max(☃.getDuration() / 8, 1), ☃.getAmplifier(), ☃.getIsAmbient(), ☃.doesShowParticles()));
      }

      if (!this.customPotionEffects.isEmpty()) {
         for (PotionEffect ☃ : this.customPotionEffects) {
            ☃.addPotionEffect(☃);
         }
      }
   }

   @Override
   protected ItemStack getArrowStack() {
      if (this.customPotionEffects.isEmpty() && this.potion == PotionTypes.EMPTY) {
         return new ItemStack(Items.ARROW);
      } else {
         ItemStack ☃ = new ItemStack(Items.TIPPED_ARROW);
         PotionUtils.addPotionToItemStack(☃, this.potion);
         PotionUtils.appendEffects(☃, this.customPotionEffects);
         if (this.fixedColor) {
            NBTTagCompound ☃x = ☃.getTagCompound();
            if (☃x == null) {
               ☃x = new NBTTagCompound();
               ☃.setTagCompound(☃x);
            }

            ☃x.setInteger("CustomPotionColor", this.getColor());
         }

         return ☃;
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 0) {
         int ☃ = this.getColor();
         if (☃ != -1) {
            double ☃x = (☃ >> 16 & 0xFF) / 255.0;
            double ☃xx = (☃ >> 8 & 0xFF) / 255.0;
            double ☃xxx = (☃ >> 0 & 0xFF) / 255.0;

            for (int ☃xxxx = 0; ☃xxxx < 20; ☃xxxx++) {
               this.world
                  .spawnParticle(
                     EnumParticleTypes.SPELL_MOB,
                     this.posX + (this.rand.nextDouble() - 0.5) * this.width,
                     this.posY + this.rand.nextDouble() * this.height,
                     this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
                     ☃x,
                     ☃xx,
                     ☃xxx
                  );
            }
         }
      } else {
         super.handleStatusUpdate(☃);
      }
   }
}
