package net.minecraft.client.audio;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

public class MusicTicker implements ITickable {
   private final Random rand = new Random();
   private final Minecraft mc;
   private ISound currentMusic;
   private int timeUntilNextMusic = 100;

   public MusicTicker(Minecraft var1) {
      this.mc = ☃;
   }

   @Override
   public void update() {
      MusicTicker.MusicType ☃ = this.mc.getAmbientMusicType();
      if (this.currentMusic != null) {
         if (!☃.getMusicLocation().getSoundName().equals(this.currentMusic.getSoundLocation())) {
            this.mc.getSoundHandler().stopSound(this.currentMusic);
            this.timeUntilNextMusic = MathHelper.getInt(this.rand, 0, ☃.getMinDelay() / 2);
         }

         if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
            this.currentMusic = null;
            this.timeUntilNextMusic = Math.min(MathHelper.getInt(this.rand, ☃.getMinDelay(), ☃.getMaxDelay()), this.timeUntilNextMusic);
         }
      }

      this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, ☃.getMaxDelay());
      if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0) {
         this.playMusic(☃);
      }
   }

   public void playMusic(MusicTicker.MusicType var1) {
      this.currentMusic = PositionedSoundRecord.getMusicRecord(☃.getMusicLocation());
      this.mc.getSoundHandler().playSound(this.currentMusic);
      this.timeUntilNextMusic = Integer.MAX_VALUE;
   }

   public static enum MusicType {
      MENU(SoundEvents.MUSIC_MENU, 20, 600),
      GAME(SoundEvents.MUSIC_GAME, 12000, 24000),
      CREATIVE(SoundEvents.MUSIC_CREATIVE, 1200, 3600),
      CREDITS(SoundEvents.MUSIC_CREDITS, 0, 0),
      NETHER(SoundEvents.MUSIC_NETHER, 1200, 3600),
      END_BOSS(SoundEvents.MUSIC_DRAGON, 0, 0),
      END(SoundEvents.MUSIC_END, 6000, 24000);

      private final SoundEvent musicLocation;
      private final int minDelay;
      private final int maxDelay;

      private MusicType(SoundEvent var3, int var4, int var5) {
         this.musicLocation = ☃;
         this.minDelay = ☃;
         this.maxDelay = ☃;
      }

      public SoundEvent getMusicLocation() {
         return this.musicLocation;
      }

      public int getMinDelay() {
         return this.minDelay;
      }

      public int getMaxDelay() {
         return this.maxDelay;
      }
   }
}
