package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.optifine.BetterGrass;
import net.optifine.ConnectedTextures;
import net.optifine.CustomItems;
import net.optifine.EmissiveTextures;
import net.optifine.SmartAnimations;
import net.optifine.SpriteDependencies;
import net.optifine.shaders.ShadersTex;
import net.optifine.util.CounterInt;
import net.optifine.util.TextureUtils;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * OptiFine additions to {@link TextureMap}.
 *
 * <p>Implements the OptiFine-specific sprite map logic: custom icon grid, mipmap detection,
 * sprite dependencies, texture scaling, multi-texture single sprites, shaders atlas upload,
 * SmartAnimations, emissive textures and the rewritten {@code loadTextureAtlas}/
 * {@code updateAnimations} loops.</p>
 *
 * <p>The constructor re-arrangement (skipFirst variants), {@code ForgeHooksClient.onTextureStitchedPre/Post}
 * calls, the progress-bar based loading and {@code getTextureExtry}/{@code setTextureEntry}/
 * {@code getBasePath}/{@code getMipmapLevels} are already provided by the Cleanroom patches and
 * thus skipped here.</p>
 */
@Mixin(TextureMap.class)
public abstract class MixinTextureMap implements ITickableTextureObject {

    // ===== new fields =====

    @Shadow
    private List<TextureAtlasSprite> listAnimatedSprites;

    @Shadow
    public TextureAtlasSprite registerSprite(ResourceLocation location) {
        throw new AbstractMethodError();
    }

    @Shadow
    private ResourceLocation getResourceLocation(TextureAtlasSprite sprite) {
        throw new AbstractMethodError();
    }

    @Shadow
    private boolean generateMipmaps(IResourceManager resourceManager, TextureAtlasSprite sprite) {
        throw new AbstractMethodError();
    }
    @Shadow
    private Map<String, TextureAtlasSprite> mapRegisteredSprites;
    @Shadow
    private Map<String, TextureAtlasSprite> mapUploadedSprites;
    @Shadow
    private String basePath;
    @Shadow
    private ITextureMapPopulator iconCreator;
    @Shadow
    private int mipmapLevels;
    @Shadow
    private TextureAtlasSprite missingImage;

    @Unique
    private TextureAtlasSprite[] iconGrid = null;
    @Unique
    private int iconGridSize = -1;
    @Unique
    private int iconGridCountX = -1;
    @Unique
    private int iconGridCountY = -1;
    @Unique
    private double iconGridSizeU = -1.0;
    @Unique
    private double iconGridSizeV = -1.0;
    @Unique
    private CounterInt counterIndexInMap = new CounterInt(0);
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public int atlasWidth = 0;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public int atlasHeight = 0;
    // ===== cross-class private access =====

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite setIndexInMap (I)V")
    private static native void TextureAtlasSprite_setIndexInMap(TextureAtlasSprite sprite, int indexInMap);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite deleteSpriteTexture ()V")
    private static native void TextureAtlasSprite_deleteSpriteTexture(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite updateIndexInMap (Lnet/optifine/util/CounterInt;)V")
    private static native void TextureAtlasSprite_updateIndexInMap(TextureAtlasSprite sprite, net.optifine.util.CounterInt counterInt);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite setAnimationIndex (I)V")
    private static native void TextureAtlasSprite_setAnimationIndex(TextureAtlasSprite sprite, int animationIndex);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getAnimationIndex ()I")
    private static native int TextureAtlasSprite_getAnimationIndex(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite bindSpriteTexture ()V")
    private static native void TextureAtlasSprite_bindSpriteTexture(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite isAnimationActive ()Z")
    private static native boolean TextureAtlasSprite_isAnimationActive(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getFramesTextureData ()Ljava/util/List;")
    private static native java.util.List<int[][]> TextureAtlasSprite_getFramesTextureData(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getAnimationMetadata ()Lnet/minecraft/client/resources/data/AnimationMetadataSection;")
    private static native net.minecraft.client.resources.data.AnimationMetadataSection TextureAtlasSprite_getAnimationMetadata(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite setAnimationMetadata (Lnet/minecraft/client/resources/data/AnimationMetadataSection;)V")
    private static native void TextureAtlasSprite_setAnimationMetadata(TextureAtlasSprite sprite, net.minecraft.client.resources.data.AnimationMetadataSection animationMetadata);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteSingle Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    private static native TextureAtlasSprite TextureAtlasSprite_spriteSingle_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteNormal Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    private static native TextureAtlasSprite TextureAtlasSprite_spriteNormal_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteSpecular Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    private static native TextureAtlasSprite TextureAtlasSprite_spriteSpecular_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetWidth I")
    private static native void TextureAtlasSprite_sheetWidth_set(TextureAtlasSprite sprite, int value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetHeight I")
    private static native void TextureAtlasSprite_sheetHeight_set(TextureAtlasSprite sprite, int value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite mipmapLevels I")
    private static native void TextureAtlasSprite_mipmapLevels_set(TextureAtlasSprite sprite, int value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite isEmissive Z")
    private static native void TextureAtlasSprite_isEmissive_set(TextureAtlasSprite sprite, boolean value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteEmissive Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    private static native void TextureAtlasSprite_spriteEmissive_set(TextureAtlasSprite sprite, TextureAtlasSprite value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap generateMipmaps (Lnet/minecraft/client/resources/IResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
    private static native void TextureMap_generateMipmaps(net.minecraft.client.renderer.texture.TextureMap textureMap, IResourceManager resourceManager, TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap isAbsoluteLocation (Lnet/minecraft/util/ResourceLocation;)Z")
    private static native boolean TextureMap_isAbsoluteLocation(net.minecraft.client.renderer.texture.TextureMap textureMap, ResourceLocation location);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getMultiTexID ()Lnet/optifine/shaders/MultiTexID;")
    private static native net.optifine.shaders.MultiTexID TextureMap_getMultiTexID(net.minecraft.client.renderer.texture.TextureMap textureMap);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager getBoundTexture ()I")
    private static native int GlStateManager_getBoundTexture();

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.texture.TextureUtil bindTexture (I)V")
    private static native void TextureUtil_bindTexture(int texture);

    @Unique
    private int countAnimationsActive;
    @Unique
    private int frameCountAnimations;

    // ===== initMissingImage: dynamic size + index =====

    @Inject(method = "initMissingImage", at = @At("HEAD"), cancellable = true)
    private void optiRefine$initMissingImage(CallbackInfo ci) {
        int i = this.getMinSpriteSize();
        int[] aint = this.getMissingImageData(i);
        this.missingImage.setIconWidth(i);
        this.missingImage.setIconHeight(i);
        int[][] aint1 = new int[this.mipmapLevels + 1][];
        aint1[0] = aint;
        this.missingImage.setFramesTextureData(java.util.Collections.singletonList(aint1));
        TextureAtlasSprite_setIndexInMap(this.missingImage, this.counterIndexInMap.nextValue());
        ci.cancel();
    }

    // ===== loadSprites: reset index + max mipmap detection =====

    @Inject(method = "loadSprites", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/ITextureMapPopulator;registerSprites(Lnet/minecraft/client/renderer/texture/TextureMap;)V", shift = At.Shift.BEFORE))

    private void optiRefine$loadSpritesReset(IResourceManager resourceManager, ITextureMapPopulator iconCreatorIn, CallbackInfo ci) {

        this.counterIndexInMap.reset();

    }



    @Inject(method = "loadSprites", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/ITextureMapPopulator;registerSprites(Lnet/minecraft/client/renderer/texture/TextureMap;)V", shift = At.Shift.AFTER))

    private void optiRefine$loadSprites(IResourceManager resourceManager, ITextureMapPopulator iconCreatorIn, CallbackInfo ci) {

        if (this.mipmapLevels >= 4) {

            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);

            Config.log("Mipmap levels: " + this.mipmapLevels);

        }

    }

    // ===== loadTextureAtlas: full OptiFine rewrite =====

    /**
     * @author OptiRefine
     * @reason OptiFine: connected textures, custom items, sprite dependencies, scaling, shaders, multi-texture
     */
    @WrapMethod(method = "loadTextureAtlas")
    private void optiRefine$loadTextureAtlas(IResourceManager resourceManager, Operation<Void> original) {
        Config.dbg("Multitexture: " + Config.isMultiTexture());
        if (Config.isMultiTexture()) {
            for (TextureAtlasSprite sprite : this.mapUploadedSprites.values()) {
                TextureAtlasSprite_deleteSpriteTexture(sprite);
            }
        }
        ConnectedTextures.updateIcons((TextureMap) (Object) this);
        CustomItems.updateIcons((TextureMap) (Object) this);
        BetterGrass.updateIcons((TextureMap) (Object) this);
        int i = TextureUtils.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(i, i, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int j = Integer.MAX_VALUE;
        int k = this.getMinSpriteSize();
        this.iconGridSize = k;
        int l = 1 << this.mipmapLevels;
        int i1 = 0;
        int j1 = 0;
        SpriteDependencies.reset();
        ArrayList<TextureAtlasSprite> arraylist = new ArrayList<>(this.mapRegisteredSprites.values());

        for (int k1 = 0; k1 < arraylist.size(); ++k1) {
            TextureAtlasSprite textureatlassprite = SpriteDependencies.resolveDependencies(arraylist, k1, (TextureMap) (Object) this);
            ResourceLocation resourcelocation = this.getResourceLocation(textureatlassprite);
            IResource iresource = null;
            TextureAtlasSprite_updateIndexInMap(textureatlassprite, this.counterIndexInMap);
            if (textureatlassprite.hasCustomLoader(resourceManager, resourcelocation)) {
                if (textureatlassprite.load(resourceManager, resourcelocation, loc -> this.mapRegisteredSprites.get(loc.toString()))) {
                    Config.detail("Custom loader (skipped): " + textureatlassprite);
                    ++j1;
                    continue;
                }
                Config.detail("Custom loader: " + textureatlassprite);
                ++i1;
            } else {
                try {
                    net.minecraft.client.renderer.texture.PngSizeInfo pngsizeinfo = net.minecraft.client.renderer.texture.PngSizeInfo.makeFromResource(resourceManager.getResource(resourcelocation));
                    iresource = resourceManager.getResource(resourcelocation);
                    boolean flag = iresource.getMetadata("animation") != null;
                    textureatlassprite.loadSprite(pngsizeinfo, flag);
                } catch (RuntimeException runtimeexception) {
                    org.apache.logging.log4j.LogManager.getLogger().error("Unable to parse metadata from {}", resourcelocation, runtimeexception);
                    continue;
                } catch (IOException ioexception) {
                    org.apache.logging.log4j.LogManager.getLogger().error("Using missing texture, unable to load " + resourcelocation + ", " + ioexception.getClass().getName());
                    continue;
                } finally {
                    IOUtils.closeQuietly(iresource);
                }
            }

            int l1 = textureatlassprite.getIconWidth();
            int i2 = textureatlassprite.getIconHeight();
            if (l1 >= 1 && i2 >= 1) {
                if (l1 < k || this.mipmapLevels > 0) {
                    int j2 = this.mipmapLevels > 0 ? TextureUtils.scaleToGrid(l1, k) : TextureUtils.scaleToMin(l1, k);
                    if (j2 != l1) {
                        if (!TextureUtils.isPowerOfTwo(l1)) {
                            Config.log("Scaled non power of 2: " + textureatlassprite.getIconName() + ", " + l1 + " -> " + j2);
                        } else {
                            Config.log("Scaled too small texture: " + textureatlassprite.getIconName() + ", " + l1 + " -> " + j2);
                        }
                        int k2 = i2 * j2 / l1;
                        textureatlassprite.setIconWidth(j2);
                        textureatlassprite.setIconHeight(k2);
                    }
                }
                j = Math.min(j, Math.min(textureatlassprite.getIconWidth(), textureatlassprite.getIconHeight()));
                int l2 = Math.min(Integer.lowestOneBit(textureatlassprite.getIconWidth()), Integer.lowestOneBit(textureatlassprite.getIconHeight()));
                if (l2 < l) {
                    org.apache.logging.log4j.LogManager.getLogger().warn("Texture {} with size {}x{} limits mip level from {} to {}", resourcelocation, textureatlassprite.getIconWidth(), textureatlassprite.getIconHeight(), MathHelper.log2(l), MathHelper.log2(l2));
                    l = l2;
                }
                if (this.generateMipmaps(resourceManager, textureatlassprite)) {
                    stitcher.addSprite(textureatlassprite);
                }
            } else {
                Config.warn("Invalid sprite size: " + textureatlassprite);
            }
        }

        if (i1 > 0) {
            Config.dbg("Custom loader sprites: " + i1);
        }
        if (j1 > 0) {
            Config.dbg("Custom loader sprites (skipped): " + j1);
        }
        if (SpriteDependencies.getCountDependencies() > 0) {
            Config.dbg("Sprite dependencies: " + SpriteDependencies.getCountDependencies());
        }

        int i3 = Math.min(j, l);
        int j3 = MathHelper.log2(i3);
        if (j3 < 0) {
            j3 = 0;
        }
        if (j3 < this.mipmapLevels) {
            org.apache.logging.log4j.LogManager.getLogger().warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, j3, i3);
            this.mipmapLevels = j3;
        }

        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);

        try {
            stitcher.doStitch();
        } catch (StitcherException stitcherexception) {
            throw stitcherexception;
        }

        org.apache.logging.log4j.LogManager.getLogger().info("Created: {}x{} {}-atlas", stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath);
        if (Config.isShaders()) {
            ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), stitcher, (TextureMap) (Object) this);
        } else {
            TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }

        HashMap<String, TextureAtlasSprite> hashmap = new HashMap<>(this.mapRegisteredSprites);

        for (TextureAtlasSprite textureatlassprite1 : stitcher.getStichSlots()) {
            String s = textureatlassprite1.getIconName();
            hashmap.remove(s);
            this.mapUploadedSprites.put(s, textureatlassprite1);

            try {
                if (Config.isShaders()) {
                    ShadersTex.uploadTexSubForLoadAtlas((TextureMap) (Object) this, textureatlassprite1.getIconName(), textureatlassprite1.getFrameTextureData(0), textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight(), textureatlassprite1.getOriginX(), textureatlassprite1.getOriginY(), false, false);
                } else {
                    TextureUtil.uploadTextureMipmap(textureatlassprite1.getFrameTextureData(0), textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight(), textureatlassprite1.getOriginX(), textureatlassprite1.getOriginY(), false, false);
                }
            } catch (Throwable throwable1) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Stitching texture atlas");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Texture being stitched together");
                crashreportcategory.addCrashSection("Atlas path", this.basePath);
                crashreportcategory.addCrashSection("Sprite", textureatlassprite1);
                throw new ReportedException(crashreport);
            }

            if (textureatlassprite1.hasAnimationMetadata()) {
                TextureAtlasSprite_setAnimationIndex(textureatlassprite1, this.listAnimatedSprites.size());
                this.listAnimatedSprites.add(textureatlassprite1);
            }
        }

        for (TextureAtlasSprite textureatlassprite2 : hashmap.values()) {
            textureatlassprite2.copyFrom(this.missingImage);
        }

        Config.log("Animated sprites: " + this.listAnimatedSprites.size());
        if (Config.isMultiTexture()) {
            int k3 = stitcher.getCurrentWidth();
            int l3 = stitcher.getCurrentHeight();

            for (TextureAtlasSprite textureatlassprite3 : stitcher.getStichSlots()) {
                TextureAtlasSprite_sheetWidth_set(textureatlassprite3, k3);
                TextureAtlasSprite_sheetHeight_set(textureatlassprite3, l3);
                TextureAtlasSprite_mipmapLevels_set(textureatlassprite3, this.mipmapLevels);
                TextureAtlasSprite textureatlassprite4 = TextureAtlasSprite_spriteSingle_get(textureatlassprite3);
                if (textureatlassprite4 != null) {
                    if (textureatlassprite4.getIconWidth() <= 0) {
                        textureatlassprite4.setIconWidth(textureatlassprite3.getIconWidth());
                        textureatlassprite4.setIconHeight(textureatlassprite3.getIconHeight());
                        textureatlassprite4.initSprite(textureatlassprite3.getIconWidth(), textureatlassprite3.getIconHeight(), 0, 0, false);
                        textureatlassprite4.clearFramesTextureData();
                        List<int[][]> list = TextureAtlasSprite_getFramesTextureData(textureatlassprite3);
                        textureatlassprite4.setFramesTextureData(list);
                        TextureAtlasSprite_setAnimationMetadata(textureatlassprite4, TextureAtlasSprite_getAnimationMetadata(textureatlassprite3));
                    }
                    TextureAtlasSprite_sheetWidth_set(textureatlassprite4, k3);
                    TextureAtlasSprite_sheetHeight_set(textureatlassprite4, l3);
                    TextureAtlasSprite_mipmapLevels_set(textureatlassprite4, this.mipmapLevels);
                    TextureAtlasSprite_setAnimationIndex(textureatlassprite4, TextureAtlasSprite_getAnimationIndex(textureatlassprite3));
                    TextureAtlasSprite_bindSpriteTexture(textureatlassprite3);
                    boolean flag1 = false;
                    boolean flag2 = true;

                    try {
                        TextureUtil.uploadTextureMipmap(textureatlassprite4.getFrameTextureData(0), textureatlassprite4.getIconWidth(), textureatlassprite4.getIconHeight(), textureatlassprite4.getOriginX(), textureatlassprite4.getOriginY(), flag1, flag2);
                    } catch (Exception exception) {
                        Config.dbg("Error uploading sprite single: " + textureatlassprite4 + ", parent: " + textureatlassprite3);
                        exception.printStackTrace();
                    }
                }
            }

            net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        }

        ForgeHooksClient.onTextureStitchedPost((TextureMap) (Object) this);
        this.updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
            Config.dbg("Exporting texture map: " + this.basePath);
            TextureUtils.saveGlTexture("debug/" + this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
    }

    // ===== generateMipmaps: custom loader support =====

    /**
     * @author OptiRefine
     * @reason OptiFine: custom loader mipmap support
     */
    @WrapMethod(method = "generateMipmaps")
    private boolean optiRefine$generateMipmaps(IResourceManager resourceManager, TextureAtlasSprite sprite, Operation<Boolean> original) {
        ResourceLocation resourcelocation = this.getResourceLocation(sprite);
        IResource iresource = null;
        if (sprite.hasCustomLoader(resourceManager, resourcelocation)) {
            TextureUtils.generateCustomMipmaps(sprite, this.mipmapLevels);
            return true;
        } else {
            boolean flag;
            label58:
            {
                try {
                    iresource = resourceManager.getResource(resourcelocation);
                    sprite.loadSpriteFrames(iresource, this.mipmapLevels + 1);
                    break label58;
                } catch (RuntimeException runtimeexception) {
                    org.apache.logging.log4j.LogManager.getLogger().error("Unable to parse metadata from {}", resourcelocation, runtimeexception);
                    return false;
                } catch (IOException ioexception) {
                    org.apache.logging.log4j.LogManager.getLogger().error("Using missing texture, unable to load {}", resourcelocation, ioexception);
                    boolean flag1 = false;
                    flag = flag1;
                } finally {
                    IOUtils.closeQuietly(iresource);
                }
                return flag;
            }

            try {
                sprite.generateMipmaps(this.mipmapLevels);
                return true;
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Applying mipmap");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
                crashreportcategory.addDetail("Sprite name", (ICrashReportDetail<String>) sprite::getIconName);
                crashreportcategory.addDetail("Sprite size", (ICrashReportDetail<String>) () -> sprite.getIconWidth() + " x " + sprite.getIconHeight());
                crashreportcategory.addDetail("Sprite frames", (ICrashReportDetail<String>) () -> sprite.getFrameCount() + " frames");
                crashreportcategory.addCrashSection("Mipmap levels", this.mipmapLevels);
                throw new ReportedException(crashreport);
            }
        }
    }

    // ===== resource location helpers =====

    @SuppressWarnings("unused")
    @mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer(name = "getResourceLocation", access = org.objectweb.asm.Opcodes.ACC_PUBLIC)
    private ResourceLocation acc_getResourceLocation(TextureAtlasSprite sprite) {
        return null;
    }

    /**
     * @author OptiRefine
     * @reason OptiFine: complete resource location with base path
     */
    @WrapMethod(method = "getResourceLocation")
    private ResourceLocation optiRefine$getResourceLocation(TextureAtlasSprite sprite, Operation<ResourceLocation> original) {
        ResourceLocation resourcelocation = new ResourceLocation(sprite.getIconName());
        return this.completeResourceLocation(resourcelocation);
    }

    @Unique @Public
    public ResourceLocation completeResourceLocation(ResourceLocation location) {
        return TextureMap_isAbsoluteLocation((net.minecraft.client.renderer.texture.TextureMap)(Object) this, location)
                ? new ResourceLocation(location.getNamespace(), location.getPath() + ".png")
                : new ResourceLocation(location.getNamespace(), String.format("%s/%s%s", this.basePath, location.getPath(), ".png"));
    }

    // ===== updateAnimations: full OptiFine rewrite =====

    /**
     * @author OptiRefine
     * @reason OptiFine: terrain animation toggles, normal/specular/single sprites, SmartAnimations
     */
    @WrapMethod(method = "updateAnimations")
    private void optiRefine$updateAnimations(Operation<Void> original) {
        boolean flag = false;
        boolean flag1 = false;
        TextureUtil_bindTexture(this.getGlTextureId());
        int i = 0;

        for (TextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
            if (this.isTerrainAnimationActive(textureatlassprite)) {
                textureatlassprite.updateAnimation();
                if (TextureAtlasSprite_isAnimationActive(textureatlassprite)) {
                    ++i;
                }
                if (TextureAtlasSprite_spriteNormal_get(textureatlassprite) != null) {
                    flag = true;
                }
                if (TextureAtlasSprite_spriteSpecular_get(textureatlassprite) != null) {
                    flag1 = true;
                }
            }
        }

        if (Config.isMultiTexture()) {
            for (TextureAtlasSprite textureatlassprite1 : this.listAnimatedSprites) {
                if (this.isTerrainAnimationActive(textureatlassprite1)) {
                    TextureAtlasSprite textureatlassprite2 = TextureAtlasSprite_spriteSingle_get(textureatlassprite1);
                    if (textureatlassprite2 != null) {
                        if (textureatlassprite1 == TextureUtils.iconClock || textureatlassprite1 == TextureUtils.iconCompass) {
                            textureatlassprite2.frameCounter = textureatlassprite1.frameCounter;
                        }
                        TextureAtlasSprite_bindSpriteTexture(textureatlassprite1);
                        textureatlassprite2.updateAnimation();
                        if (TextureAtlasSprite_isAnimationActive(textureatlassprite2)) {
                            ++i;
                        }
                    }
                }
            }
            TextureUtil_bindTexture(this.getGlTextureId());
        }

        if (Config.isShaders()) {
            if (flag) {
                TextureUtil_bindTexture(TextureMap_getMultiTexID((net.minecraft.client.renderer.texture.TextureMap)(Object) this).norm);
                for (TextureAtlasSprite textureatlassprite3 : this.listAnimatedSprites) {
                    if (TextureAtlasSprite_spriteNormal_get(textureatlassprite3) != null && this.isTerrainAnimationActive(textureatlassprite3)) {
                        if (textureatlassprite3 == TextureUtils.iconClock || textureatlassprite3 == TextureUtils.iconCompass) {
                            TextureAtlasSprite_spriteNormal_get(textureatlassprite3).frameCounter = textureatlassprite3.frameCounter;
                        }
                        TextureAtlasSprite_spriteNormal_get(textureatlassprite3).updateAnimation();
                        if (TextureAtlasSprite_isAnimationActive(TextureAtlasSprite_spriteNormal_get(textureatlassprite3))) {
                            ++i;
                        }
                    }
                }
            }
            if (flag1) {
                TextureUtil_bindTexture(TextureMap_getMultiTexID((net.minecraft.client.renderer.texture.TextureMap)(Object) this).spec);
                for (TextureAtlasSprite textureatlassprite4 : this.listAnimatedSprites) {
                    if (TextureAtlasSprite_spriteSpecular_get(textureatlassprite4) != null && this.isTerrainAnimationActive(textureatlassprite4)) {
                        if (textureatlassprite4 == TextureUtils.iconClock || textureatlassprite4 == TextureUtils.iconCompass) {
                            TextureAtlasSprite_spriteSpecular_get(textureatlassprite4).frameCounter = textureatlassprite4.frameCounter;
                        }
                        TextureAtlasSprite_spriteSpecular_get(textureatlassprite4).updateAnimation();
                        if (TextureAtlasSprite_isAnimationActive(TextureAtlasSprite_spriteSpecular_get(textureatlassprite4))) {
                            ++i;
                        }
                    }
                }
            }
            if (flag || flag1) {
                TextureUtil_bindTexture(this.getGlTextureId());
            }
        }

        int j = Config.getMinecraft().entityRenderer.frameCount;
        if (j != this.frameCountAnimations) {
            this.countAnimationsActive = i;
            this.frameCountAnimations = j;
        }
        if (SmartAnimations.isActive()) {
            SmartAnimations.resetSpritesRendered();
        }
    }

    // ===== registerSprite: index + emissive =====

    @Inject(method = "registerSprite", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;makeAtlasSprite(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", shift = At.Shift.AFTER))
    private void optiRefine$registerSprite(ResourceLocation location, CallbackInfo ci) {
        TextureAtlasSprite textureatlassprite = this.mapRegisteredSprites.get(location.toString());
        if (textureatlassprite != null) {
            TextureAtlasSprite_updateIndexInMap(textureatlassprite, this.counterIndexInMap);
            if (Config.isEmissiveTextures()) {
                this.checkEmissive(location, textureatlassprite);
            }
        }
    }

    // ===== new methods =====

    @Unique @Public
    public TextureAtlasSprite getSpriteSafe(String name) {
        ResourceLocation resourcelocation = new ResourceLocation(name);
        return this.mapRegisteredSprites.get(resourcelocation.toString());
    }

    @Unique @Public
    public TextureAtlasSprite getRegisteredSprite(ResourceLocation location) {
        return this.mapRegisteredSprites.get(location.toString());
    }

    @Unique
    private boolean isTerrainAnimationActive(TextureAtlasSprite sprite) {
        if (sprite == TextureUtils.iconWaterStill || sprite == TextureUtils.iconWaterFlow) {
            return Config.isAnimatedWater();
        } else if (sprite == TextureUtils.iconLavaStill || sprite == TextureUtils.iconLavaFlow) {
            return Config.isAnimatedLava();
        } else if (sprite == TextureUtils.iconFireLayer0 || sprite == TextureUtils.iconFireLayer1) {
            return Config.isAnimatedFire();
        } else if (sprite == TextureUtils.iconPortal) {
            return Config.isAnimatedPortal();
        } else {
            return sprite != TextureUtils.iconClock && sprite != TextureUtils.iconCompass ? Config.isAnimatedTerrain() : true;
        }
    }

    @Unique @Public
    public int getCountRegisteredSprites() {
        return this.counterIndexInMap.getValue();
    }

    @Unique
    private int detectMaxMipmapLevel(Map<String, TextureAtlasSprite> map, IResourceManager resourceManager) {
        int i = this.detectMinimumSpriteSize(map, resourceManager, 20);
        if (i < 16) {
            i = 16;
        }
        i = MathHelper.smallestEncompassingPowerOfTwo(i);
        if (i > 16) {
            Config.log("Sprite size: " + i);
        }
        int j = MathHelper.log2(i);
        if (j < 4) {
            j = 4;
        }
        return j;
    }

    @Unique
    private int detectMinimumSpriteSize(Map<String, TextureAtlasSprite> map, IResourceManager resourceManager, int maxCount) {
        HashMap<Integer, Integer> hashmap = new HashMap<>();

        for (Map.Entry<String, TextureAtlasSprite> entry : map.entrySet()) {
            TextureAtlasSprite textureatlassprite = entry.getValue();
            ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
            ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation);
            if (!textureatlassprite.hasCustomLoader(resourceManager, resourcelocation)) {
                try {
                    IResource iresource = resourceManager.getResource(resourcelocation1);
                    if (iresource != null) {
                        InputStream inputstream = iresource.getInputStream();
                        if (inputstream != null) {
                            Dimension dimension = TextureUtils.getImageSize(inputstream, "png");
                            inputstream.close();
                            if (dimension != null) {
                                int k = dimension.width;
                                int l = MathHelper.smallestEncompassingPowerOfTwo(k);
                                hashmap.merge(l, 1, Integer::sum);
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }

        int i1 = 0;
        Set<Integer> set = hashmap.keySet();
        TreeSet<Integer> treeset = new TreeSet<>(set);
        for (int j1 : treeset) {
            int k1 = hashmap.get(j1);
            i1 += k1;
        }
        int l1 = 16;
        int i2 = 0;
        int j2 = i1 * maxCount / 100;
        for (int k2 : treeset) {
            int l2 = hashmap.get(k2);
            i2 += l2;
            if (k2 > l1) {
                l1 = k2;
            }
            if (i2 > j2) {
                return l1;
            }
        }
        return l1;
    }

    @Unique
    private int getMinSpriteSize() {
        int i = 1 << this.mipmapLevels;
        if (i < 8) {
            i = 8;
        }
        return i;
    }

    @Unique
    private int[] getMissingImageData(int size) {
        BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
        BufferedImage bufferedimage1 = TextureUtils.scaleImage(bufferedimage, size);
        int[] aint = new int[size * size];
        bufferedimage1.getRGB(0, 0, size, size, aint, 0, size);
        return aint;
    }

    @Unique @Public
    public boolean isTextureBound() {
        int i = GlStateManager_getBoundTexture();
        int j = this.getGlTextureId();
        return i == j;
    }

    @Unique
    private void updateIconGrid(int width, int height) {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;
        if (this.iconGridSize > 0) {
            this.iconGridCountX = width / this.iconGridSize;
            this.iconGridCountY = height / this.iconGridSize;
            this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
            this.iconGridSizeU = 1.0 / this.iconGridCountX;
            this.iconGridSizeV = 1.0 / this.iconGridCountY;

            for (TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values()) {
                double d0 = 0.5 / width;
                double d1 = 0.5 / height;
                double d2 = Math.min(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) + d0;
                double d3 = Math.min(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) + d1;
                double d4 = Math.max(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) - d0;
                double d5 = Math.max(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) - d1;
                int i = (int) (d2 / this.iconGridSizeU);
                int j = (int) (d3 / this.iconGridSizeV);
                int k = (int) (d4 / this.iconGridSizeU);
                int l = (int) (d5 / this.iconGridSizeV);

                for (int i1 = i; i1 <= k; ++i1) {
                    if (i1 >= 0 && i1 < this.iconGridCountX) {
                        for (int j1 = j; j1 <= l; ++j1) {
                            if (j1 >= 0 && j1 < this.iconGridCountX) {
                                int k1 = j1 * this.iconGridCountX + i1;
                                this.iconGrid[k1] = textureatlassprite;
                            } else {
                                Config.warn("Invalid grid V: " + j1 + ", icon: " + textureatlassprite.getIconName());
                            }
                        }
                    } else {
                        Config.warn("Invalid grid U: " + i1 + ", icon: " + textureatlassprite.getIconName());
                    }
                }
            }
        }
    }

    @Unique @Public
    public TextureAtlasSprite getIconByUV(double u, double v) {
        if (this.iconGrid == null) {
            return null;
        } else {
            int i = (int) (u / this.iconGridSizeU);
            int j = (int) (v / this.iconGridSizeV);
            int k = j * this.iconGridCountX + i;
            return k >= 0 && k <= this.iconGrid.length ? this.iconGrid[k] : null;
        }
    }

    @Unique
    private void checkEmissive(ResourceLocation location, TextureAtlasSprite sprite) {
        String s = EmissiveTextures.getSuffixEmissive();
        if (s != null) {
            if (!location.getPath().endsWith(s)) {
                ResourceLocation resourcelocation = new ResourceLocation(location.getNamespace(), location.getPath() + s);
                ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation);
                if (Config.hasResource(resourcelocation1)) {
                    TextureAtlasSprite textureatlassprite = this.registerSprite(resourcelocation);
                    TextureAtlasSprite_isEmissive_set(textureatlassprite, true);
                    TextureAtlasSprite_spriteEmissive_set(sprite, textureatlassprite);
                }
            }
        }
    }

    @Unique @Public
    public int getCountAnimations() {
        return this.listAnimatedSprites.size();
    }

    @Unique @Public
    public int getCountAnimationsActive() {
        return this.countAnimationsActive;
    }

}
