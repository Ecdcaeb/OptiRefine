package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class SoundEventAccessor implements ISoundEventAccessor<Sound> {
   private final List<ISoundEventAccessor<Sound>> accessorList = Lists.newArrayList();
   private final Random rnd = new Random();
   private final ResourceLocation location;
   private final ITextComponent subtitle;

   public SoundEventAccessor(ResourceLocation var1, @Nullable String var2) {
      this.location = ☃;
      this.subtitle = ☃ == null ? null : new TextComponentTranslation(☃);
   }

   @Override
   public int getWeight() {
      int ☃ = 0;

      for (ISoundEventAccessor<Sound> ☃x : this.accessorList) {
         ☃ += ☃x.getWeight();
      }

      return ☃;
   }

   public Sound cloneEntry() {
      int ☃ = this.getWeight();
      if (!this.accessorList.isEmpty() && ☃ != 0) {
         int ☃x = this.rnd.nextInt(☃);

         for (ISoundEventAccessor<Sound> ☃xx : this.accessorList) {
            ☃x -= ☃xx.getWeight();
            if (☃x < 0) {
               return ☃xx.cloneEntry();
            }
         }

         return SoundHandler.MISSING_SOUND;
      } else {
         return SoundHandler.MISSING_SOUND;
      }
   }

   public void addSound(ISoundEventAccessor<Sound> var1) {
      this.accessorList.add(☃);
   }

   public ResourceLocation getLocation() {
      return this.location;
   }

   @Nullable
   public ITextComponent getSubtitle() {
      return this.subtitle;
   }
}
