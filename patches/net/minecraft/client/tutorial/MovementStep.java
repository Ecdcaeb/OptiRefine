package net.minecraft.client.tutorial;

import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class MovementStep implements ITutorialStep {
   private static final ITextComponent MOVE_TITLE = new TextComponentTranslation(
      "tutorial.move.title",
      Tutorial.createKeybindComponent("forward"),
      Tutorial.createKeybindComponent("left"),
      Tutorial.createKeybindComponent("back"),
      Tutorial.createKeybindComponent("right")
   );
   private static final ITextComponent MOVE_DESCRIPTION = new TextComponentTranslation("tutorial.move.description", Tutorial.createKeybindComponent("jump"));
   private static final ITextComponent LOOK_TITLE = new TextComponentTranslation("tutorial.look.title");
   private static final ITextComponent LOOK_DESCRIPTION = new TextComponentTranslation("tutorial.look.description");
   private final Tutorial tutorial;
   private TutorialToast moveToast;
   private TutorialToast lookToast;
   private int timeWaiting;
   private int timeMoved;
   private int timeLooked;
   private boolean moved;
   private boolean turned;
   private int moveCompleted = -1;
   private int lookCompleted = -1;

   public MovementStep(Tutorial var1) {
      this.tutorial = ☃;
   }

   @Override
   public void update() {
      this.timeWaiting++;
      if (this.moved) {
         this.timeMoved++;
         this.moved = false;
      }

      if (this.turned) {
         this.timeLooked++;
         this.turned = false;
      }

      if (this.moveCompleted == -1 && this.timeMoved > 40) {
         if (this.moveToast != null) {
            this.moveToast.hide();
            this.moveToast = null;
         }

         this.moveCompleted = this.timeWaiting;
      }

      if (this.lookCompleted == -1 && this.timeLooked > 40) {
         if (this.lookToast != null) {
            this.lookToast.hide();
            this.lookToast = null;
         }

         this.lookCompleted = this.timeWaiting;
      }

      if (this.moveCompleted != -1 && this.lookCompleted != -1) {
         if (this.tutorial.getGameType() == GameType.SURVIVAL) {
            this.tutorial.setStep(TutorialSteps.FIND_TREE);
         } else {
            this.tutorial.setStep(TutorialSteps.NONE);
         }
      }

      if (this.moveToast != null) {
         this.moveToast.setProgress(this.timeMoved / 40.0F);
      }

      if (this.lookToast != null) {
         this.lookToast.setProgress(this.timeLooked / 40.0F);
      }

      if (this.timeWaiting >= 100) {
         if (this.moveCompleted == -1 && this.moveToast == null) {
            this.moveToast = new TutorialToast(TutorialToast.Icons.MOVEMENT_KEYS, MOVE_TITLE, MOVE_DESCRIPTION, true);
            this.tutorial.getMinecraft().getToastGui().add(this.moveToast);
         } else if (this.moveCompleted != -1 && this.timeWaiting - this.moveCompleted >= 20 && this.lookCompleted == -1 && this.lookToast == null) {
            this.lookToast = new TutorialToast(TutorialToast.Icons.MOUSE, LOOK_TITLE, LOOK_DESCRIPTION, true);
            this.tutorial.getMinecraft().getToastGui().add(this.lookToast);
         }
      }
   }

   @Override
   public void onStop() {
      if (this.moveToast != null) {
         this.moveToast.hide();
         this.moveToast = null;
      }

      if (this.lookToast != null) {
         this.lookToast.hide();
         this.lookToast = null;
      }
   }

   @Override
   public void handleMovement(MovementInput var1) {
      if (☃.forwardKeyDown || ☃.backKeyDown || ☃.leftKeyDown || ☃.rightKeyDown || ☃.jump) {
         this.moved = true;
      }
   }

   @Override
   public void handleMouse(MouseHelper var1) {
      if (MathHelper.abs(☃.deltaX) > 0.01 || MathHelper.abs(☃.deltaY) > 0.01) {
         this.turned = true;
      }
   }
}
