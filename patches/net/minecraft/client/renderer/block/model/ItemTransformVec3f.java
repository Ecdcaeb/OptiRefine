package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class ItemTransformVec3f {
   public static final ItemTransformVec3f DEFAULT = new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0F, 1.0F, 1.0F));
   public final Vector3f rotation;
   public final Vector3f translation;
   public final Vector3f scale;

   public ItemTransformVec3f(Vector3f var1, Vector3f var2, Vector3f var3) {
      this.rotation = new Vector3f(☃);
      this.translation = new Vector3f(☃);
      this.scale = new Vector3f(☃);
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (this.getClass() != ☃.getClass()) {
         return false;
      } else {
         ItemTransformVec3f ☃ = (ItemTransformVec3f)☃;
         return this.rotation.equals(☃.rotation) && this.scale.equals(☃.scale) && this.translation.equals(☃.translation);
      }
   }

   @Override
   public int hashCode() {
      int ☃ = this.rotation.hashCode();
      ☃ = 31 * ☃ + this.translation.hashCode();
      return 31 * ☃ + this.scale.hashCode();
   }

   static class Deserializer implements JsonDeserializer<ItemTransformVec3f> {
      private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
      private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
      private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

      public ItemTransformVec3f deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         Vector3f ☃x = this.parseVector3f(☃, "rotation", ROTATION_DEFAULT);
         Vector3f ☃xx = this.parseVector3f(☃, "translation", TRANSLATION_DEFAULT);
         ☃xx.scale(0.0625F);
         ☃xx.x = MathHelper.clamp(☃xx.x, -5.0F, 5.0F);
         ☃xx.y = MathHelper.clamp(☃xx.y, -5.0F, 5.0F);
         ☃xx.z = MathHelper.clamp(☃xx.z, -5.0F, 5.0F);
         Vector3f ☃xxx = this.parseVector3f(☃, "scale", SCALE_DEFAULT);
         ☃xxx.x = MathHelper.clamp(☃xxx.x, -4.0F, 4.0F);
         ☃xxx.y = MathHelper.clamp(☃xxx.y, -4.0F, 4.0F);
         ☃xxx.z = MathHelper.clamp(☃xxx.z, -4.0F, 4.0F);
         return new ItemTransformVec3f(☃x, ☃xx, ☃xxx);
      }

      private Vector3f parseVector3f(JsonObject var1, String var2, Vector3f var3) {
         if (!☃.has(☃)) {
            return ☃;
         } else {
            JsonArray ☃ = JsonUtils.getJsonArray(☃, ☃);
            if (☃.size() != 3) {
               throw new JsonParseException("Expected 3 " + ☃ + " values, found: " + ☃.size());
            } else {
               float[] ☃x = new float[3];

               for (int ☃xx = 0; ☃xx < ☃x.length; ☃xx++) {
                  ☃x[☃xx] = JsonUtils.getFloat(☃.get(☃xx), ☃ + "[" + ☃xx + "]");
               }

               return new Vector3f(☃x[0], ☃x[1], ☃x[2]);
            }
         }
      }
   }
}
