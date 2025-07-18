package net.minecraft.client.renderer.block.model;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelBlock {
   private static final Logger LOGGER = LogManager.getLogger();
   @VisibleForTesting
   static final Gson SERIALIZER = new GsonBuilder()
      .registerTypeAdapter(ModelBlock.class, new ModelBlock.Deserializer())
      .registerTypeAdapter(BlockPart.class, new BlockPart.Deserializer())
      .registerTypeAdapter(BlockPartFace.class, new BlockPartFace.Deserializer())
      .registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer())
      .registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer())
      .registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer())
      .registerTypeAdapter(ItemOverride.class, new ItemOverride.Deserializer())
      .create();
   private final List<BlockPart> elements;
   private final boolean gui3d;
   private final boolean ambientOcclusion;
   private final ItemCameraTransforms cameraTransforms;
   private final List<ItemOverride> overrides;
   public String name = "";
   @VisibleForTesting
   protected final Map<String, String> textures;
   @VisibleForTesting
   protected ModelBlock parent;
   @VisibleForTesting
   protected ResourceLocation parentLocation;

   public static ModelBlock deserialize(Reader var0) {
      return JsonUtils.gsonDeserialize(SERIALIZER, ☃, ModelBlock.class, false);
   }

   public static ModelBlock deserialize(String var0) {
      return deserialize(new StringReader(☃));
   }

   public ModelBlock(
      @Nullable ResourceLocation var1,
      List<BlockPart> var2,
      Map<String, String> var3,
      boolean var4,
      boolean var5,
      ItemCameraTransforms var6,
      List<ItemOverride> var7
   ) {
      this.elements = ☃;
      this.ambientOcclusion = ☃;
      this.gui3d = ☃;
      this.textures = ☃;
      this.parentLocation = ☃;
      this.cameraTransforms = ☃;
      this.overrides = ☃;
   }

   public List<BlockPart> getElements() {
      return this.elements.isEmpty() && this.hasParent() ? this.parent.getElements() : this.elements;
   }

   private boolean hasParent() {
      return this.parent != null;
   }

   public boolean isAmbientOcclusion() {
      return this.hasParent() ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
   }

   public boolean isGui3d() {
      return this.gui3d;
   }

   public boolean isResolved() {
      return this.parentLocation == null || this.parent != null && this.parent.isResolved();
   }

   public void getParentFromMap(Map<ResourceLocation, ModelBlock> var1) {
      if (this.parentLocation != null) {
         this.parent = ☃.get(this.parentLocation);
      }
   }

   public Collection<ResourceLocation> getOverrideLocations() {
      Set<ResourceLocation> ☃ = Sets.newHashSet();

      for (ItemOverride ☃x : this.overrides) {
         ☃.add(☃x.getLocation());
      }

      return ☃;
   }

   protected List<ItemOverride> getOverrides() {
      return this.overrides;
   }

   public ItemOverrideList createOverrides() {
      return this.overrides.isEmpty() ? ItemOverrideList.NONE : new ItemOverrideList(this.overrides);
   }

   public boolean isTexturePresent(String var1) {
      return !"missingno".equals(this.resolveTextureName(☃));
   }

   public String resolveTextureName(String var1) {
      if (!this.startsWithHash(☃)) {
         ☃ = '#' + ☃;
      }

      return this.resolveTextureName(☃, new ModelBlock.Bookkeep(this));
   }

   private String resolveTextureName(String var1, ModelBlock.Bookkeep var2) {
      if (this.startsWithHash(☃)) {
         if (this == ☃.modelExt) {
            LOGGER.warn("Unable to resolve texture due to upward reference: {} in {}", ☃, this.name);
            return "missingno";
         } else {
            String ☃ = this.textures.get(☃.substring(1));
            if (☃ == null && this.hasParent()) {
               ☃ = this.parent.resolveTextureName(☃, ☃);
            }

            ☃.modelExt = this;
            if (☃ != null && this.startsWithHash(☃)) {
               ☃ = ☃.model.resolveTextureName(☃, ☃);
            }

            return ☃ != null && !this.startsWithHash(☃) ? ☃ : "missingno";
         }
      } else {
         return ☃;
      }
   }

   private boolean startsWithHash(String var1) {
      return ☃.charAt(0) == '#';
   }

   @Nullable
   public ResourceLocation getParentLocation() {
      return this.parentLocation;
   }

   public ModelBlock getRootModel() {
      return this.hasParent() ? this.parent.getRootModel() : this;
   }

   public ItemCameraTransforms getAllTransforms() {
      ItemTransformVec3f ☃ = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
      ItemTransformVec3f ☃x = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
      ItemTransformVec3f ☃xx = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND);
      ItemTransformVec3f ☃xxx = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
      ItemTransformVec3f ☃xxxx = this.getTransform(ItemCameraTransforms.TransformType.HEAD);
      ItemTransformVec3f ☃xxxxx = this.getTransform(ItemCameraTransforms.TransformType.GUI);
      ItemTransformVec3f ☃xxxxxx = this.getTransform(ItemCameraTransforms.TransformType.GROUND);
      ItemTransformVec3f ☃xxxxxxx = this.getTransform(ItemCameraTransforms.TransformType.FIXED);
      return new ItemCameraTransforms(☃, ☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
   }

   private ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType var1) {
      return this.parent != null && !this.cameraTransforms.hasCustomTransform(☃) ? this.parent.getTransform(☃) : this.cameraTransforms.getTransform(☃);
   }

   public static void checkModelHierarchy(Map<ResourceLocation, ModelBlock> var0) {
      for (ModelBlock ☃ : ☃.values()) {
         try {
            ModelBlock ☃x = ☃.parent;

            for (ModelBlock ☃xx = ☃x.parent; ☃x != ☃xx; ☃xx = ☃xx.parent.parent) {
               ☃x = ☃x.parent;
            }

            throw new ModelBlock.LoopException();
         } catch (NullPointerException var5) {
         }
      }
   }

   static final class Bookkeep {
      public final ModelBlock model;
      public ModelBlock modelExt;

      private Bookkeep(ModelBlock var1) {
         this.model = ☃;
      }
   }

   public static class Deserializer implements JsonDeserializer<ModelBlock> {
      public ModelBlock deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         List<BlockPart> ☃x = this.getModelElements(☃, ☃);
         String ☃xx = this.getParent(☃);
         Map<String, String> ☃xxx = this.getTextures(☃);
         boolean ☃xxxx = this.getAmbientOcclusionEnabled(☃);
         ItemCameraTransforms ☃xxxxx = ItemCameraTransforms.DEFAULT;
         if (☃.has("display")) {
            JsonObject ☃xxxxxx = JsonUtils.getJsonObject(☃, "display");
            ☃xxxxx = (ItemCameraTransforms)☃.deserialize(☃xxxxxx, ItemCameraTransforms.class);
         }

         List<ItemOverride> ☃xxxxxx = this.getItemOverrides(☃, ☃);
         ResourceLocation ☃xxxxxxx = ☃xx.isEmpty() ? null : new ResourceLocation(☃xx);
         return new ModelBlock(☃xxxxxxx, ☃x, ☃xxx, ☃xxxx, true, ☃xxxxx, ☃xxxxxx);
      }

      protected List<ItemOverride> getItemOverrides(JsonDeserializationContext var1, JsonObject var2) {
         List<ItemOverride> ☃ = Lists.newArrayList();
         if (☃.has("overrides")) {
            for (JsonElement ☃x : JsonUtils.getJsonArray(☃, "overrides")) {
               ☃.add((ItemOverride)☃.deserialize(☃x, ItemOverride.class));
            }
         }

         return ☃;
      }

      private Map<String, String> getTextures(JsonObject var1) {
         Map<String, String> ☃ = Maps.newHashMap();
         if (☃.has("textures")) {
            JsonObject ☃x = ☃.getAsJsonObject("textures");

            for (Entry<String, JsonElement> ☃xx : ☃x.entrySet()) {
               ☃.put(☃xx.getKey(), ☃xx.getValue().getAsString());
            }
         }

         return ☃;
      }

      private String getParent(JsonObject var1) {
         return JsonUtils.getString(☃, "parent", "");
      }

      protected boolean getAmbientOcclusionEnabled(JsonObject var1) {
         return JsonUtils.getBoolean(☃, "ambientocclusion", true);
      }

      protected List<BlockPart> getModelElements(JsonDeserializationContext var1, JsonObject var2) {
         List<BlockPart> ☃ = Lists.newArrayList();
         if (☃.has("elements")) {
            for (JsonElement ☃x : JsonUtils.getJsonArray(☃, "elements")) {
               ☃.add((BlockPart)☃.deserialize(☃x, BlockPart.class));
            }
         }

         return ☃;
      }
   }

   public static class LoopException extends RuntimeException {
   }
}
