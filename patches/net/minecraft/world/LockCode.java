package net.minecraft.world;

import javax.annotation.concurrent.Immutable;
import net.minecraft.nbt.NBTTagCompound;

@Immutable
public class LockCode {
   public static final LockCode EMPTY_CODE = new LockCode("");
   private final String lock;

   public LockCode(String var1) {
      this.lock = ☃;
   }

   public boolean isEmpty() {
      return this.lock == null || this.lock.isEmpty();
   }

   public String getLock() {
      return this.lock;
   }

   public void toNBT(NBTTagCompound var1) {
      ☃.setString("Lock", this.lock);
   }

   public static LockCode fromNBT(NBTTagCompound var0) {
      if (☃.hasKey("Lock", 8)) {
         String ☃ = ☃.getString("Lock");
         return new LockCode(☃);
      } else {
         return EMPTY_CODE;
      }
   }
}
