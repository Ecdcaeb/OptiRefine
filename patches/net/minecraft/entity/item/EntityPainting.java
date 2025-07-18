package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityPainting extends EntityHanging {
   public EntityPainting.EnumArt art;

   public EntityPainting(World var1) {
      super(☃);
   }

   public EntityPainting(World var1, BlockPos var2, EnumFacing var3) {
      super(☃, ☃);
      List<EntityPainting.EnumArt> ☃ = Lists.newArrayList();
      int ☃x = 0;

      for (EntityPainting.EnumArt ☃xx : EntityPainting.EnumArt.values()) {
         this.art = ☃xx;
         this.updateFacingWithBoundingBox(☃);
         if (this.onValidSurface()) {
            ☃.add(☃xx);
            int ☃xxx = ☃xx.sizeX * ☃xx.sizeY;
            if (☃xxx > ☃x) {
               ☃x = ☃xxx;
            }
         }
      }

      if (!☃.isEmpty()) {
         Iterator<EntityPainting.EnumArt> ☃xxx = ☃.iterator();

         while (☃xxx.hasNext()) {
            EntityPainting.EnumArt ☃xxxx = ☃xxx.next();
            if (☃xxxx.sizeX * ☃xxxx.sizeY < ☃x) {
               ☃xxx.remove();
            }
         }

         this.art = ☃.get(this.rand.nextInt(☃.size()));
      }

      this.updateFacingWithBoundingBox(☃);
   }

   public EntityPainting(World var1, BlockPos var2, EnumFacing var3, String var4) {
      this(☃, ☃, ☃);

      for (EntityPainting.EnumArt ☃ : EntityPainting.EnumArt.values()) {
         if (☃.title.equals(☃)) {
            this.art = ☃;
            break;
         }
      }

      this.updateFacingWithBoundingBox(☃);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setString("Motive", this.art.title);
      super.writeEntityToNBT(☃);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      String ☃ = ☃.getString("Motive");

      for (EntityPainting.EnumArt ☃x : EntityPainting.EnumArt.values()) {
         if (☃x.title.equals(☃)) {
            this.art = ☃x;
         }
      }

      if (this.art == null) {
         this.art = EntityPainting.EnumArt.KEBAB;
      }

      super.readEntityFromNBT(☃);
   }

   @Override
   public int getWidthPixels() {
      return this.art.sizeX;
   }

   @Override
   public int getHeightPixels() {
      return this.art.sizeY;
   }

   @Override
   public void onBroken(@Nullable Entity var1) {
      if (this.world.getGameRules().getBoolean("doEntityDrops")) {
         this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);
         if (☃ instanceof EntityPlayer) {
            EntityPlayer ☃ = (EntityPlayer)☃;
            if (☃.capabilities.isCreativeMode) {
               return;
            }
         }

         this.entityDropItem(new ItemStack(Items.PAINTING), 0.0F);
      }
   }

   @Override
   public void playPlaceSound() {
      this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
   }

   @Override
   public void setLocationAndAngles(double var1, double var3, double var5, float var7, float var8) {
      this.setPosition(☃, ☃, ☃);
   }

   @Override
   public void setPositionAndRotationDirect(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      BlockPos ☃ = this.hangingPosition.add(☃ - this.posX, ☃ - this.posY, ☃ - this.posZ);
      this.setPosition(☃.getX(), ☃.getY(), ☃.getZ());
   }

   public static enum EnumArt {
      KEBAB("Kebab", 16, 16, 0, 0),
      AZTEC("Aztec", 16, 16, 16, 0),
      ALBAN("Alban", 16, 16, 32, 0),
      AZTEC_2("Aztec2", 16, 16, 48, 0),
      BOMB("Bomb", 16, 16, 64, 0),
      PLANT("Plant", 16, 16, 80, 0),
      WASTELAND("Wasteland", 16, 16, 96, 0),
      POOL("Pool", 32, 16, 0, 32),
      COURBET("Courbet", 32, 16, 32, 32),
      SEA("Sea", 32, 16, 64, 32),
      SUNSET("Sunset", 32, 16, 96, 32),
      CREEBET("Creebet", 32, 16, 128, 32),
      WANDERER("Wanderer", 16, 32, 0, 64),
      GRAHAM("Graham", 16, 32, 16, 64),
      MATCH("Match", 32, 32, 0, 128),
      BUST("Bust", 32, 32, 32, 128),
      STAGE("Stage", 32, 32, 64, 128),
      VOID("Void", 32, 32, 96, 128),
      SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128),
      WITHER("Wither", 32, 32, 160, 128),
      FIGHTERS("Fighters", 64, 32, 0, 96),
      POINTER("Pointer", 64, 64, 0, 192),
      PIGSCENE("Pigscene", 64, 64, 64, 192),
      BURNING_SKULL("BurningSkull", 64, 64, 128, 192),
      SKELETON("Skeleton", 64, 48, 192, 64),
      DONKEY_KONG("DonkeyKong", 64, 48, 192, 112);

      public static final int MAX_NAME_LENGTH = "SkullAndRoses".length();
      public final String title;
      public final int sizeX;
      public final int sizeY;
      public final int offsetX;
      public final int offsetY;

      private EnumArt(String var3, int var4, int var5, int var6, int var7) {
         this.title = ☃;
         this.sizeX = ☃;
         this.sizeY = ☃;
         this.offsetX = ☃;
         this.offsetY = ☃;
      }
   }
}
