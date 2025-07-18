package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

public class AnimationMetadataSection implements IMetadataSection {
   private final List<AnimationFrame> animationFrames;
   private final int frameWidth;
   private final int frameHeight;
   private final int frameTime;
   private final boolean interpolate;

   public AnimationMetadataSection(List<AnimationFrame> var1, int var2, int var3, int var4, boolean var5) {
      this.animationFrames = ☃;
      this.frameWidth = ☃;
      this.frameHeight = ☃;
      this.frameTime = ☃;
      this.interpolate = ☃;
   }

   public int getFrameHeight() {
      return this.frameHeight;
   }

   public int getFrameWidth() {
      return this.frameWidth;
   }

   public int getFrameCount() {
      return this.animationFrames.size();
   }

   public int getFrameTime() {
      return this.frameTime;
   }

   public boolean isInterpolate() {
      return this.interpolate;
   }

   private AnimationFrame getAnimationFrame(int var1) {
      return this.animationFrames.get(☃);
   }

   public int getFrameTimeSingle(int var1) {
      AnimationFrame ☃ = this.getAnimationFrame(☃);
      return ☃.hasNoTime() ? this.frameTime : ☃.getFrameTime();
   }

   public boolean frameHasTime(int var1) {
      return !this.animationFrames.get(☃).hasNoTime();
   }

   public int getFrameIndex(int var1) {
      return this.animationFrames.get(☃).getFrameIndex();
   }

   public Set<Integer> getFrameIndexSet() {
      Set<Integer> ☃ = Sets.newHashSet();

      for (AnimationFrame ☃x : this.animationFrames) {
         ☃.add(☃x.getFrameIndex());
      }

      return ☃;
   }
}
