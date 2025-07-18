package net.minecraft.server.management;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class DemoPlayerInteractionManager extends PlayerInteractionManager {
   private boolean displayedIntro;
   private boolean demoTimeExpired;
   private int demoEndedReminder;
   private int gameModeTicks;

   public DemoPlayerInteractionManager(World var1) {
      super(☃);
   }

   @Override
   public void updateBlockRemoving() {
      super.updateBlockRemoving();
      this.gameModeTicks++;
      long ☃ = this.world.getTotalWorldTime();
      long ☃x = ☃ / 24000L + 1L;
      if (!this.displayedIntro && this.gameModeTicks > 20) {
         this.displayedIntro = true;
         this.player.connection.sendPacket(new SPacketChangeGameState(5, 0.0F));
      }

      this.demoTimeExpired = ☃ > 120500L;
      if (this.demoTimeExpired) {
         this.demoEndedReminder++;
      }

      if (☃ % 24000L == 500L) {
         if (☃x <= 6L) {
            this.player.sendMessage(new TextComponentTranslation("demo.day." + ☃x));
         }
      } else if (☃x == 1L) {
         if (☃ == 100L) {
            this.player.connection.sendPacket(new SPacketChangeGameState(5, 101.0F));
         } else if (☃ == 175L) {
            this.player.connection.sendPacket(new SPacketChangeGameState(5, 102.0F));
         } else if (☃ == 250L) {
            this.player.connection.sendPacket(new SPacketChangeGameState(5, 103.0F));
         }
      } else if (☃x == 5L && ☃ % 24000L == 22000L) {
         this.player.sendMessage(new TextComponentTranslation("demo.day.warning"));
      }
   }

   private void sendDemoReminder() {
      if (this.demoEndedReminder > 100) {
         this.player.sendMessage(new TextComponentTranslation("demo.reminder"));
         this.demoEndedReminder = 0;
      }
   }

   @Override
   public void onBlockClicked(BlockPos var1, EnumFacing var2) {
      if (this.demoTimeExpired) {
         this.sendDemoReminder();
      } else {
         super.onBlockClicked(☃, ☃);
      }
   }

   @Override
   public void blockRemoving(BlockPos var1) {
      if (!this.demoTimeExpired) {
         super.blockRemoving(☃);
      }
   }

   @Override
   public boolean tryHarvestBlock(BlockPos var1) {
      return this.demoTimeExpired ? false : super.tryHarvestBlock(☃);
   }

   @Override
   public EnumActionResult processRightClick(EntityPlayer var1, World var2, ItemStack var3, EnumHand var4) {
      if (this.demoTimeExpired) {
         this.sendDemoReminder();
         return EnumActionResult.PASS;
      } else {
         return super.processRightClick(☃, ☃, ☃, ☃);
      }
   }

   @Override
   public EnumActionResult processRightClickBlock(
      EntityPlayer var1, World var2, ItemStack var3, EnumHand var4, BlockPos var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (this.demoTimeExpired) {
         this.sendDemoReminder();
         return EnumActionResult.PASS;
      } else {
         return super.processRightClickBlock(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
