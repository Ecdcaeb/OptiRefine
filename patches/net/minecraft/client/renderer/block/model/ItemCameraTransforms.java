package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Quaternion;

public class ItemCameraTransforms {
   public static final ItemCameraTransforms DEFAULT = new ItemCameraTransforms();
   public static float offsetTranslateX;
   public static float offsetTranslateY;
   public static float offsetTranslateZ;
   public static float offsetRotationX;
   public static float offsetRotationY;
   public static float offsetRotationZ;
   public static float offsetScaleX;
   public static float offsetScaleY;
   public static float offsetScaleZ;
   public final ItemTransformVec3f thirdperson_left;
   public final ItemTransformVec3f thirdperson_right;
   public final ItemTransformVec3f firstperson_left;
   public final ItemTransformVec3f firstperson_right;
   public final ItemTransformVec3f head;
   public final ItemTransformVec3f gui;
   public final ItemTransformVec3f ground;
   public final ItemTransformVec3f fixed;

   private ItemCameraTransforms() {
      this(
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT
      );
   }

   public ItemCameraTransforms(ItemCameraTransforms var1) {
      this.thirdperson_left = ☃.thirdperson_left;
      this.thirdperson_right = ☃.thirdperson_right;
      this.firstperson_left = ☃.firstperson_left;
      this.firstperson_right = ☃.firstperson_right;
      this.head = ☃.head;
      this.gui = ☃.gui;
      this.ground = ☃.ground;
      this.fixed = ☃.fixed;
   }

   public ItemCameraTransforms(
      ItemTransformVec3f var1,
      ItemTransformVec3f var2,
      ItemTransformVec3f var3,
      ItemTransformVec3f var4,
      ItemTransformVec3f var5,
      ItemTransformVec3f var6,
      ItemTransformVec3f var7,
      ItemTransformVec3f var8
   ) {
      this.thirdperson_left = ☃;
      this.thirdperson_right = ☃;
      this.firstperson_left = ☃;
      this.firstperson_right = ☃;
      this.head = ☃;
      this.gui = ☃;
      this.ground = ☃;
      this.fixed = ☃;
   }

   public void applyTransform(ItemCameraTransforms.TransformType var1) {
      applyTransformSide(this.getTransform(☃), false);
   }

   public static void applyTransformSide(ItemTransformVec3f var0, boolean var1) {
      if (☃ != ItemTransformVec3f.DEFAULT) {
         int ☃ = ☃ ? -1 : 1;
         GlStateManager.translate(☃ * (offsetTranslateX + ☃.translation.x), offsetTranslateY + ☃.translation.y, offsetTranslateZ + ☃.translation.z);
         float ☃x = offsetRotationX + ☃.rotation.x;
         float ☃xx = offsetRotationY + ☃.rotation.y;
         float ☃xxx = offsetRotationZ + ☃.rotation.z;
         if (☃) {
            ☃xx = -☃xx;
            ☃xxx = -☃xxx;
         }

         GlStateManager.rotate(makeQuaternion(☃x, ☃xx, ☃xxx));
         GlStateManager.scale(offsetScaleX + ☃.scale.x, offsetScaleY + ☃.scale.y, offsetScaleZ + ☃.scale.z);
      }
   }

   private static Quaternion makeQuaternion(float var0, float var1, float var2) {
      float ☃ = ☃ * (float) (Math.PI / 180.0);
      float ☃x = ☃ * (float) (Math.PI / 180.0);
      float ☃xx = ☃ * (float) (Math.PI / 180.0);
      float ☃xxx = MathHelper.sin(0.5F * ☃);
      float ☃xxxx = MathHelper.cos(0.5F * ☃);
      float ☃xxxxx = MathHelper.sin(0.5F * ☃x);
      float ☃xxxxxx = MathHelper.cos(0.5F * ☃x);
      float ☃xxxxxxx = MathHelper.sin(0.5F * ☃xx);
      float ☃xxxxxxxx = MathHelper.cos(0.5F * ☃xx);
      return new Quaternion(
         ☃xxx * ☃xxxxxx * ☃xxxxxxxx + ☃xxxx * ☃xxxxx * ☃xxxxxxx,
         ☃xxxx * ☃xxxxx * ☃xxxxxxxx - ☃xxx * ☃xxxxxx * ☃xxxxxxx,
         ☃xxx * ☃xxxxx * ☃xxxxxxxx + ☃xxxx * ☃xxxxxx * ☃xxxxxxx,
         ☃xxxx * ☃xxxxxx * ☃xxxxxxxx - ☃xxx * ☃xxxxx * ☃xxxxxxx
      );
   }

   public ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType var1) {
      switch (☃) {
         case THIRD_PERSON_LEFT_HAND:
            return this.thirdperson_left;
         case THIRD_PERSON_RIGHT_HAND:
            return this.thirdperson_right;
         case FIRST_PERSON_LEFT_HAND:
            return this.firstperson_left;
         case FIRST_PERSON_RIGHT_HAND:
            return this.firstperson_right;
         case HEAD:
            return this.head;
         case GUI:
            return this.gui;
         case GROUND:
            return this.ground;
         case FIXED:
            return this.fixed;
         default:
            return ItemTransformVec3f.DEFAULT;
      }
   }

   public boolean hasCustomTransform(ItemCameraTransforms.TransformType var1) {
      return this.getTransform(☃) != ItemTransformVec3f.DEFAULT;
   }

   static class Deserializer implements JsonDeserializer<ItemCameraTransforms> {
      public ItemCameraTransforms deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         ItemTransformVec3f ☃x = this.getTransform(☃, ☃, "thirdperson_righthand");
         ItemTransformVec3f ☃xx = this.getTransform(☃, ☃, "thirdperson_lefthand");
         if (☃xx == ItemTransformVec3f.DEFAULT) {
            ☃xx = ☃x;
         }

         ItemTransformVec3f ☃xxx = this.getTransform(☃, ☃, "firstperson_righthand");
         ItemTransformVec3f ☃xxxx = this.getTransform(☃, ☃, "firstperson_lefthand");
         if (☃xxxx == ItemTransformVec3f.DEFAULT) {
            ☃xxxx = ☃xxx;
         }

         ItemTransformVec3f ☃xxxxx = this.getTransform(☃, ☃, "head");
         ItemTransformVec3f ☃xxxxxx = this.getTransform(☃, ☃, "gui");
         ItemTransformVec3f ☃xxxxxxx = this.getTransform(☃, ☃, "ground");
         ItemTransformVec3f ☃xxxxxxxx = this.getTransform(☃, ☃, "fixed");
         return new ItemCameraTransforms(☃xx, ☃x, ☃xxxx, ☃xxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
      }

      private ItemTransformVec3f getTransform(JsonDeserializationContext var1, JsonObject var2, String var3) {
         return ☃.has(☃) ? (ItemTransformVec3f)☃.deserialize(☃.get(☃), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
      }
   }

   public static enum TransformType {
      NONE,
      THIRD_PERSON_LEFT_HAND,
      THIRD_PERSON_RIGHT_HAND,
      FIRST_PERSON_LEFT_HAND,
      FIRST_PERSON_RIGHT_HAND,
      HEAD,
      GUI,
      GROUND,
      FIXED;
   }
}
