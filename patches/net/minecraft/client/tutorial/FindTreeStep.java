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
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class FindTreeStep implements ITutorialStep {
   private static final Set<Block> TREE_BLOCKS = Sets.newHashSet(new Block[]{Blocks.LOG, Blocks.LOG2, Blocks.LEAVES, Blocks.LEAVES2});
   private static final ITextComponent TITLE = new TextComponentTranslation("tutorial.find_tree.title");
   private static final ITextComponent DESCRIPTION = new TextComponentTranslation("tutorial.find_tree.description");
   private final Tutorial tutorial;
   private TutorialToast toast;
   private int timeWaiting;

   public FindTreeStep(Tutorial var1) {
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
               for (Block ☃x : TREE_BLOCKS) {
                  if (☃.inventory.hasItemStack(new ItemStack(☃x))) {
                     this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                     return;
                  }
               }

               if (hasPunchedTreesPreviously(☃)) {
                  this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                  return;
               }
            }
         }

         if (this.timeWaiting >= 6000 && this.toast == null) {
            this.toast = new TutorialToast(TutorialToast.Icons.TREE, TITLE, DESCRIPTION, false);
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
   public void onMouseHover(WorldClient var1, RayTraceResult var2) {
      if (☃.typeOfHit == RayTraceResult.Type.BLOCK && ☃.getBlockPos() != null) {
         IBlockState ☃ = ☃.getBlockState(☃.getBlockPos());
         if (TREE_BLOCKS.contains(☃.getBlock())) {
            this.tutorial.setStep(TutorialSteps.PUNCH_TREE);
         }
      }
   }

   @Override
   public void handleSetSlot(ItemStack var1) {
      for (Block ☃ : TREE_BLOCKS) {
         if (☃.getItem() == Item.getItemFromBlock(☃)) {
            this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
            return;
         }
      }
   }

   public static boolean hasPunchedTreesPreviously(EntityPlayerSP var0) {
      for (Block ☃ : TREE_BLOCKS) {
         StatBase ☃x = StatList.getBlockStats(☃);
         if (☃x != null && ☃.getStatFileWriter().readStat(☃x) > 0) {
            return true;
         }
      }

      return false;
   }
}
