package net.minecraft.client.tutorial;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class PunchTreeStep implements ITutorialStep {
   private static final Set<Block> LOG_BLOCKS = Sets.newHashSet(new Block[]{Blocks.LOG, Blocks.LOG2});
   private static final ITextComponent TITLE = new TextComponentTranslation("tutorial.punch_tree.title");
   private static final ITextComponent DESCRIPTION = new TextComponentTranslation("tutorial.punch_tree.description", Tutorial.createKeybindComponent("attack"));
   private final Tutorial tutorial;
   private TutorialToast toast;
   private int timeWaiting;
   private int resetCount;

   public PunchTreeStep(Tutorial var1) {
      this.tutorial = ☃;
   }

   @Override
   public void update() {
      this.timeWaiting++;
      if (this.tutorial.getGameType() != GameType.SURVIVAL) {
         this.tutorial.setStep(TutorialSteps.NONE);
      } else {
         if (this.timeWaiting == 1) {
            EntityPlayerSP ☃ = this.tutorial.getMinecraft().player;
            if (☃ != null) {
               for (Block ☃x : LOG_BLOCKS) {
                  if (☃.inventory.hasItemStack(new ItemStack(☃x))) {
                     this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                     return;
                  }
               }

               if (FindTreeStep.hasPunchedTreesPreviously(☃)) {
                  this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                  return;
               }
            }
         }

         if ((this.timeWaiting >= 600 || this.resetCount > 3) && this.toast == null) {
            this.toast = new TutorialToast(TutorialToast.Icons.TREE, TITLE, DESCRIPTION, true);
            this.tutorial.getMinecraft().getToastGui().add(this.toast);
         }
      }
   }

   @Override
   public void onStop() {
      if (this.toast != null) {
         this.toast.hide();
         this.toast = null;
      }
   }

   @Override
   public void onHitBlock(WorldClient var1, BlockPos var2, IBlockState var3, float var4) {
      boolean ☃ = LOG_BLOCKS.contains(☃.getBlock());
      if (☃ && ☃ > 0.0F) {
         if (this.toast != null) {
            this.toast.setProgress(☃);
         }

         if (☃ >= 1.0F) {
            this.tutorial.setStep(TutorialSteps.OPEN_INVENTORY);
         }
      } else if (this.toast != null) {
         this.toast.setProgress(0.0F);
      } else if (☃) {
         this.resetCount++;
      }
   }

   @Override
   public void handleSetSlot(ItemStack var1) {
      for (Block ☃ : LOG_BLOCKS) {
         if (☃.getItem() == Item.getItemFromBlock(☃)) {
            this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
            return;
         }
      }
   }
}
