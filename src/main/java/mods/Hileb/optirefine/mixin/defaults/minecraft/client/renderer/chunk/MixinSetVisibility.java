package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.*;

@Mixin(SetVisibility.class)
public abstract class MixinSetVisibility {

    @Shadow @Final
    private static int COUNT_FACES;

    @Unique
    private long optiRefine$bits;

    @Unique
    public void setVisible(EnumFacing facing, EnumFacing facing2, boolean p_178619_3_) {
        this.optiRefine$setBit(facing.ordinal() + facing2.ordinal() * COUNT_FACES, p_178619_3_);
        this.optiRefine$setBit(facing2.ordinal() + facing.ordinal() * COUNT_FACES, p_178619_3_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setAllVisible(boolean visible) {
        if (visible) {
            this.optiRefine$bits = -1L;
        } else {
            this.optiRefine$bits = 0L;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
        return this.optiRefine$getBit(facing.ordinal() + facing2.ordinal() * COUNT_FACES);
    }

    @Unique
    private boolean optiRefine$getBit(int i) {
        return (this.optiRefine$bits & 1L << i) != 0L;
    }

    @Unique
    private void optiRefine$setBit(int i, boolean on) {
        if (on) {
            this.optiRefine$setBit(i);
        } else {
            this.optiRefine$clearBit(i);
        }
    }

    @Unique
    private void optiRefine$setBit(int i) {
        this.optiRefine$bits |= 1L << i;
    }

    @Unique
    private void optiRefine$clearBit(int i) {
        this.optiRefine$bits &= ~(1L << i);
    }
}

/*
--- net/minecraft/client/renderer/chunk/SetVisibility.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/chunk/SetVisibility.java	Tue Aug 19 14:59:58 2025
@@ -1,35 +1,38 @@
 package net.minecraft.client.renderer.chunk;

-import java.util.BitSet;
 import java.util.Set;
 import net.minecraft.util.EnumFacing;

 public class SetVisibility {
    private static final int COUNT_FACES = EnumFacing.values().length;
-   private final BitSet bitSet = new BitSet(COUNT_FACES * COUNT_FACES);
+   private long bits;

    public void setManyVisible(Set<EnumFacing> var1) {
       for (EnumFacing var3 : var1) {
          for (EnumFacing var5 : var1) {
             this.setVisible(var3, var5, true);
          }
       }
    }

    public void setVisible(EnumFacing var1, EnumFacing var2, boolean var3) {
-      this.bitSet.set(var1.ordinal() + var2.ordinal() * COUNT_FACES, var3);
-      this.bitSet.set(var2.ordinal() + var1.ordinal() * COUNT_FACES, var3);
+      this.setBit(var1.ordinal() + var2.ordinal() * COUNT_FACES, var3);
+      this.setBit(var2.ordinal() + var1.ordinal() * COUNT_FACES, var3);
    }

    public void setAllVisible(boolean var1) {
-      this.bitSet.set(0, this.bitSet.size(), var1);
+      if (var1) {
+         this.bits = -1L;
+      } else {
+         this.bits = 0L;
+      }
    }

    public boolean isVisible(EnumFacing var1, EnumFacing var2) {
-      return this.bitSet.get(var1.ordinal() + var2.ordinal() * COUNT_FACES);
+      return this.getBit(var1.ordinal() + var2.ordinal() * COUNT_FACES);
    }

    public String toString() {
       StringBuilder var1 = new StringBuilder();
       var1.append(' ');

@@ -52,8 +55,28 @@
          }

          var1.append('\n');
       }

       return var1.toString();
+   }
+
+   private boolean getBit(int var1) {
+      return (this.bits & 1 << var1) != 0L;
+   }
+
+   private void setBit(int var1, boolean var2) {
+      if (var2) {
+         this.setBit(var1);
+      } else {
+         this.clearBit(var1);
+      }
+   }
+
+   private void setBit(int var1) {
+      this.bits |= 1 << var1;
+   }
+
+   private void clearBit(int var1) {
+      this.bits &= ~(1 << var1);
    }
 }
 */