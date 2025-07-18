package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class BlockPart {
   public final Vector3f positionFrom;
   public final Vector3f positionTo;
   public final Map<EnumFacing, BlockPartFace> mapFaces;
   public final BlockPartRotation partRotation;
   public final boolean shade;

   public BlockPart(Vector3f var1, Vector3f var2, Map<EnumFacing, BlockPartFace> var3, @Nullable BlockPartRotation var4, boolean var5) {
      this.positionFrom = ☃;
      this.positionTo = ☃;
      this.mapFaces = ☃;
      this.partRotation = ☃;
      this.shade = ☃;
      this.setDefaultUvs();
   }

   private void setDefaultUvs() {
      for (Entry<EnumFacing, BlockPartFace> ☃ : this.mapFaces.entrySet()) {
         float[] ☃x = this.getFaceUvs(☃.getKey());
         ☃.getValue().blockFaceUV.setUvs(☃x);
      }
   }

   private float[] getFaceUvs(EnumFacing var1) {
      switch (☃) {
         case DOWN:
            return new float[]{this.positionFrom.x, 16.0F - this.positionTo.z, this.positionTo.x, 16.0F - this.positionFrom.z};
         case UP:
            return new float[]{this.positionFrom.x, this.positionFrom.z, this.positionTo.x, this.positionTo.z};
         case NORTH:
         default:
            return new float[]{16.0F - this.positionTo.x, 16.0F - this.positionTo.y, 16.0F - this.positionFrom.x, 16.0F - this.positionFrom.y};
         case SOUTH:
            return new float[]{this.positionFrom.x, 16.0F - this.positionTo.y, this.positionTo.x, 16.0F - this.positionFrom.y};
         case WEST:
            return new float[]{this.positionFrom.z, 16.0F - this.positionTo.y, this.positionTo.z, 16.0F - this.positionFrom.y};
         case EAST:
            return new float[]{16.0F - this.positionTo.z, 16.0F - this.positionTo.y, 16.0F - this.positionFrom.z, 16.0F - this.positionFrom.y};
      }
   }

   static class Deserializer implements JsonDeserializer<BlockPart> {
      public BlockPart deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         Vector3f ☃x = this.parsePositionFrom(☃);
         Vector3f ☃xx = this.parsePositionTo(☃);
         BlockPartRotation ☃xxx = this.parseRotation(☃);
         Map<EnumFacing, BlockPartFace> ☃xxxx = this.parseFacesCheck(☃, ☃);
         if (☃.has("shade") && !JsonUtils.isBoolean(☃, "shade")) {
            throw new JsonParseException("Expected shade to be a Boolean");
         } else {
            boolean ☃xxxxx = JsonUtils.getBoolean(☃, "shade", true);
            return new BlockPart(☃x, ☃xx, ☃xxxx, ☃xxx, ☃xxxxx);
         }
      }

      @Nullable
      private BlockPartRotation parseRotation(JsonObject var1) {
         BlockPartRotation ☃ = null;
         if (☃.has("rotation")) {
            JsonObject ☃x = JsonUtils.getJsonObject(☃, "rotation");
            Vector3f ☃xx = this.parsePosition(☃x, "origin");
            ☃xx.scale(0.0625F);
            EnumFacing.Axis ☃xxx = this.parseAxis(☃x);
            float ☃xxxx = this.parseAngle(☃x);
            boolean ☃xxxxx = JsonUtils.getBoolean(☃x, "rescale", false);
            ☃ = new BlockPartRotation(☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
         }

         return ☃;
      }

      private float parseAngle(JsonObject var1) {
         float ☃ = JsonUtils.getFloat(☃, "angle");
         if (☃ != 0.0F && MathHelper.abs(☃) != 22.5F && MathHelper.abs(☃) != 45.0F) {
            throw new JsonParseException("Invalid rotation " + ☃ + " found, only -45/-22.5/0/22.5/45 allowed");
         } else {
            return ☃;
         }
      }

      private EnumFacing.Axis parseAxis(JsonObject var1) {
         String ☃ = JsonUtils.getString(☃, "axis");
         EnumFacing.Axis ☃x = EnumFacing.Axis.byName(☃.toLowerCase(Locale.ROOT));
         if (☃x == null) {
            throw new JsonParseException("Invalid rotation axis: " + ☃);
         } else {
            return ☃x;
         }
      }

      private Map<EnumFacing, BlockPartFace> parseFacesCheck(JsonDeserializationContext var1, JsonObject var2) {
         Map<EnumFacing, BlockPartFace> ☃ = this.parseFaces(☃, ☃);
         if (☃.isEmpty()) {
            throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
         } else {
            return ☃;
         }
      }

      private Map<EnumFacing, BlockPartFace> parseFaces(JsonDeserializationContext var1, JsonObject var2) {
         Map<EnumFacing, BlockPartFace> ☃ = Maps.newEnumMap(EnumFacing.class);
         JsonObject ☃x = JsonUtils.getJsonObject(☃, "faces");

         for (Entry<String, JsonElement> ☃xx : ☃x.entrySet()) {
            EnumFacing ☃xxx = this.parseEnumFacing(☃xx.getKey());
            ☃.put(☃xxx, (BlockPartFace)☃.deserialize(☃xx.getValue(), BlockPartFace.class));
         }

         return ☃;
      }

      private EnumFacing parseEnumFacing(String var1) {
         EnumFacing ☃ = EnumFacing.byName(☃);
         if (☃ == null) {
            throw new JsonParseException("Unknown facing: " + ☃);
         } else {
            return ☃;
         }
      }

      private Vector3f parsePositionTo(JsonObject var1) {
         Vector3f ☃ = this.parsePosition(☃, "to");
         if (!(☃.x < -16.0F) && !(☃.y < -16.0F) && !(☃.z < -16.0F) && !(☃.x > 32.0F) && !(☃.y > 32.0F) && !(☃.z > 32.0F)) {
            return ☃;
         } else {
            throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + ☃);
         }
      }

      private Vector3f parsePositionFrom(JsonObject var1) {
         Vector3f ☃ = this.parsePosition(☃, "from");
         if (!(☃.x < -16.0F) && !(☃.y < -16.0F) && !(☃.z < -16.0F) && !(☃.x > 32.0F) && !(☃.y > 32.0F) && !(☃.z > 32.0F)) {
            return ☃;
         } else {
            throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + ☃);
         }
      }

      private Vector3f parsePosition(JsonObject var1, String var2) {
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
