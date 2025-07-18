package net.minecraft.entity.ai.attributes;

import io.netty.util.internal.ThreadLocalRandom;
import java.util.UUID;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.Validate;

public class AttributeModifier {
   private final double amount;
   private final int operation;
   private final String name;
   private final UUID id;
   private boolean isSaved = true;

   public AttributeModifier(String var1, double var2, int var4) {
      this(MathHelper.getRandomUUID(ThreadLocalRandom.current()), ☃, ☃, ☃);
   }

   public AttributeModifier(UUID var1, String var2, double var3, int var5) {
      this.id = ☃;
      this.name = ☃;
      this.amount = ☃;
      this.operation = ☃;
      Validate.notEmpty(☃, "Modifier name cannot be empty", new Object[0]);
      Validate.inclusiveBetween(0L, 2L, ☃, "Invalid operation");
   }

   public UUID getID() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public int getOperation() {
      return this.operation;
   }

   public double getAmount() {
      return this.amount;
   }

   public boolean isSaved() {
      return this.isSaved;
   }

   public AttributeModifier setSaved(boolean var1) {
      this.isSaved = ☃;
      return this;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ != null && this.getClass() == ☃.getClass()) {
         AttributeModifier ☃ = (AttributeModifier)☃;
         return this.id != null ? this.id.equals(☃.id) : ☃.id == null;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.id != null ? this.id.hashCode() : 0;
   }

   @Override
   public String toString() {
      return "AttributeModifier{amount="
         + this.amount
         + ", operation="
         + this.operation
         + ", name='"
         + this.name
         + '\''
         + ", id="
         + this.id
         + ", serialize="
         + this.isSaved
         + '}';
   }
}
