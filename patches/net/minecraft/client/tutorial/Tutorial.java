package net.minecraft.client.tutorial;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.world.GameType;

public class Tutorial {
   private final Minecraft minecraft;
   @Nullable
   private ITutorialStep tutorialStep;

   public Tutorial(Minecraft var1) {
      this.minecraft = ☃;
   }

   public void handleMovement(MovementInput var1) {
      if (this.tutorialStep != null) {
         this.tutorialStep.handleMovement(☃);
      }
   }

   public void handleMouse(MouseHelper var1) {
      if (this.tutorialStep != null) {
         this.tutorialStep.handleMouse(☃);
      }
   }

   public void onMouseHover(@Nullable WorldClient var1, @Nullable RayTraceResult var2) {
      if (this.tutorialStep != null && ☃ != null && ☃ != null) {
         this.tutorialStep.onMouseHover(☃, ☃);
      }
   }

   public void onHitBlock(WorldClient var1, BlockPos var2, IBlockState var3, float var4) {
      if (this.tutorialStep != null) {
         this.tutorialStep.onHitBlock(☃, ☃, ☃, ☃);
      }
   }

   public void openInventory() {
      if (this.tutorialStep != null) {
         this.tutorialStep.openInventory();
      }
   }

   public void handleSetSlot(ItemStack var1) {
      if (this.tutorialStep != null) {
         this.tutorialStep.handleSetSlot(☃);
      }
   }

   public void stop() {
      if (this.tutorialStep != null) {
         this.tutorialStep.onStop();
         this.tutorialStep = null;
      }
   }

   public void reload() {
      if (this.tutorialStep != null) {
         this.stop();
      }

      this.tutorialStep = this.minecraft.gameSettings.tutorialStep.create(this);
   }

   public void update() {
      if (this.tutorialStep != null) {
         if (this.minecraft.world != null) {
            this.tutorialStep.update();
         } else {
            this.stop();
         }
      } else if (this.minecraft.world != null) {
         this.reload();
      }
   }

   public void setStep(TutorialSteps var1) {
      this.minecraft.gameSettings.tutorialStep = ☃;
      this.minecraft.gameSettings.saveOptions();
      if (this.tutorialStep != null) {
         this.tutorialStep.onStop();
         this.tutorialStep = ☃.create(this);
      }
   }

   public Minecraft getMinecraft() {
      return this.minecraft;
   }

   public GameType getGameType() {
      return this.minecraft.playerController == null ? GameType.NOT_SET : this.minecraft.playerController.getCurrentGameType();
   }

   public static ITextComponent createKeybindComponent(String var0) {
      TextComponentKeybind ☃ = new TextComponentKeybind("key." + ☃);
      ☃.getStyle().setBold(true);
      return ☃;
   }
}
