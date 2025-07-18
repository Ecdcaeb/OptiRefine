package net.minecraft.util;

public class TupleIntJsonSerializable {
   private int integerValue;
   private IJsonSerializable jsonSerializableValue;

   public int getIntegerValue() {
      return this.integerValue;
   }

   public void setIntegerValue(int var1) {
      this.integerValue = ☃;
   }

   public <T extends IJsonSerializable> T getJsonSerializableValue() {
      return (T)this.jsonSerializableValue;
   }

   public void setJsonSerializableValue(IJsonSerializable var1) {
      this.jsonSerializableValue = ☃;
   }
}
