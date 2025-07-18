package net.minecraft.client.util;

import com.google.gson.JsonObject;
import java.util.Locale;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.JsonUtils;

public class JsonBlendingMode {
   private static JsonBlendingMode lastApplied;
   private final int srcColorFactor;
   private final int srcAlphaFactor;
   private final int destColorFactor;
   private final int destAlphaFactor;
   private final int blendFunction;
   private final boolean separateBlend;
   private final boolean opaque;

   private JsonBlendingMode(boolean var1, boolean var2, int var3, int var4, int var5, int var6, int var7) {
      this.separateBlend = ☃;
      this.srcColorFactor = ☃;
      this.destColorFactor = ☃;
      this.srcAlphaFactor = ☃;
      this.destAlphaFactor = ☃;
      this.opaque = ☃;
      this.blendFunction = ☃;
   }

   public JsonBlendingMode() {
      this(false, true, 1, 0, 1, 0, 32774);
   }

   public JsonBlendingMode(int var1, int var2, int var3) {
      this(false, false, ☃, ☃, ☃, ☃, ☃);
   }

   public JsonBlendingMode(int var1, int var2, int var3, int var4, int var5) {
      this(true, false, ☃, ☃, ☃, ☃, ☃);
   }

   public void apply() {
      if (!this.equals(lastApplied)) {
         if (lastApplied == null || this.opaque != lastApplied.isOpaque()) {
            lastApplied = this;
            if (this.opaque) {
               GlStateManager.disableBlend();
               return;
            }

            GlStateManager.enableBlend();
         }

         GlStateManager.glBlendEquation(this.blendFunction);
         if (this.separateBlend) {
            GlStateManager.tryBlendFuncSeparate(this.srcColorFactor, this.destColorFactor, this.srcAlphaFactor, this.destAlphaFactor);
         } else {
            GlStateManager.blendFunc(this.srcColorFactor, this.destColorFactor);
         }
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof JsonBlendingMode)) {
         return false;
      } else {
         JsonBlendingMode ☃ = (JsonBlendingMode)☃;
         if (this.blendFunction != ☃.blendFunction) {
            return false;
         } else if (this.destAlphaFactor != ☃.destAlphaFactor) {
            return false;
         } else if (this.destColorFactor != ☃.destColorFactor) {
            return false;
         } else if (this.opaque != ☃.opaque) {
            return false;
         } else if (this.separateBlend != ☃.separateBlend) {
            return false;
         } else {
            return this.srcAlphaFactor != ☃.srcAlphaFactor ? false : this.srcColorFactor == ☃.srcColorFactor;
         }
      }
   }

   @Override
   public int hashCode() {
      int ☃ = this.srcColorFactor;
      ☃ = 31 * ☃ + this.srcAlphaFactor;
      ☃ = 31 * ☃ + this.destColorFactor;
      ☃ = 31 * ☃ + this.destAlphaFactor;
      ☃ = 31 * ☃ + this.blendFunction;
      ☃ = 31 * ☃ + (this.separateBlend ? 1 : 0);
      return 31 * ☃ + (this.opaque ? 1 : 0);
   }

   public boolean isOpaque() {
      return this.opaque;
   }

   public static JsonBlendingMode parseBlendNode(JsonObject var0) {
      if (☃ == null) {
         return new JsonBlendingMode();
      } else {
         int ☃ = 32774;
         int ☃x = 1;
         int ☃xx = 0;
         int ☃xxx = 1;
         int ☃xxxx = 0;
         boolean ☃xxxxx = true;
         boolean ☃xxxxxx = false;
         if (JsonUtils.isString(☃, "func")) {
            ☃ = stringToBlendFunction(☃.get("func").getAsString());
            if (☃ != 32774) {
               ☃xxxxx = false;
            }
         }

         if (JsonUtils.isString(☃, "srcrgb")) {
            ☃x = stringToBlendFactor(☃.get("srcrgb").getAsString());
            if (☃x != 1) {
               ☃xxxxx = false;
            }
         }

         if (JsonUtils.isString(☃, "dstrgb")) {
            ☃xx = stringToBlendFactor(☃.get("dstrgb").getAsString());
            if (☃xx != 0) {
               ☃xxxxx = false;
            }
         }

         if (JsonUtils.isString(☃, "srcalpha")) {
            ☃xxx = stringToBlendFactor(☃.get("srcalpha").getAsString());
            if (☃xxx != 1) {
               ☃xxxxx = false;
            }

            ☃xxxxxx = true;
         }

         if (JsonUtils.isString(☃, "dstalpha")) {
            ☃xxxx = stringToBlendFactor(☃.get("dstalpha").getAsString());
            if (☃xxxx != 0) {
               ☃xxxxx = false;
            }

            ☃xxxxxx = true;
         }

         if (☃xxxxx) {
            return new JsonBlendingMode();
         } else {
            return ☃xxxxxx ? new JsonBlendingMode(☃x, ☃xx, ☃xxx, ☃xxxx, ☃) : new JsonBlendingMode(☃x, ☃xx, ☃);
         }
      }
   }

   private static int stringToBlendFunction(String var0) {
      String ☃ = ☃.trim().toLowerCase(Locale.ROOT);
      if ("add".equals(☃)) {
         return 32774;
      } else if ("subtract".equals(☃)) {
         return 32778;
      } else if ("reversesubtract".equals(☃)) {
         return 32779;
      } else if ("reverse_subtract".equals(☃)) {
         return 32779;
      } else if ("min".equals(☃)) {
         return 32775;
      } else {
         return "max".equals(☃) ? 32776 : 32774;
      }
   }

   private static int stringToBlendFactor(String var0) {
      String ☃ = ☃.trim().toLowerCase(Locale.ROOT);
      ☃ = ☃.replaceAll("_", "");
      ☃ = ☃.replaceAll("one", "1");
      ☃ = ☃.replaceAll("zero", "0");
      ☃ = ☃.replaceAll("minus", "-");
      if ("0".equals(☃)) {
         return 0;
      } else if ("1".equals(☃)) {
         return 1;
      } else if ("srccolor".equals(☃)) {
         return 768;
      } else if ("1-srccolor".equals(☃)) {
         return 769;
      } else if ("dstcolor".equals(☃)) {
         return 774;
      } else if ("1-dstcolor".equals(☃)) {
         return 775;
      } else if ("srcalpha".equals(☃)) {
         return 770;
      } else if ("1-srcalpha".equals(☃)) {
         return 771;
      } else if ("dstalpha".equals(☃)) {
         return 772;
      } else {
         return "1-dstalpha".equals(☃) ? 773 : -1;
      }
   }
}
