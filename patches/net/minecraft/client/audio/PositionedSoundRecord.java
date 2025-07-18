package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class PositionedSoundRecord extends PositionedSound {
   public PositionedSoundRecord(SoundEvent var1, SoundCategory var2, float var3, float var4, BlockPos var5) {
      this(☃, ☃, ☃, ☃, ☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F);
   }

   public static PositionedSoundRecord getMasterRecord(SoundEvent var0, float var1) {
      return getRecord(☃, ☃, 0.25F);
   }

   public static PositionedSoundRecord getRecord(SoundEvent var0, float var1, float var2) {
      return new PositionedSoundRecord(☃, SoundCategory.MASTER, ☃, ☃, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
   }

   public static PositionedSoundRecord getMusicRecord(SoundEvent var0) {
      return new PositionedSoundRecord(☃, SoundCategory.MUSIC, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
   }

   public static PositionedSoundRecord getRecordSoundRecord(SoundEvent var0, float var1, float var2, float var3) {
      return new PositionedSoundRecord(☃, SoundCategory.RECORDS, 4.0F, 1.0F, false, 0, ISound.AttenuationType.LINEAR, ☃, ☃, ☃);
   }

   public PositionedSoundRecord(SoundEvent var1, SoundCategory var2, float var3, float var4, float var5, float var6, float var7) {
      this(☃, ☃, ☃, ☃, false, 0, ISound.AttenuationType.LINEAR, ☃, ☃, ☃);
   }

   private PositionedSoundRecord(
      SoundEvent var1, SoundCategory var2, float var3, float var4, boolean var5, int var6, ISound.AttenuationType var7, float var8, float var9, float var10
   ) {
      this(☃.getSoundName(), ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public PositionedSoundRecord(
      ResourceLocation var1,
      SoundCategory var2,
      float var3,
      float var4,
      boolean var5,
      int var6,
      ISound.AttenuationType var7,
      float var8,
      float var9,
      float var10
   ) {
      super(☃, ☃);
      this.volume = ☃;
      this.pitch = ☃;
      this.xPosF = ☃;
      this.yPosF = ☃;
      this.zPosF = ☃;
      this.repeat = ☃;
      this.repeatDelay = ☃;
      this.attenuationType = ☃;
   }
}
