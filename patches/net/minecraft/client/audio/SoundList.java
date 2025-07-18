package net.minecraft.client.audio;

import java.util.List;
import javax.annotation.Nullable;

public class SoundList {
   private final List<Sound> sounds;
   private final boolean replaceExisting;
   private final String subtitle;

   public SoundList(List<Sound> var1, boolean var2, String var3) {
      this.sounds = ☃;
      this.replaceExisting = ☃;
      this.subtitle = ☃;
   }

   public List<Sound> getSounds() {
      return this.sounds;
   }

   public boolean canReplaceExisting() {
      return this.replaceExisting;
   }

   @Nullable
   public String getSubtitle() {
      return this.subtitle;
   }
}
