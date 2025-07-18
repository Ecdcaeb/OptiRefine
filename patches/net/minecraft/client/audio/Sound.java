package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class Sound implements ISoundEventAccessor<Sound> {
   private final ResourceLocation name;
   private final float volume;
   private final float pitch;
   private final int weight;
   private final Sound.Type type;
   private final boolean streaming;

   public Sound(String var1, float var2, float var3, int var4, Sound.Type var5, boolean var6) {
      this.name = new ResourceLocation(☃);
      this.volume = ☃;
      this.pitch = ☃;
      this.weight = ☃;
      this.type = ☃;
      this.streaming = ☃;
   }

   public ResourceLocation getSoundLocation() {
      return this.name;
   }

   public ResourceLocation getSoundAsOggLocation() {
      return new ResourceLocation(this.name.getNamespace(), "sounds/" + this.name.getPath() + ".ogg");
   }

   public float getVolume() {
      return this.volume;
   }

   public float getPitch() {
      return this.pitch;
   }

   @Override
   public int getWeight() {
      return this.weight;
   }

   public Sound cloneEntry() {
      return this;
   }

   public Sound.Type getType() {
      return this.type;
   }

   public boolean isStreaming() {
      return this.streaming;
   }

   public static enum Type {
      FILE("file"),
      SOUND_EVENT("event");

      private final String name;

      private Type(String var3) {
         this.name = ☃;
      }

      public static Sound.Type getByName(String var0) {
         for (Sound.Type ☃ : values()) {
            if (☃.name.equals(☃)) {
               return ☃;
            }
         }

         return null;
      }
   }
}
