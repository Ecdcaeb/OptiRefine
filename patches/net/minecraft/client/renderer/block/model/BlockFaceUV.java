package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;

public class BlockFaceUV {
   public float[] uvs;
   public final int rotation;

   public BlockFaceUV(@Nullable float[] var1, int var2) {
      this.uvs = ☃;
      this.rotation = ☃;
   }

   public float getVertexU(int var1) {
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int ☃ = this.getVertexRotated(☃);
         return ☃ != 0 && ☃ != 1 ? this.uvs[2] : this.uvs[0];
      }
   }

   public float getVertexV(int var1) {
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int ☃ = this.getVertexRotated(☃);
         return ☃ != 0 && ☃ != 3 ? this.uvs[3] : this.uvs[1];
      }
   }

   private int getVertexRotated(int var1) {
      return (☃ + this.rotation / 90) % 4;
   }

   public int getVertexRotatedRev(int var1) {
      return (☃ + (4 - this.rotation / 90)) % 4;
   }

   public void setUvs(float[] var1) {
      if (this.uvs == null) {
         this.uvs = ☃;
      }
   }

   static class Deserializer implements JsonDeserializer<BlockFaceUV> {
      public BlockFaceUV deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         float[] ☃x = this.parseUV(☃);
         int ☃xx = this.parseRotation(☃);
         return new BlockFaceUV(☃x, ☃xx);
      }

      protected int parseRotation(JsonObject var1) {
         int ☃ = JsonUtils.getInt(☃, "rotation", 0);
         if (☃ >= 0 && ☃ % 90 == 0 && ☃ / 90 <= 3) {
            return ☃;
         } else {
            throw new JsonParseException("Invalid rotation " + ☃ + " found, only 0/90/180/270 allowed");
         }
      }

      @Nullable
      private float[] parseUV(JsonObject var1) {
         if (!☃.has("uv")) {
            return null;
         } else {
            JsonArray ☃ = JsonUtils.getJsonArray(☃, "uv");
            if (☃.size() != 4) {
               throw new JsonParseException("Expected 4 uv values, found: " + ☃.size());
            } else {
               float[] ☃x = new float[4];

               for (int ☃xx = 0; ☃xx < ☃x.length; ☃xx++) {
                  ☃x[☃xx] = JsonUtils.getFloat(☃.get(☃xx), "uv[" + ☃xx + "]");
               }

               return ☃x;
            }
         }
      }
   }
}
