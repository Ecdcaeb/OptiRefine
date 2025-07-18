package net.minecraft.entity;

public interface IJumpingMount {
   void setJumpPower(int var1);

   boolean canJump();

   void handleStartJump(int var1);

   void handleStopJump();
}
