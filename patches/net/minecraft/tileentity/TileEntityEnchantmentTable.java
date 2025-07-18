package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject {
   public int tickCount;
   public float pageFlip;
   public float pageFlipPrev;
   public float flipT;
   public float flipA;
   public float bookSpread;
   public float bookSpreadPrev;
   public float bookRotation;
   public float bookRotationPrev;
   public float tRot;
   private static final Random rand = new Random();
   private String customName;

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      if (this.hasCustomName()) {
         ☃.setString("CustomName", this.customName);
      }

      return ☃;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      if (☃.hasKey("CustomName", 8)) {
         this.customName = ☃.getString("CustomName");
      }
   }

   @Override
   public void update() {
      this.bookSpreadPrev = this.bookSpread;
      this.bookRotationPrev = this.bookRotation;
      EntityPlayer ☃ = this.world.getClosestPlayer(this.pos.getX() + 0.5F, this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F, 3.0, false);
      if (☃ != null) {
         double ☃x = ☃.posX - (this.pos.getX() + 0.5F);
         double ☃xx = ☃.posZ - (this.pos.getZ() + 0.5F);
         this.tRot = (float)MathHelper.atan2(☃xx, ☃x);
         this.bookSpread += 0.1F;
         if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
            float ☃xxx = this.flipT;

            do {
               this.flipT = this.flipT + (rand.nextInt(4) - rand.nextInt(4));
            } while (☃xxx == this.flipT);
         }
      } else {
         this.tRot += 0.02F;
         this.bookSpread -= 0.1F;
      }

      while (this.bookRotation >= (float) Math.PI) {
         this.bookRotation -= (float) (Math.PI * 2);
      }

      while (this.bookRotation < (float) -Math.PI) {
         this.bookRotation += (float) (Math.PI * 2);
      }

      while (this.tRot >= (float) Math.PI) {
         this.tRot -= (float) (Math.PI * 2);
      }

      while (this.tRot < (float) -Math.PI) {
         this.tRot += (float) (Math.PI * 2);
      }

      float ☃x = this.tRot - this.bookRotation;

      while (☃x >= (float) Math.PI) {
         ☃x -= (float) (Math.PI * 2);
      }

      while (☃x < (float) -Math.PI) {
         ☃x += (float) (Math.PI * 2);
      }

      this.bookRotation += ☃x * 0.4F;
      this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
      this.tickCount++;
      this.pageFlipPrev = this.pageFlip;
      float ☃xx = (this.flipT - this.pageFlip) * 0.4F;
      float ☃xxx = 0.2F;
      ☃xx = MathHelper.clamp(☃xx, -0.2F, 0.2F);
      this.flipA = this.flipA + (☃xx - this.flipA) * 0.9F;
      this.pageFlip = this.pageFlip + this.flipA;
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.customName : "container.enchant";
   }

   @Override
   public boolean hasCustomName() {
      return this.customName != null && !this.customName.isEmpty();
   }

   public void setCustomName(String var1) {
      this.customName = ☃;
   }

   @Override
   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName()));
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerEnchantment(☃, this.world, this.pos);
   }

   @Override
   public String getGuiID() {
      return "minecraft:enchanting_table";
   }
}
