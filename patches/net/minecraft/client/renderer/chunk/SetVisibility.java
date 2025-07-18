package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Set;
import net.minecraft.util.EnumFacing;

public class SetVisibility {
   private static final int COUNT_FACES = EnumFacing.values().length;
   private final BitSet bitSet = new BitSet(COUNT_FACES * COUNT_FACES);

   public void setManyVisible(Set<EnumFacing> var1) {
      for (EnumFacing ☃ : ☃) {
         for (EnumFacing ☃x : ☃) {
            this.setVisible(☃, ☃x, true);
         }
      }
   }

   public void setVisible(EnumFacing var1, EnumFacing var2, boolean var3) {
      this.bitSet.set(☃.ordinal() + ☃.ordinal() * COUNT_FACES, ☃);
      this.bitSet.set(☃.ordinal() + ☃.ordinal() * COUNT_FACES, ☃);
   }

   public void setAllVisible(boolean var1) {
      this.bitSet.set(0, this.bitSet.size(), ☃);
   }

   public boolean isVisible(EnumFacing var1, EnumFacing var2) {
      return this.bitSet.get(☃.ordinal() + ☃.ordinal() * COUNT_FACES);
   }

   @Override
   public String toString() {
      StringBuilder ☃ = new StringBuilder();
      ☃.append(' ');

      for (EnumFacing ☃x : EnumFacing.values()) {
         ☃.append(' ').append(☃x.toString().toUpperCase().charAt(0));
      }

      ☃.append('\n');

      for (EnumFacing ☃x : EnumFacing.values()) {
         ☃.append(☃x.toString().toUpperCase().charAt(0));

         for (EnumFacing ☃xx : EnumFacing.values()) {
            if (☃x == ☃xx) {
               ☃.append("  ");
            } else {
               boolean ☃xxx = this.isVisible(☃x, ☃xx);
               ☃.append(' ').append((char)(☃xxx ? 'Y' : 'n'));
            }
         }

         ☃.append('\n');
      }

      return ☃.toString();
   }
}
