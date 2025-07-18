package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;

public class BlockPartFace {
   public static final EnumFacing FACING_DEFAULT = null;
   public final EnumFacing cullFace;
   public final int tintIndex;
   public final String texture;
   public final BlockFaceUV blockFaceUV;

   public BlockPartFace(@Nullable EnumFacing var1, int var2, String var3, BlockFaceUV var4) {
      this.cullFace = ☃;
      this.tintIndex = ☃;
      this.texture = ☃;
      this.blockFaceUV = ☃;
   }

   static class Deserializer implements JsonDeserializer<BlockPartFace> {
      public BlockPartFace deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         EnumFacing ☃x = this.parseCullFace(☃);
         int ☃xx = this.parseTintIndex(☃);
         String ☃xxx = this.parseTexture(☃);
         BlockFaceUV ☃xxxx = (BlockFaceUV)☃.deserialize(☃, BlockFaceUV.class);
         return new BlockPartFace(☃x, ☃xx, ☃xxx, ☃xxxx);
      }

      protected int parseTintIndex(JsonObject var1) {
         return JsonUtils.getInt(☃, "tintindex", -1);
      }

      private String parseTexture(JsonObject var1) {
         return JsonUtils.getString(☃, "texture");
      }

      @Nullable
      private EnumFacing parseCullFace(JsonObject var1) {
         String ☃ = JsonUtils.getString(☃, "cullface", "");
         return EnumFacing.byName(☃);
      }
   }
}
