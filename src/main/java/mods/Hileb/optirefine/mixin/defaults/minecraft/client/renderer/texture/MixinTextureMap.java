package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 3
public abstract class MixinTextureMap implements ITickableTextureObject {

    // ===== new fields =====

    @Shadow
    // [AUDIT-OK] baseline member listAnimatedSprites (field_94258_i) exists in TextureMap
    private List<TextureAtlasSprite> listAnimatedSprites;

    @Shadow
    // [AUDIT-OK] baseline member registerSprite (func_174942_a) exists in TextureMap
    public TextureAtlasSprite registerSprite(ResourceLocation location) {
        throw new AbstractMethodError();
    }

    @Shadow
    // [AUDIT-OK] baseline member getResourceLocation (func_184396_a) exists in TextureMap
    private ResourceLocation getResourceLocation(TextureAtlasSprite sprite) {
        throw new AbstractMethodError();
    }

    @Shadow
    // [AUDIT-OK] baseline member generateMipmaps (func_184397_a, returns Z) exists in TextureMap
    private boolean generateMipmaps(IResourceManager resourceManager, TextureAtlasSprite sprite) {
        throw new AbstractMethodError();
    }
    @Shadow
    // [AUDIT-OK] baseline member mapRegisteredSprites (field_110574_e) exists in TextureMap
    private Map<String, TextureAtlasSprite> mapRegisteredSprites;
    @Shadow
    // [AUDIT-OK] baseline member mapUploadedSprites (field_94252_e) exists in TextureMap
    private Map<String, TextureAtlasSprite> mapUploadedSprites;
    @Shadow
    // [AUDIT-OK] baseline member basePath (field_94254_c) exists in TextureMap
    private String basePath;
    @Shadow
    // [AUDIT-OK] baseline member iconCreator (field_174946_m) exists in TextureMap
    private ITextureMapPopulator iconCreator;
    @Shadow
    // [AUDIT-OK] baseline member mipmapLevels (field_147636_j) exists in TextureMap
    private int mipmapLevels;
    @Shadow
    // [AUDIT-OK] baseline member missingImage (field_94249_f) exists in TextureMap
    private TextureAtlasSprite missingImage;

    // [AUDIT-OK] OF-added fields (iconGrid/iconGridSize/iconGridCountX/Y/iconGridSizeU/V/counterIndexInMap/countAnimationsActive/frameCountAnimations), not in baseline
    @Unique
    private TextureAtlasSprite[] iconGrid;
    @Unique
    private int iconGridSize;
    @Unique
    private int iconGridCountX;
    @Unique
    private int iconGridCountY;
    @Unique
    private double iconGridSizeU;
    @Unique
    private double iconGridSizeV;
    @Unique
    private CounterInt counterIndexInMap;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added fields atlasWidth/atlasHeight (@Public), not in baseline
    @Unique
    private int atlasWidth;
    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private int atlasHeight;
    // ===== cross-class private access =====

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite setIndexInMap (I)V")
    // [AUDIT-OK] OF member setIndexInMap(I)V, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_setIndexInMap(TextureAtlasSprite sprite, int indexInMap);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite deleteSpriteTexture ()V")
    // [AUDIT-OK] OF member deleteSpriteTexture()V, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_deleteSpriteTexture(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite updateIndexInMap (Lnet/optifine/util/CounterInt;)V")
    // [AUDIT-OK] OF member updateIndexInMap(CounterInt)V, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_updateIndexInMap(TextureAtlasSprite sprite, net.optifine.util.CounterInt counterInt);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite setAnimationIndex (I)V")
    // [AUDIT-OK] OF member setAnimationIndex(I)V, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_setAnimationIndex(TextureAtlasSprite sprite, int animationIndex);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getAnimationIndex ()I")
    // [AUDIT-OK] OF member getAnimationIndex()I, MCP name (not in tsrg)
    private static native int TextureAtlasSprite_getAnimationIndex(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite bindSpriteTexture ()V")
    // [AUDIT-OK] OF member bindSpriteTexture()V, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_bindSpriteTexture(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite isAnimationActive ()Z")
    // [AUDIT-OK] OF member isAnimationActive()Z, MCP name (not in tsrg)
    private static native boolean TextureAtlasSprite_isAnimationActive(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getFramesTextureData ()Ljava/util/List;")
    // [AUDIT-OK] OF member getFramesTextureData()Ljava/util/List; (vanilla only has getFrameTextureData(int)+static 4-arg), MCP name
    private static native java.util.List<int[][]> TextureAtlasSprite_getFramesTextureData(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getAnimationMetadata ()Lnet/minecraft/client/resources/data/AnimationMetadataSection;")
    // [AUDIT-OK] OF member getAnimationMetadata(), MCP name (not in tsrg)
    private static native net.minecraft.client.resources.data.AnimationMetadataSection TextureAtlasSprite_getAnimationMetadata(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite setAnimationMetadata (Lnet/minecraft/client/resources/data/AnimationMetadataSection;)V")
    // [AUDIT-OK] OF member setAnimationMetadata(AnimationMetadataSection)V, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_setAnimationMetadata(TextureAtlasSprite sprite, net.minecraft.client.resources.data.AnimationMetadataSection animationMetadata);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteSingle Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    // [AUDIT-OK] OF field spriteSingle, MCP name (not in tsrg)
    private static native TextureAtlasSprite TextureAtlasSprite_spriteSingle_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteNormal Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    // [AUDIT-OK] OF field spriteNormal, MCP name (not in tsrg)
    private static native TextureAtlasSprite TextureAtlasSprite_spriteNormal_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteSpecular Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    // [AUDIT-OK] OF field spriteSpecular, MCP name (not in tsrg)
    private static native TextureAtlasSprite TextureAtlasSprite_spriteSpecular_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetWidth I")
    // [AUDIT-OK] OF field sheetWidth, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_sheetWidth_set(TextureAtlasSprite sprite, int value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetHeight I")
    // [AUDIT-OK] OF field sheetHeight, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_sheetHeight_set(TextureAtlasSprite sprite, int value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite mipmapLevels I")
    // [AUDIT-OK] OF field mipmapLevels, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_mipmapLevels_set(TextureAtlasSprite sprite, int value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite isEmissive Z")
    // [AUDIT-OK] OF field isEmissive, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_isEmissive_set(TextureAtlasSprite sprite, boolean value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteEmissive Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    // [AUDIT-OK] OF field spriteEmissive, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_spriteEmissive_set(TextureAtlasSprite sprite, TextureAtlasSprite value);

    @SuppressWarnings({"unused", "MissingUnique"})
    // [AUDIT-FIXED] generateMipmaps is private vanilla; cross-class INVOKEVIRTUAL from TextureAtlasSprite needs public
    @AccessTransformer(name = "func_184397_a", deobf = true, access = org.objectweb.asm.Opcodes.ACC_PUBLIC)
    private boolean acc_generateMipmaps(net.minecraft.client.resources.IResourceManager resourceManager, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite) { return false; }

        @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap func_184397_a (Lnet/minecraft/client/resources/IResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)Z", deobf = true)
    // [AUDIT-ISSUE] desc ends )V but func_184397_a returns Z (boolean); helper never called here (dead decl). Fix: )Z + deobf=true
    private static native boolean TextureMap_generateMipmaps(net.minecraft.client.renderer.texture.TextureMap textureMap, IResourceManager resourceManager, TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap isAbsoluteLocation (Lnet/minecraft/util/ResourceLocation;)Z")
    // [AUDIT-OK] OF member isAbsoluteLocation(ResourceLocation)Z, MCP name (not in tsrg)
    private static native boolean TextureMap_isAbsoluteLocation(net.minecraft.client.renderer.texture.TextureMap textureMap, ResourceLocation location);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getMultiTexID ()Lnet/optifine/shaders/MultiTexID;")
    // [AUDIT-OK] OF member getMultiTexID()Lnet/optifine/shaders/MultiTexID; (inherited from AbstractTexture), MCP name
    private static native net.optifine.shaders.MultiTexID TextureMap_getMultiTexID(net.minecraft.client.renderer.texture.TextureMap textureMap);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager getBoundTexture ()I")
    // [AUDIT-OK] OF member GlStateManager.getBoundTexture()I (OF GlStateManager:797), MCP name (not in tsrg)
    private static native int GlStateManager_getBoundTexture();

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.texture.TextureUtil func_94277_a (I)V")
    // [AUDIT-OK] vanilla SRG func_94277_a = TextureUtil.bindTexture(I)V
    private static native void TextureUtil_bindTexture(int texture);

    private int countAnimationsActive;
    @Unique
    private int frameCountAnimations;

    // ===== initMissingImage: dynamic size + index =====

    @Inject(method = "initMissingImage", at = @At("HEAD"), cancellable = true)
    // [AUDIT-OK] target initMissingImage()V (func_110569_e) matches baseline; HEAD-cancel replicates OF dynamic-size init
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

    // [AUDIT-OK] target loadSprites(IResourceManager,ITextureMapPopulator)V (func_174943_a) matches baseline; reset before registerSprites like OF
    private void optiRefine$loadSpritesReset(IResourceManager resourceManager, ITextureMapPopulator iconCreatorIn, CallbackInfo ci) {

        this.counterIndexInMap.reset();

    }



    @Inject(method = "loadSprites", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/ITextureMapPopulator;registerSprites(Lnet/minecraft/client/renderer/texture/TextureMap;)V", shift = At.Shift.AFTER))

    // [AUDIT-OK] target loadSprites (func_174943_a) matches baseline; mipmap detection after registerSprites like OF
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
    // [AUDIT-OK] target loadTextureAtlas(IResourceManager)V (func_110571_b) matches baseline; body mirrors OF rewrite
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
                    net.minecraftforge.fml.client.FMLClientHandler.instance().trackBrokenTexture(resourcelocation, runtimeexception.getMessage());
                    continue;
                } catch (IOException ioexception) {
                    org.apache.logging.log4j.LogManager.getLogger().error("Using missing texture, unable to load " + resourcelocation + ", " + ioexception.getClass().getName());
                    net.minecraftforge.fml.client.FMLClientHandler.instance().trackMissingTexture(resourcelocation);
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
    // [AUDIT-OK] target generateMipmaps (func_184397_a)Z matches baseline; body mirrors OF (custom-loader + crash report)
    private boolean optiRefine$generateMipmaps(IResourceManager resourceManager, TextureAtlasSprite sprite, Operation<Boolean> original) {
        ResourceLocation resourcelocation = this.getResourceLocation(sprite);
        IResource iresource = null;
        if (sprite.hasCustomLoader(resourceManager, resourcelocation)) {
            // [AUDIT-FIXED] three-way audit (P12): OF falls through to the shared sprite.generateMipmaps
            // tail after generateCustomMipmaps (OF:353-366); early return skipped it.
            TextureUtils.generateCustomMipmaps(sprite, this.mipmapLevels);
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

    // ===== resource location helpers =====

    @SuppressWarnings("unused")
    @mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer(name = "func_184396_a", access = org.objectweb.asm.Opcodes.ACC_PUBLIC, deobf = true)
    // [AUDIT-ISSUE] vanilla member getResourceLocation = func_184396_a; AT uses MCP name w/o deobf=true -> silent no-op at SRG runtime (stays private); impact needs-verification
    private ResourceLocation acc_getResourceLocation(TextureAtlasSprite sprite) {
        return null;
    }

    /**
     * @author OptiRefine
     * @reason OptiFine: complete resource location with base path
     */
    @WrapMethod(method = "getResourceLocation")
    // [AUDIT-OK] target getResourceLocation(TextureAtlasSprite)LResourceLocation matches baseline; mirrors OF
    private ResourceLocation optiRefine$getResourceLocation(TextureAtlasSprite sprite, Operation<ResourceLocation> original) {
        ResourceLocation resourcelocation = new ResourceLocation(sprite.getIconName());
        return this.completeResourceLocation(resourcelocation);
    }

    @Public
    // [AUDIT-OK] OF-added @Public method completeResourceLocation, not in baseline
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
    // [AUDIT-OK] target updateAnimations()V (func_94248_c) matches baseline; body mirrors OF
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

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    // [AUDIT-OK] OF-added private method isAbsoluteLocation (MCP name), merged to satisfy OF jar (AGENT.md crash record); note: no @Unique
    private boolean isAbsoluteLocation(ResourceLocation location) {
        return this.isAbsoluteLocationPath(location.getPath());
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    // [AUDIT-OK] OF-added private method isAbsoluteLocationPath (MCP name); note: no @Unique
    private boolean isAbsoluteLocationPath(String path) {
        String s = path.toLowerCase();
        return s.startsWith("mcpatcher/") || s.startsWith("optifine/");
    }

    // ===== registerSprite: index + emissive =====

    @WrapMethod(method = "registerSprite")
    // [AUDIT-FIXED] three-way audit (P12): RETURN-inject re-ran updateIndexInMap+checkEmissive on
    // cache hits -> counterIndexInMap inflated, getCountRegisteredSprites distorted. Replaced with
    // OF's exact body (OF:518-531): index+emissive only in the new-sprite branch.
    private TextureAtlasSprite optiRefine$registerSprite(ResourceLocation location, Operation<TextureAtlasSprite> original) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite textureatlassprite = this.mapRegisteredSprites.get(location.toString());
        if (textureatlassprite == null) {
            textureatlassprite = TextureAtlasSprite_makeAtlasSprite(location);
            this.mapRegisteredSprites.put(location.toString(), textureatlassprite);
            TextureAtlasSprite_updateIndexInMap(textureatlassprite, this.counterIndexInMap);
            if (Config.isEmissiveTextures()) {
                this.checkEmissive(location, textureatlassprite);
            }
        }
        return textureatlassprite;
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite func_176604_a (Lnet/minecraft/util/ResourceLocation;)Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;", deobf = true)
    // [AUDIT-FIXED] 2026-08-05 crash: SRG runtime uses the raw desc name (deobf remaps only on MCP
    // runtime), so the vanilla member must be written as its SRG name func_176604_a (= makeAtlasSprite,
    // tsrg:12970). The MCP name caused NoSuchMethodError at startup.
    private static native TextureAtlasSprite TextureAtlasSprite_makeAtlasSprite(ResourceLocation location);

    // ===== new methods =====

    @Public
    // [AUDIT-OK] OF-added @Public method getSpriteSafe(String), not in baseline
    public TextureAtlasSprite getSpriteSafe(String name) {
        ResourceLocation resourcelocation = new ResourceLocation(name);
        return this.mapRegisteredSprites.get(resourcelocation.toString());
    }

    @Public
    // [AUDIT-OK] OF-added @Public method getRegisteredSprite(ResourceLocation), not in baseline
    public TextureAtlasSprite getRegisteredSprite(ResourceLocation location) {
        return this.mapRegisteredSprites.get(location.toString());
    }

    // [AUDIT-OK] OF-added @Unique method isTerrainAnimationActive, not in baseline
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

    @Public
    // [AUDIT-OK] OF-added @Public method getCountRegisteredSprites, not in baseline
    public int getCountRegisteredSprites() {
        return this.counterIndexInMap.getValue();
    }

    @Unique
    // [AUDIT-OK] OF-added @Unique method detectMaxMipmapLevel, not in baseline
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

    // [AUDIT-OK] OF-added @Unique method detectMinimumSpriteSize, not in baseline
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
    // [AUDIT-OK] OF-added @Unique method getMinSpriteSize, not in baseline
    private int getMinSpriteSize() {
        int i = 1 << this.mipmapLevels;
        if (i < 8) {
            i = 8;
        }
        return i;
    }

    // [AUDIT-OK] OF-added @Unique method getMissingImageData, not in baseline
    private int[] getMissingImageData(int size) {
        BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
        BufferedImage bufferedimage1 = TextureUtils.scaleImage(bufferedimage, size);
        int[] aint = new int[size * size];
        bufferedimage1.getRGB(0, 0, size, size, aint, 0, size);
        return aint;
    }

    @Public
    // [AUDIT-OK] OF-added @Public method isTextureBound, not in baseline
    public boolean isTextureBound() {
        int i = GlStateManager_getBoundTexture();
        int j = this.getGlTextureId();
        return i == j;
    }

    @Unique
    // [AUDIT-OK] OF-added @Unique method updateIconGrid, not in baseline
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

    @Public
    // [AUDIT-OK] OF-added @Public method getIconByUV, not in baseline
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
    // [AUDIT-OK] OF-added @Unique method checkEmissive, not in baseline
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

    @Public
    // [AUDIT-OK] OF-added @Public method getCountAnimations, not in baseline
    public int getCountAnimations() {
        return this.listAnimatedSprites.size();
    }

    @Public
    // [AUDIT-OK] OF-added @Public method getCountAnimationsActive, not in baseline
    public int getCountAnimationsActive() {
        return this.countAnimationsActive;
    }


    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.iconGrid = null;
        this.iconGridSize = -1;
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGridSizeU = -1.0;
        this.iconGridSizeV = -1.0;
        this.counterIndexInMap = new CounterInt(0);
        this.atlasWidth = 0;
        this.atlasHeight = 0;
    }
}
