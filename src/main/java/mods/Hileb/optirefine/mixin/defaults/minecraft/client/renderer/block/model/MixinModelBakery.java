package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import net.optifine.util.StrUtils;
import net.optifine.util.TextureUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;

@Mixin(ModelBakery.class)
public abstract class MixinModelBakery {
    @Inject(method = "loadVariantItemModels", at = @At("TAIL"))
    public void $loadVariantItemModels(CallbackInfo ci) {
        CustomItems.update();
        CustomItems.loadModels((ModelBakery)(Object) this);
    }

    @ModifyReturnValue(method = "getModelLocation", at = @At("RETURN"))
    public ResourceLocation modifyMcPatcherLocation(ResourceLocation original, @Local(argsOnly = true) ResourceLocation arg){
        String path = arg.getPath();
        String name = arg.getNamespace();
        if (!path.startsWith("mcpatcher") && !path.startsWith("optifine")) {
            return original;
        } else {
            if (!path.endsWith(".json")) {
                return new ResourceLocation(name, path + ".json");
            }
            return arg;
        }
    }


    private static String fixResourcePath(String path, String basePath) {
        path = TextureUtils.fixResourcePath(path, basePath);
        path = StrUtils.removeSuffix(path, ".json");
        return StrUtils.removeSuffix(path, ".png");
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    private static ResourceLocation fixModelLocation(ResourceLocation loc, String basePath) {
        if (loc != null && basePath != null) {
            if (!loc.getNamespace().equals("minecraft")) {
                return loc;
            } else {
                String path = loc.getPath();
                String pathFixed = fixResourcePath(path, basePath);
                if (!Objects.equals(pathFixed, path)) {
                    loc = new ResourceLocation(loc.getNamespace(), pathFixed);
                }

                return loc;
            }
        } else {
            return loc;
        }
    }

    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.block.model.ModelBlock field_178316_e Lnet.minecraft.util.ResourceLocation;", deobf = true)
    private static native void ModelBlock_parentLocation_set(ModelBlock modelBlock, ResourceLocation resourceLocation);

    @Public @Unique
    private static void fixModelLocations(ModelBlock modelBlock, String basePath) {
        ResourceLocation parentLocFixed = fixModelLocation(modelBlock.getParentLocation(), basePath);
        if (parentLocFixed != modelBlock.getParentLocation()) {
            ModelBlock_parentLocation_set(modelBlock, parentLocFixed);
        }

        if (modelBlock.textures != null) {
            for (Map.Entry<String, String> entry : modelBlock.textures.entrySet()) {
                String path = entry.getValue();
                String pathFixed = fixResourcePath(path, basePath);
                if (!Objects.equals(pathFixed, path)) {
                    entry.setValue(pathFixed);
                }
            }
        }
    }

    @Shadow @Final
    private Map<ResourceLocation, ModelBlock> models;

    @Unique
    public ModelBlock getModelBlock(ResourceLocation resourceLocation) {
        return this.models.get(resourceLocation);
    }

    @WrapOperation(method = "loadModel", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/block/model/ModelBlock;name:Ljava/lang/String;"))
    public void fixLocation(ModelBlock instance, String value, Operation<Void> original, @Local(argsOnly = true) ResourceLocation resourceLocation){
        original.call(instance, value);
        String basePath = TextureUtils.getBasePath(resourceLocation.getPath());
        fixModelLocations(instance, basePath);
    }
}
/*
+++ net/minecraft/client/renderer/block/model/ModelBakery.java	Tue Aug 19 14:59:58 2025
@@ -25,12 +25,13 @@
 import java.util.Map;
 import java.util.Set;
 import java.util.Map.Entry;
 import javax.annotation.Nullable;
 import net.minecraft.block.Block;
 import net.minecraft.client.renderer.BlockModelShapes;
+import net.minecraft.client.renderer.block.model.MultipartBakedModel.Builder;
 import net.minecraft.client.renderer.block.model.multipart.Multipart;
 import net.minecraft.client.renderer.block.model.multipart.Selector;
 import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
 import net.minecraft.client.renderer.texture.ITextureMapPopulator;
 import net.minecraft.client.renderer.texture.TextureAtlasSprite;
 import net.minecraft.client.renderer.texture.TextureMap;
@@ -40,12 +41,19 @@
 import net.minecraft.init.Items;
 import net.minecraft.item.Item;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.registry.IRegistry;
 import net.minecraft.util.registry.RegistrySimple;
+import net.minecraftforge.common.model.ITransformation;
+import net.minecraftforge.common.model.TRSRTransformation;
+import net.minecraftforge.registries.IRegistryDelegate;
+import net.optifine.CustomItems;
+import net.optifine.reflect.Reflector;
+import net.optifine.util.StrUtils;
+import net.optifine.util.TextureUtils;
 import org.apache.commons.io.IOUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class ModelBakery {
    private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet(
@@ -100,20 +108,21 @@
    private final Map<ModelResourceLocation, VariantList> variants = Maps.newLinkedHashMap();
    private final Map<ModelBlockDefinition, Collection<ModelResourceLocation>> multipartVariantMap = Maps.newLinkedHashMap();
    private final TextureMap textureMap;
    private final BlockModelShapes blockModelShapes;
    private final FaceBakery faceBakery = new FaceBakery();
    private final ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
-   private final RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple<>();
+   private final RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple();
    private static final String EMPTY_MODEL_RAW = "{    'elements': [        {   'from': [0, 0, 0],            'to': [16, 16, 16],            'faces': {                'down': {'uv': [0, 0, 16, 16], 'texture': '' }            }        }    ]}"
       .replaceAll("'", "\"");
    private static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize(EMPTY_MODEL_RAW);
    private static final ModelBlock MODEL_ENTITY = ModelBlock.deserialize(EMPTY_MODEL_RAW);
    private final Map<String, ResourceLocation> itemLocations = Maps.newLinkedHashMap();
    private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = Maps.newHashMap();
    private final Map<Item, List<String>> variantNames = Maps.newIdentityHashMap();
+   private static Map<IRegistryDelegate<Item>, Set<String>> customVariantNames = Maps.newHashMap();

    public ModelBakery(IResourceManager var1, TextureMap var2, BlockModelShapes var3) {
       this.resourceManager = var1;
       this.textureMap = var2;
       this.blockModelShapes = var3;
    }
@@ -130,46 +139,54 @@
    }

    private void loadBlocks() {
       BlockStateMapper var1 = this.blockModelShapes.getBlockStateMapper();

       for (Block var3 : Block.REGISTRY) {
-         for (final ResourceLocation var5 : var1.getBlockstateLocations(var3)) {
+         for (ResourceLocation var5 : var1.getBlockstateLocations(var3)) {
             try {
-               ModelBlockDefinition var6 = this.getModelBlockDefinition(var5);
-               Map var7 = var1.getVariants(var3);
-               if (var6.hasMultipartData()) {
-                  HashSet var8 = Sets.newHashSet(var7.values());
-                  var6.getMultipartData().setStateContainer(var3.getBlockState());
-                  Object var9 = this.multipartVariantMap.get(var6);
-                  if (var9 == null) {
-                     var9 = Lists.newArrayList();
-                     this.multipartVariantMap.put(var6, (Collection<ModelResourceLocation>)var9);
-                  }
+               this.loadBlock(var1, var3, var5);
+            } catch (Exception var7) {
+               LOGGER.warn("Unable to load definition " + var5, var7);
+            }
+         }
+      }
+   }

-                  var9.addAll(Lists.newArrayList(Iterables.filter(var8, new Predicate<ModelResourceLocation>() {
-                     public boolean apply(@Nullable ModelResourceLocation var1) {
-                        return var5.equals(var1);
-                     }
-                  })));
-               }
-
-               for (Entry var15 : var7.entrySet()) {
-                  ModelResourceLocation var10 = (ModelResourceLocation)var15.getValue();
-                  if (var5.equals(var10)) {
-                     try {
-                        this.variants.put(var10, var6.getVariant(var10.getVariant()));
-                     } catch (RuntimeException var12) {
-                        if (!var6.hasMultipartData()) {
-                           LOGGER.warn("Unable to load variant: {} from {}", var10.getVariant(), var10);
-                        }
-                     }
-                  }
+   protected void loadBlock(BlockStateMapper var1, Block var2, final ResourceLocation var3) {
+      ModelBlockDefinition var4 = this.getModelBlockDefinition(var3);
+      Map var5 = var1.getVariants(var2);
+      if (var4.hasMultipartData()) {
+         HashSet var6 = Sets.newHashSet(var5.values());
+         var4.getMultipartData().setStateContainer(var2.getBlockState());
+         Object var7 = this.multipartVariantMap.get(var4);
+         if (var7 == null) {
+            var7 = Lists.newArrayList();
+         }
+
+         var7.addAll(Lists.newArrayList(Iterables.filter(var6, new Predicate<ModelResourceLocation>() {
+            public boolean apply(@Nullable ModelResourceLocation var1) {
+               return var3.equals(var1);
+            }
+         })));
+         this.registerMultipartVariant(var4, (Collection<ModelResourceLocation>)var7);
+      }
+
+      for (Entry var12 : var5.entrySet()) {
+         ModelResourceLocation var8 = (ModelResourceLocation)var12.getValue();
+         if (var3.equals(var8)) {
+            try {
+               if (Reflector.ForgeItem_delegate.exists()) {
+                  this.registerVariant(var4, var8);
+               } else {
+                  this.variants.put(var8, var4.getVariant(var8.getVariant()));
+               }
+            } catch (RuntimeException var10) {
+               if (!var4.hasMultipartData()) {
+                  LOGGER.warn("Unable to load variant: " + var8.getVariant() + " from " + var8, var10);
                }
-            } catch (Exception var13) {
-               LOGGER.warn("Unable to load definition {}", var5, var13);
             }
          }
       }
    }

    private void loadVariantItemModels() {
@@ -179,12 +196,14 @@
             new VariantList(Lists.newArrayList(new Variant[]{new Variant(new ResourceLocation(MODEL_MISSING.getPath()), ModelRotation.X0_Y0, false, 1)}))
          );
       this.loadStaticModels();
       this.loadVariantModels();
       this.loadMultipartVariantModels();
       this.loadItemModels();
+      CustomItems.update();
+      CustomItems.loadModels(this);
    }

    private void loadStaticModels() {
       ResourceLocation var1 = new ResourceLocation("item_frame");
       ModelBlockDefinition var2 = this.getModelBlockDefinition(var1);
       this.registerVariant(var2, new ModelResourceLocation(var1, "normal"));
@@ -229,23 +248,29 @@
    private ModelBlockDefinition loadModelBlockDefinition(ResourceLocation var1, IResource var2) {
       InputStream var3 = null;

       ModelBlockDefinition var4;
       try {
          var3 = var2.getInputStream();
-         var4 = ModelBlockDefinition.parseFromReader(new InputStreamReader(var3, StandardCharsets.UTF_8));
-      } catch (Exception var8) {
+         if (Reflector.ForgeModelBlockDefinition_parseFromReader2.exists()) {
+            var4 = (ModelBlockDefinition)Reflector.call(
+               Reflector.ForgeModelBlockDefinition_parseFromReader2, new Object[]{new InputStreamReader(var3, StandardCharsets.UTF_8), var1}
+            );
+         } else {
+            var4 = ModelBlockDefinition.parseFromReader(new InputStreamReader(var3, StandardCharsets.UTF_8));
+         }
+      } catch (Exception var9) {
          throw new RuntimeException(
             "Encountered an exception when loading model definition of '"
                + var1
                + "' from: '"
                + var2.getResourceLocation()
                + "' in resourcepack: '"
                + var2.getResourcePackName()
                + "'",
-            var8
+            var9
          );
       } finally {
          IOUtils.closeQuietly(var3);
       }

       return var4;
@@ -285,84 +310,99 @@
    }

    private ModelBlock loadModel(ResourceLocation var1) throws IOException {
       Object var2 = null;
       IResource var3 = null;

-      ModelBlock var5;
+      ModelBlock var6;
       try {
-         String var4 = var1.getPath();
-         if ("builtin/generated".equals(var4)) {
+         String var5 = var1.getPath();
+         if ("builtin/generated".equals(var5)) {
             return MODEL_GENERATED;
          }

-         if (!"builtin/entity".equals(var4)) {
-            if (var4.startsWith("builtin/")) {
-               String var10 = var4.substring("builtin/".length());
-               String var6 = BUILT_IN_MODELS.get(var10);
-               if (var6 == null) {
+         if (!"builtin/entity".equals(var5)) {
+            if (var5.startsWith("builtin/")) {
+               String var14 = var5.substring("builtin/".length());
+               String var7 = BUILT_IN_MODELS.get(var14);
+               if (var7 == null) {
                   throw new FileNotFoundException(var1.toString());
                }

-               var2 = new StringReader(var6);
+               var2 = new StringReader(var7);
             } else {
-               var3 = this.resourceManager.getResource(this.getModelLocation(var1));
+               var1 = this.getModelLocation(var1);
+               var3 = this.resourceManager.getResource(var1);
                var2 = new InputStreamReader(var3.getInputStream(), StandardCharsets.UTF_8);
             }

-            var5 = ModelBlock.deserialize((Reader)var2);
-            var5.name = var1.toString();
-            return var5;
+            ModelBlock var12 = ModelBlock.deserialize((Reader)var2);
+            var12.name = var1.toString();
+            String var15 = TextureUtils.getBasePath(var1.getPath());
+            fixModelLocations(var12, var15);
+            return var12;
          }

-         var5 = MODEL_ENTITY;
+         ModelBlock var4 = MODEL_ENTITY;
+         var6 = var4;
       } finally {
          IOUtils.closeQuietly((Reader)var2);
          IOUtils.closeQuietly(var3);
       }

-      return var5;
+      return var6;
    }

    private ResourceLocation getModelLocation(ResourceLocation var1) {
-      return new ResourceLocation(var1.getNamespace(), "models/" + var1.getPath() + ".json");
+      String var2 = var1.getPath();
+      if (!var2.startsWith("mcpatcher") && !var2.startsWith("optifine")) {
+         return new ResourceLocation(var1.getNamespace(), "models/" + var1.getPath() + ".json");
+      } else {
+         if (!var2.endsWith(".json")) {
+            var1 = new ResourceLocation(var1.getNamespace(), var2 + ".json");
+         }
+
+         return var1;
+      }
    }

    private void loadItemModels() {
       this.registerVariantNames();

       for (Item var2 : Item.REGISTRY) {
-         for (String var5 : this.getVariantNames(var2)) {
-            ResourceLocation var6 = this.getItemLocation(var5);
-            ResourceLocation var7 = Item.REGISTRY.getNameForObject(var2);
-            this.loadItemModel(var5, var6, var7);
+         for (String var4 : this.getVariantNames(var2)) {
+            ResourceLocation var5 = this.getItemLocation(var4);
+            ResourceLocation var6 = (ResourceLocation)Item.REGISTRY.getNameForObject(var2);
+            this.loadItemModel(var4, var5, var6);
             if (var2.hasCustomProperties()) {
-               ModelBlock var8 = this.models.get(var6);
-               if (var8 != null) {
-                  for (ResourceLocation var10 : var8.getOverrideLocations()) {
-                     this.loadItemModel(var10.toString(), var10, var7);
+               ModelBlock var7 = this.models.get(var5);
+               if (var7 != null) {
+                  for (ResourceLocation var9 : var7.getOverrideLocations()) {
+                     this.loadItemModel(var9.toString(), var9, var6);
                   }
                }
             }
          }
       }
    }

-   private void loadItemModel(String var1, ResourceLocation var2, ResourceLocation var3) {
+   public void loadItemModel(String var1, ResourceLocation var2, ResourceLocation var3) {
       this.itemLocations.put(var1, var2);
       if (this.models.get(var2) == null) {
          try {
             ModelBlock var4 = this.loadModel(var2);
             this.models.put(var2, var4);
          } catch (Exception var5) {
-            LOGGER.warn("Unable to load item model: '{}' for item: '{}'", var2, var3, var5);
+            LOGGER.warn("Unable to load item model: '{}' for item: '{}'", new Object[]{var2, var3});
+            LOGGER.warn(var5.getClass().getName() + ": " + var5.getMessage());
          }
       }
    }

    private void registerVariantNames() {
+      this.variantNames.clear();
       this.variantNames
          .put(
             Item.getItemFromBlock(Blocks.STONE),
             Lists.newArrayList(new String[]{"stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth"})
          );
       this.variantNames.put(Item.getItemFromBlock(Blocks.DIRT), Lists.newArrayList(new String[]{"dirt", "coarse_dirt", "podzol"}));
@@ -642,25 +682,33 @@
       this.variantNames.put(Item.getItemFromBlock(Blocks.AIR), Collections.emptyList());
       this.variantNames.put(Item.getItemFromBlock(Blocks.OAK_FENCE_GATE), Lists.newArrayList(new String[]{"oak_fence_gate"}));
       this.variantNames.put(Item.getItemFromBlock(Blocks.OAK_FENCE), Lists.newArrayList(new String[]{"oak_fence"}));
       this.variantNames.put(Items.OAK_DOOR, Lists.newArrayList(new String[]{"oak_door"}));
       this.variantNames.put(Items.BOAT, Lists.newArrayList(new String[]{"oak_boat"}));
       this.variantNames.put(Items.TOTEM_OF_UNDYING, Lists.newArrayList(new String[]{"totem"}));
+
+      for (Entry var2 : customVariantNames.entrySet()) {
+         this.variantNames.put((Item)((IRegistryDelegate)var2.getKey()).get(), Lists.newArrayList(((Set)var2.getValue()).iterator()));
+      }
    }

    private List<String> getVariantNames(Item var1) {
       List var2 = this.variantNames.get(var1);
       if (var2 == null) {
-         var2 = Collections.singletonList(Item.REGISTRY.getNameForObject(var1).toString());
+         var2 = Collections.singletonList(((ResourceLocation)Item.REGISTRY.getNameForObject(var1)).toString());
       }

       return var2;
    }

    private ResourceLocation getItemLocation(String var1) {
       ResourceLocation var2 = new ResourceLocation(var1);
+      if (Reflector.ForgeHooksClient.exists()) {
+         var2 = new ResourceLocation(var1.replaceAll("#.*", ""));
+      }
+
       return new ResourceLocation(var2.getNamespace(), "item/" + var2.getPath());
    }

    private void bakeBlockModels() {
       for (ModelResourceLocation var2 : this.variants.keySet()) {
          IBakedModel var3 = this.createRandomModelForVariantList(this.variants.get(var2), var2.toString());
@@ -669,14 +717,14 @@
          }
       }

       for (Entry var11 : this.multipartVariantMap.entrySet()) {
          ModelBlockDefinition var12 = (ModelBlockDefinition)var11.getKey();
          Multipart var4 = var12.getMultipartData();
-         String var5 = Block.REGISTRY.getNameForObject(var4.getStateContainer().getBlock()).toString();
-         MultipartBakedModel.Builder var6 = new MultipartBakedModel.Builder();
+         String var5 = ((ResourceLocation)Block.REGISTRY.getNameForObject(var4.getStateContainer().getBlock())).toString();
+         Builder var6 = new Builder();

          for (Selector var8 : var4.getSelectors()) {
             IBakedModel var9 = this.createRandomModelForVariantList(var8.getVariantList(), "selector of " + var5);
             if (var9 != null) {
                var6.putModel(var8.getPredicate(var4.getStateContainer()), var9);
             }
@@ -694,13 +742,13 @@

    @Nullable
    private IBakedModel createRandomModelForVariantList(VariantList var1, String var2) {
       if (var1.getVariantList().isEmpty()) {
          return null;
       } else {
-         WeightedBakedModel.Builder var3 = new WeightedBakedModel.Builder();
+         net.minecraft.client.renderer.block.model.WeightedBakedModel.Builder var3 = new net.minecraft.client.renderer.block.model.WeightedBakedModel.Builder();
          int var4 = 0;

          for (Variant var6 : var1.getVariantList()) {
             ModelBlock var7 = this.models.get(var6.getModelLocation());
             if (var7 == null || !var7.isResolved()) {
                LOGGER.warn("Missing model for: {}", var2);
@@ -729,12 +777,16 @@
    }

    private void bakeItemModels() {
       for (Entry var2 : this.itemLocations.entrySet()) {
          ResourceLocation var3 = (ResourceLocation)var2.getValue();
          ModelResourceLocation var4 = new ModelResourceLocation((String)var2.getKey(), "inventory");
+         if (Reflector.ForgeHooksClient.exists()) {
+            var4 = (ModelResourceLocation)Reflector.call(Reflector.ModelLoader_getInventoryVariant, new Object[]{var2.getKey()});
+         }
+
          ModelBlock var5 = this.models.get(var3);
          if (var5 == null || !var5.isResolved()) {
             LOGGER.warn("Missing model for: {}", var3);
          } else if (var5.getElements().isEmpty()) {
             LOGGER.warn("Missing elements for: {}", var3);
          } else if (this.isCustomRenderer(var5)) {
@@ -785,35 +837,53 @@

       var1.addAll(LOCATIONS_BUILTIN_TEXTURES);
       return var1;
    }

    @Nullable
-   private IBakedModel bakeModel(ModelBlock var1, ModelRotation var2, boolean var3) {
+   public IBakedModel bakeModel(ModelBlock var1, ModelRotation var2, boolean var3) {
+      return this.bakeModel(var1, (ITransformation)var2, var3);
+   }
+
+   protected IBakedModel bakeModel(ModelBlock var1, ITransformation var2, boolean var3) {
       TextureAtlasSprite var4 = this.sprites.get(new ResourceLocation(var1.resolveTextureName("particle")));
-      SimpleBakedModel.Builder var5 = new SimpleBakedModel.Builder(var1, var1.createOverrides()).setTexture(var4);
+      net.minecraft.client.renderer.block.model.SimpleBakedModel.Builder var5 = new net.minecraft.client.renderer.block.model.SimpleBakedModel.Builder(
+            var1, var1.createOverrides()
+         )
+         .setTexture(var4);
       if (var1.getElements().isEmpty()) {
          return null;
       } else {
          for (BlockPart var7 : var1.getElements()) {
             for (EnumFacing var9 : var7.mapFaces.keySet()) {
-               BlockPartFace var10 = var7.mapFaces.get(var9);
+               BlockPartFace var10 = (BlockPartFace)var7.mapFaces.get(var9);
                TextureAtlasSprite var11 = this.sprites.get(new ResourceLocation(var1.resolveTextureName(var10.texture)));
-               if (var10.cullFace == null) {
-                  var5.addGeneralQuad(this.makeBakedQuad(var7, var10, var11, var9, var2, var3));
+               boolean var12 = true;
+               if (Reflector.ForgeHooksClient.exists()) {
+                  var12 = TRSRTransformation.isInteger(var2.getMatrix());
+               }
+
+               if (var10.cullFace != null && var12) {
+                  var5.addFaceQuad(var2.rotate(var10.cullFace), this.makeBakedQuad(var7, var10, var11, var9, var2, var3));
                } else {
-                  var5.addFaceQuad(var2.rotateFace(var10.cullFace), this.makeBakedQuad(var7, var10, var11, var9, var2, var3));
+                  var5.addGeneralQuad(this.makeBakedQuad(var7, var10, var11, var9, var2, var3));
                }
             }
          }

          return var5.makeBakedModel();
       }
    }

    private BakedQuad makeBakedQuad(BlockPart var1, BlockPartFace var2, TextureAtlasSprite var3, EnumFacing var4, ModelRotation var5, boolean var6) {
+      return Reflector.ForgeHooksClient.exists()
+         ? this.makeBakedQuad(var1, var2, var3, var4, (ITransformation)var5, var6)
+         : this.faceBakery.makeBakedQuad(var1.positionFrom, var1.positionTo, var2, var3, var4, var5, var1.partRotation, var6, var1.shade);
+   }
+
+   protected BakedQuad makeBakedQuad(BlockPart var1, BlockPartFace var2, TextureAtlasSprite var3, EnumFacing var4, ITransformation var5, boolean var6) {
       return this.faceBakery.makeBakedQuad(var1.positionFrom, var1.positionTo, var2, var3, var4, var5, var1.partRotation, var6, var1.shade);
    }

    private void loadModelsCheck() {
       this.loadModels();

@@ -842,13 +912,13 @@
             }

             ModelBlock var7 = this.loadModel(var6);
             this.models.put(var6, var7);
             this.addModelParentLocation(var1, var2, var7);
          } catch (Exception var5) {
-            LOGGER.warn("In parent chain: {}; unable to load model: '{}'", JOINER.join(this.getParentPath(var6)), var6, var5);
+            LOGGER.warn("In parent chain: {}; unable to load model: '{}'", new Object[]{JOINER.join(this.getParentPath(var6)), var6});
          }

          var2.add(var6);
       }
    }

@@ -971,12 +1041,72 @@
          }
       }
    }

    private ModelBlock makeItemModel(ModelBlock var1) {
       return this.itemModelGenerator.makeItemModel(this.textureMap, var1);
+   }
+
+   public ModelBlock getModelBlock(ResourceLocation var1) {
+      return this.models.get(var1);
+   }
+
+   public static void fixModelLocations(ModelBlock var0, String var1) {
+      ResourceLocation var2 = fixModelLocation(var0.parentLocation, var1);
+      if (var2 != var0.parentLocation) {
+         var0.parentLocation = var2;
+      }
+
+      if (var0.textures != null) {
+         for (Entry var4 : var0.textures.entrySet()) {
+            String var5 = (String)var4.getValue();
+            String var6 = fixResourcePath(var5, var1);
+            if (var6 != var5) {
+               var4.setValue(var6);
+            }
+         }
+      }
+   }
+
+   public static ResourceLocation fixModelLocation(ResourceLocation var0, String var1) {
+      if (var0 != null && var1 != null) {
+         if (!var0.getNamespace().equals("minecraft")) {
+            return var0;
+         } else {
+            String var2 = var0.getPath();
+            String var3 = fixResourcePath(var2, var1);
+            if (var3 != var2) {
+               var0 = new ResourceLocation(var0.getNamespace(), var3);
+            }
+
+            return var0;
+         }
+      } else {
+         return var0;
+      }
+   }
+
+   private static String fixResourcePath(String var0, String var1) {
+      var0 = TextureUtils.fixResourcePath(var0, var1);
+      var0 = StrUtils.removeSuffix(var0, ".json");
+      return StrUtils.removeSuffix(var0, ".png");
+   }
+
+   protected void registerMultipartVariant(ModelBlockDefinition var1, Collection<ModelResourceLocation> var2) {
+      this.multipartVariantMap.put(var1, var2);
+   }
+
+   public static void registerItemVariants(Item var0, ResourceLocation... var1) {
+      IRegistryDelegate var2 = (IRegistryDelegate)Reflector.getFieldValue(var0, Reflector.ForgeItem_delegate);
+      if (!customVariantNames.containsKey(var2)) {
+         customVariantNames.put(var2, Sets.newHashSet());
+      }
+
+      for (ResourceLocation var6 : var1) {
+         customVariantNames.get(var2).add(var6.toString());
+      }
    }

    static {
       BUILT_IN_MODELS.put("missing", MISSING_MODEL_MESH);
       MODEL_GENERATED.name = "generation marker";
       MODEL_ENTITY.name = "block entity marker";
 */
