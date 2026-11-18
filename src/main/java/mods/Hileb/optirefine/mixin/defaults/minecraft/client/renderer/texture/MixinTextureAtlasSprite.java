package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.ShadowSuper;
import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.AtlasSpriteMipmaps;
import mods.Hileb.optirefine.optifine.resources.ModernTextureProperties;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.SmartAnimations;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersTex;
import net.optifine.util.CounterInt;
import net.optifine.util.TextureUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * OptiFine additions to {@link TextureAtlasSprite}.
 *
 * <p>Implements the OptiFine-specific fields and methods (spriteSingle/spriteNormal/spriteSpecular
 * bookkeeping, SmartAnimations integration, transparent color fixing, shaders sprites, mipmap
 * propagation, custom UV helpers). The {@code hasCustomLoader}/{@code load}/{@code getDependencies}
 * methods are provided by the Cleanroom patches; the UV inset in {@code initSprite} was REMOVED by
 * the Cleanroom patch and is restored by {@code optiRefine$initSprite} (OF behavior, 2026-08-05).</p>
 */
@Mixin(TextureAtlasSprite.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1
public abstract class MixinTextureAtlasSprite implements AtlasSpriteMipmaps {

    @Unique private boolean optiRefine$allowRectangularLoad;
    @Unique private boolean optiRefine$alignedResource;
    @Unique private boolean optiRefine$labPbrSpecular;

    // ===== new fields =====

    // [AUDIT-OK] OF-added fields (indexInMap/animationIndex/animationActive), not in baseline
    @Unique
    private int indexInMap;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added fields baseU/baseV (@Public), not in baseline
    public float baseU;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    public float baseV;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added fields sheetWidth/sheetHeight (@Public), not in baseline
    public int sheetWidth;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    public int sheetHeight;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added field glSpriteTextureId (@Public), not in baseline
    @Unique
    private int glSpriteTextureId;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added fields spriteSingle/isSpriteSingle (@Public), not in baseline
    @Unique
    private TextureAtlasSprite spriteSingle;
    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private boolean isSpriteSingle;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added field mipmapLevels (@Public), not in baseline
    @Unique
    private int mipmapLevels;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added fields spriteNormal/spriteSpecular (@Public), not in baseline
    @Unique
    private TextureAtlasSprite spriteNormal;
    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private TextureAtlasSprite spriteSpecular;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added fields isShadersSprite/isDependencyParent/isEmissive (@Public), not in baseline
    @Unique
    private boolean isShadersSprite;
    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private boolean isDependencyParent;
    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private boolean isEmissive;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added field spriteEmissive (@Public), not in baseline
    @Unique
    private TextureAtlasSprite spriteEmissive;
    @Unique
    private int animationIndex;
    @Unique
    private boolean animationActive;

    // ===== shadowed existing fields =====

    @Shadow
    // [AUDIT-OK] baseline member iconName exists in TextureAtlasSprite
    private String iconName;
    @Shadow
    // [AUDIT-OK] baseline member framesTextureData exists in TextureAtlasSprite
    protected List<int[][]> framesTextureData;
    @Shadow
    // [AUDIT-OK] baseline member interpolatedFrameData exists in TextureAtlasSprite
    protected int[][] interpolatedFrameData;
    @Shadow
    // [AUDIT-OK] baseline member animationMetadata exists in TextureAtlasSprite
    private AnimationMetadataSection animationMetadata;
    @Shadow
    // [AUDIT-OK] baseline members frameCounter/tickCounter exist in TextureAtlasSprite
    protected int frameCounter;
    @Shadow
    protected int tickCounter;
    @Shadow
    // [AUDIT-OK] baseline members originX/originY exist in TextureAtlasSprite
    protected int originX;
    @Shadow
    protected int originY;
    @Shadow
    // [AUDIT-OK] baseline members width/height exist in TextureAtlasSprite
    protected int width;
    @Shadow
    protected int height;
    @Shadow
    // [AUDIT-OK] baseline member rotated exists in TextureAtlasSprite
    protected boolean rotated;
    @Shadow
    // [AUDIT-OK] baseline members minU/maxU/minV/maxV exist in TextureAtlasSprite
    private float minU;
    @Shadow
    private float maxU;
    @Shadow
    private float minV;
    @Shadow
    private float maxV;

    @ShadowSuper("<init>")
    // [AUDIT-OK] @ShadowSuper: super call to Object.<init> (target super is Object)
    public void optiRefine$invokeObjectConstructor() {
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite (Ljava/lang/String;Z)V")
    // [AUDIT-OK] OF private ctor TextureAtlasSprite(String,Z), MCP name N/A (ctor); NEW+Construction idiom
    private static native TextureAtlasSprite optiRefine$createTextureAtlasSprite(AccessibleOperation.Construction construction, String name, boolean isSpriteSingle);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite baseU F")
    // [AUDIT-OK] OF field baseU, MCP name (not in tsrg)
    private static native float optiRefine$getTextureAtlasSpriteBaseU(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite baseV F")
    // [AUDIT-OK] OF field baseV, MCP name (not in tsrg)
    private static native float optiRefine$getTextureAtlasSpriteBaseV(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetWidth I")
    // [AUDIT-OK] OF field sheetWidth, MCP name (not in tsrg)
    private static native int optiRefine$getTextureAtlasSpriteSheetWidth(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetHeight I")
    // [AUDIT-OK] OF field sheetHeight, MCP name (not in tsrg)
    private static native int optiRefine$getTextureAtlasSpriteSheetHeight(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite glSpriteTextureId I")
    // [AUDIT-OK] OF field glSpriteTextureId, MCP name (not in tsrg)
    private static native int optiRefine$getTextureAtlasSpriteGlSpriteTextureId(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite mipmapLevels I")
    // [AUDIT-OK] OF field mipmapLevels, MCP name (not in tsrg)
    private static native int optiRefine$getTextureAtlasSpriteMipmapLevels(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getIndexInMap ()I")
    // [AUDIT-OK] OF member getIndexInMap()I, MCP name (not in tsrg)
    private static native int optiRefine$getTextureAtlasSpriteIndexInMap(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getAnimationIndex ()I")
    // [AUDIT-OK] OF member getAnimationIndex()I, MCP name (not in tsrg)
    private static native int optiRefine$getTextureAtlasSpriteAnimationIndex(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap func_184397_a (Lnet/minecraft/client/resources/IResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)Z", deobf = true)
    // [AUDIT-OK] desc )Z + deobf=true verified 2026-08-05
    private static native boolean optiRefine$generateTextureMapMipmaps(net.minecraft.client.renderer.texture.TextureMap textureMap, IResourceManager resourceManager, TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap completeResourceLocation (Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/util/ResourceLocation;")
    // [AUDIT-OK] OF member completeResourceLocation(ResourceLocation)LResourceLocation, MCP name (not in tsrg)
    private static native ResourceLocation optiRefine$completeTextureMapResourceLocation(net.minecraft.client.renderer.texture.TextureMap textureMap, ResourceLocation location);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite setAnimationIndex (I)V")
    // [AUDIT-OK] OF member setAnimationIndex(I)V, MCP name (not in tsrg)
    private static native void optiRefine$setTextureAtlasSpriteAnimationIndex(TextureAtlasSprite sprite, int animationIndex);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite func_130099_d (I)V", deobf = true)
    // [AUDIT-OK] vanilla SRG func_130099_d = allocateFrameTextureData(I)V; missing deobf=true (devrun only)
    private static native void optiRefine$allocateSpriteFrameData(TextureAtlasSprite sprite, int index);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite func_130102_n ()V", deobf = true)
    // [AUDIT-OK] vanilla SRG func_130102_n = resetSprite()V; missing deobf=true (devrun only)
    private static native void optiRefine$resetSingleSprite(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern", "MissingUnique"})
    @NewConstructor
    @Public
    // This constructor is added after mixin injection, so initialize both baseline and added state here.
    public void optiRefine$initializeSpriteTextureState(String name, boolean isSpriteSingle) {
        optiRefine$invokeObjectConstructor();
        this.iconName = name;
        this.framesTextureData = new ArrayList<>();
        this.optiRefine$initializeSpriteState();
        this.isSpriteSingle = isSpriteSingle;
    }

    // ===== constructor: spriteSingle init =====

    @Inject(method = "<init>(Ljava/lang/String;)V", at = @At("RETURN"))
    // [AUDIT-OK] target <init>(Ljava/lang/String;)V matches baseline; RETURN inject replicates OF spriteSingle init
    private void optiRefine$initializeMultiTextureSpriteVariant(CallbackInfo ci) {
        this.optiRefine$initializeSpriteState();
        if (Config.isMultiTexture()) {
            this.spriteSingle = optiRefine$createTextureAtlasSprite(AccessibleOperation.Construction.construction(), this.getIconName() + ".spriteSingle", true);
        }
    }

    @Shadow
    // [AUDIT-OK] baseline member getIconName exists in TextureAtlasSprite
    public String getIconName() {
        return null;
    }

    // ===== initSprite: baseU/baseV + sprite propagation =====

    @Inject(method = "initSprite", at = @At("RETURN"))
    // [AUDIT-FIXED] 2026-08-05: cleanroom patch REMOVED the 0.01F UV inset from initSprite
    // (patch: @@ -49,12 +49,10); OF keeps it (OF:79-87) as the atlas-border bleeding guard.
    // Recompute minU..maxV with the inset, then OF baseU/baseV + sprite propagation.
    private void optiRefine$initSprite(int atlasWidth, int atlasHeight, int spriteX, int spriteY, boolean rotated, CallbackInfo ci) {
        float f = (float) (0.01F / atlasWidth);
        float f1 = (float) (0.01F / atlasHeight);
        this.minU = spriteX / (float) atlasWidth + f;
        this.maxU = (spriteX + this.width) / (float) atlasWidth - f;
        this.minV = (float) spriteY / atlasHeight + f1;
        this.maxV = (float) (spriteY + this.height) / atlasHeight - f1;
        this.baseU = Math.min(this.minU, this.maxU);
        this.baseV = Math.min(this.minV, this.maxV);
        if (this.spriteSingle != null) {
            this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
        }
        if (this.spriteNormal != null) {
            this.spriteNormal.copyFrom((TextureAtlasSprite) (Object) this);
        }
        if (this.spriteSpecular != null) {
            this.spriteSpecular.copyFrom((TextureAtlasSprite) (Object) this);
        }
    }

    // ===== copyFrom: index/base/sheet/glId/mipmap/animation propagation =====

    @Inject(method = "copyFrom", at = @At("RETURN"))
    // [AUDIT-OK] target copyFrom(TextureAtlasSprite)V matches baseline; RETURN inject replicates OF index/base/sheet/glId/mipmap/animation propagation
    private void optiRefine$copyFrom(TextureAtlasSprite atlasSpirit, CallbackInfo ci) {
        if (atlasSpirit != Config.getTextureMap().getMissingSprite()) {
            this.indexInMap = optiRefine$getTextureAtlasSpriteIndexInMap(atlasSpirit);
        }
        this.baseU = optiRefine$getTextureAtlasSpriteBaseU(atlasSpirit);
        this.baseV = optiRefine$getTextureAtlasSpriteBaseV(atlasSpirit);
        this.sheetWidth = optiRefine$getTextureAtlasSpriteSheetWidth(atlasSpirit);
        this.sheetHeight = optiRefine$getTextureAtlasSpriteSheetHeight(atlasSpirit);
        this.glSpriteTextureId = optiRefine$getTextureAtlasSpriteGlSpriteTextureId(atlasSpirit);
        this.mipmapLevels = optiRefine$getTextureAtlasSpriteMipmapLevels(atlasSpirit);
        if (this.spriteSingle != null) {
            this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
        }
        this.animationIndex = optiRefine$getTextureAtlasSpriteAnimationIndex(atlasSpirit);
    }

    // ===== updateAnimation: SmartAnimations + null guard =====

    /**
     * @author OptiRefine
     * @reason OptiFine: null animation guard + SmartAnimations active tracking
     */
    @WrapMethod(method = "updateAnimation")
    // [AUDIT-OK] target updateAnimation()V matches baseline; WrapMethod body mirrors OF (SmartAnimations + null guard)
    private void optiRefine$updateAnimation(Operation<Void> original) {
        if (this.animationMetadata != null) {
            this.animationActive = SmartAnimations.isActive() ? SmartAnimations.isSpriteRendered(this.animationIndex) : true;
            this.tickCounter++;
            if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
                int i = this.animationMetadata.getFrameIndex(this.frameCounter);
                int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
                this.frameCounter = (this.frameCounter + 1) % j;
                this.tickCounter = 0;
                int k = this.animationMetadata.getFrameIndex(this.frameCounter);
                boolean flag = false;
                boolean flag1 = this.isSpriteSingle;
                if (!this.animationActive) {
                    return;
                }
                if (i != k && k >= 0 && k < this.framesTextureData.size()) {
                    if (this.optiRefine$labPbrSpecular) {
                        ShadersTex.uploadTexSub1(this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, 0);
                    } else {
                        TextureUtil.uploadTextureMipmap(this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, flag, flag1);
                    }
                }
            } else if (this.animationMetadata.isInterpolate()) {
                if (!this.animationActive) {
                    return;
                }
                this.updateAnimationInterpolated();
            }
        }
    }

    @WrapOperation(method = "updateAnimationInterpolated", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureMipmap([[IIIIIZZ)V"))
    private void optiRefine$uploadInterpolated(int[][] data, int width, int height, int x, int y, boolean blur, boolean clamp,
                                              Operation<Void> original) {
        if (this.optiRefine$labPbrSpecular) {
            ShadersTex.uploadTexSub1(data, width, height, x, y, 0);
        } else {
            original.call(data, width, height, x, y, blur, this.isSpriteSingle);
        }
    }

    @Shadow
    // [AUDIT-OK] baseline member updateAnimationInterpolated exists in TextureAtlasSprite
    private void updateAnimationInterpolated() {
    }

    // ===== setIconWidth/setIconHeight: spriteSingle propagation =====

    @Inject(method = "setIconWidth", at = @At("RETURN"))
    // [AUDIT-OK] target setIconWidth(I)V matches baseline; RETURN inject replicates OF spriteSingle propagation
    private void optiRefine$setIconWidth(int widthIn, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.setIconWidth(this.width);
        }
    }

    @Inject(method = "setIconHeight", at = @At("RETURN"))
    // [AUDIT-OK] target setIconHeight(I)V matches baseline; RETURN inject replicates OF spriteSingle propagation
    private void optiRefine$setIconHeight(int heightIn, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.setIconHeight(this.height);
        }
    }

    @Override
    public void optiRefine$loadAtlasSprite(PngSizeInfo size, boolean animated, int alignment) throws IOException {
        TextureAtlasSprite sprite = (TextureAtlasSprite) (Object) this;
        this.optiRefine$clearAlignedResource();
        boolean previous = this.optiRefine$allowRectangularLoad;
        this.optiRefine$allowRectangularLoad = alignment > 0;
        try {
            sprite.loadSprite(size, animated);
        } finally {
            this.optiRefine$allowRectangularLoad = previous;
        }

        int width = sprite.getIconWidth();
        int height = sprite.getIconHeight();
        if (alignment > 0 && width > 0 && height > 0) {
            int alignedWidth = TextureUtils.scaleToGrid(width, alignment);
            int alignedHeight = TextureUtils.scaleToGrid(height, alignment);
            this.optiRefine$alignedResource = width != alignedWidth || height != alignedHeight || width != height;
            if (this.optiRefine$alignedResource) {
                this.interpolatedFrameData = null;
            }
            if (width != alignedWidth || height != alignedHeight) {
                Config.log("Scaled sprite: " + sprite.getIconName() + ", " + width + "x" + height + " -> " + alignedWidth + "x" + alignedHeight);
                sprite.setIconWidth(alignedWidth);
                sprite.setIconHeight(alignedHeight);
            }
        }
    }

    @WrapOperation(method = "loadSprite", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/PngSizeInfo;pngHeight:I", ordinal = 1))
    private int optiRefine$staticAspectRatioCheck(PngSizeInfo size, Operation<Integer> original) {
        // The first read sets storage height. The second only enforces the vanilla square requirement.
        return this.optiRefine$allowRectangularLoad ? size.pngWidth : original.call(size);
    }

    @Override
    public void optiRefine$clearAlignedResource() {
        if (this.optiRefine$alignedResource) {
            this.optiRefine$alignedResource = false;
            this.interpolatedFrameData = null;
            if (this.spriteSingle != null) {
                ((AtlasSpriteMipmaps) this.spriteSingle).optiRefine$clearAlignedResource();
            }
        }
    }

    @Override
    public boolean optiRefine$hasAlignedResource() {
        return this.optiRefine$alignedResource;
    }

    @Override
    public int[][] optiRefine$getInitialFrame() {
        int index = this.optiRefine$alignedResource && this.animationMetadata != null
                ? this.animationMetadata.getFrameIndex(0) : 0;
        return ((TextureAtlasSprite) (Object) this).getFrameTextureData(index);
    }

    @Override
    public void optiRefine$limitMipLevels(int levels) {
        List<int[][]> limited = new ArrayList<>(this.framesTextureData.size());
        for (int[][] frame : this.framesTextureData) {
            limited.add(frame != null && frame.length > levels ? Arrays.copyOf(frame, levels) : frame);
        }
        ((TextureAtlasSprite) (Object) this).setFramesTextureData(limited);
        if (this.spriteNormal != null) {
            ((AtlasSpriteMipmaps) this.spriteNormal).optiRefine$limitMipLevels(levels);
        }
        if (this.spriteSpecular != null) {
            ((AtlasSpriteMipmaps) this.spriteSpecular).optiRefine$limitMipLevels(levels);
        }
    }

    // ===== loadSprite: spriteSingle size propagation =====

    @Inject(method = "loadSprite", at = @At("RETURN"))
    // [AUDIT-OK] target loadSprite(PngSizeInfo,Z)V matches baseline; RETURN inject replicates OF size propagation
    private void optiRefine$loadSprite(net.minecraft.client.renderer.texture.PngSizeInfo sizeInfo, boolean animation, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.width = this.width;
            this.spriteSingle.height = this.height;
        }
    }

    // ===== loadSpriteFrames: scale + shaders sprites + transparent fix =====

    /**
     * @author OptiRefine
     * @reason OptiFine: image scaling, shaders sprites, transparent color fixing, spriteSingle propagation
     */
    @WrapMethod(method = "loadSpriteFrames")
    // [AUDIT-OK] target loadSpriteFrames(IResource,I)V matches baseline; WrapMethod body mirrors OF (scale/shaders/fixTransparent/spriteSingle)
    private void optiRefine$loadSpriteFrames(IResource resource, int mipmapLevelsIn, Operation<Void> original) throws java.io.IOException {
        java.awt.image.BufferedImage bufferedimage = TextureUtil.readBufferedImage(resource.getInputStream());
        if (bufferedimage == null) {
            throw new java.io.IOException("Unable to decode sprite " + this.iconName);
        }
        if (!this.optiRefine$alignedResource && this.width != bufferedimage.getWidth()) {
            bufferedimage = TextureUtils.scaleImage(bufferedimage, this.width);
        }
        this.optiRefine$readFrames(bufferedimage, resource.getMetadata("animation"), mipmapLevelsIn);

        if (!this.isShadersSprite) {
            if (!this.optiRefine$alignedResource && Config.isShaders()) {
                this.optiRefine$loadShaderSidecars(Config.getTextureMap(), Config.getResourceManager());
            }
            for (int[][] frame : this.framesTextureData) {
                if (frame != null) {
                    for (int[] pixels : frame) {
                        ModernTextureProperties.fixTransparentColor(this.iconName, pixels);
                    }
                }
            }
            if (this.spriteSingle != null) {
                if (this.optiRefine$alignedResource) {
                    MixinTextureAtlasSprite single = (MixinTextureAtlasSprite) (Object) this.spriteSingle;
                    single.optiRefine$alignedResource = true;
                    single.animationMetadata = this.animationMetadata;
                    single.interpolatedFrameData = null;
                } else {
                    try (IResource iresource = Config.getResourceManager().getResource(resource.getResourceLocation())) {
                        this.spriteSingle.loadSpriteFrames(iresource, mipmapLevelsIn);
                    }
                }
            }
        }
    }

    @Unique
    private void optiRefine$readFrames(BufferedImage image, AnimationMetadataSection metadata, int levels) throws IOException {
        if (metadata == null) {
            this.framesTextureData.add(this.optiRefine$readFrame(image, 0, image.getHeight(), levels));
            return;
        }

        int frameSize = image.getWidth();
        int frameCount = image.getHeight() / frameSize;
        if (frameCount == 0 || image.getHeight() % frameSize != 0) {
            throw new IOException("Invalid animation dimensions for " + this.iconName);
        }
        boolean implicitOrder = metadata.getFrameCount() == 0;
        int animationLength = implicitOrder ? frameCount : metadata.getFrameCount();
        List<AnimationFrame> frames = implicitOrder ? new ArrayList<>(animationLength) : null;
        for (int index = 0; index < animationLength; ++index) {
            int frame = implicitOrder ? index : metadata.getFrameIndex(index);
            if (frame < 0 || frame >= frameCount) {
                throw new IllegalArgumentException("invalid frameindex " + frame);
            }
            this.allocateFrameTextureData(frame);
            if (this.framesTextureData.get(frame) == null) {
                this.framesTextureData.set(frame, this.optiRefine$readFrame(image, frame * frameSize, frameSize, levels));
            }
            if (implicitOrder) {
                frames.add(new AnimationFrame(frame, -1));
            }
        }
        this.animationMetadata = implicitOrder
                ? new AnimationMetadataSection(frames, this.width, this.height, metadata.getFrameTime(), metadata.isInterpolate())
                : metadata;
    }

    @Unique
    private int[][] optiRefine$readFrame(BufferedImage source, int sourceY, int sourceHeight, int levels) {
        int sourceWidth = source.getWidth();
        if (this.optiRefine$alignedResource && (sourceWidth != this.width || sourceHeight != this.height)) {
            BufferedImage frame = source.getSubimage(0, sourceY, sourceWidth, sourceHeight);
            BufferedImage scaled = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = scaled.createGraphics();
            try {
                graphics.setComposite(AlphaComposite.Src);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        this.width % sourceWidth == 0 && this.height % sourceHeight == 0
                                ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR : RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics.drawImage(frame, 0, 0, this.width, this.height, null);
            } finally {
                graphics.dispose();
            }
            source = scaled;
            sourceY = 0;
            sourceWidth = this.width;
            sourceHeight = this.height;
        }
        int[][] data = new int[levels][];
        data[0] = source.getRGB(0, sourceY, sourceWidth, sourceHeight, null, 0, sourceWidth);
        return data;
    }

    @Shadow
    // [AUDIT-OK] baseline member allocateFrameTextureData exists in TextureAtlasSprite
    private void allocateFrameTextureData(int index) {
    }

    @Override
    public void optiRefine$loadShaderSidecars(TextureMap atlas, IResourceManager resourceManager) {
        if (this.isShadersSprite) {
            return;
        }
        if (Shaders.configNormalMap) {
            this.spriteNormal = this.optiRefine$loadSidecar(atlas, resourceManager, "_n");
        }
        if (Shaders.configSpecularMap) {
            this.spriteSpecular = this.optiRefine$loadSidecar(atlas, resourceManager, "_s");
        }
    }

    @Unique
    private TextureAtlasSprite optiRefine$loadSidecar(TextureMap atlas, IResourceManager resourceManager, String suffix) {
        String name = this.iconName + suffix;
        ResourceLocation location = optiRefine$completeTextureMapResourceLocation(atlas, new ResourceLocation(name));
        try (IResource resource = resourceManager.getResource(location)) {
            if (resource == null) {
                return null;
            }
        } catch (IOException missing) {
            return null;
        }
        TextureAtlasSprite sidecar = new TextureAtlasSprite(name);
        MixinTextureAtlasSprite sprite = (MixinTextureAtlasSprite) (Object) sidecar;
        sprite.isShadersSprite = true;
        sprite.optiRefine$labPbrSpecular = suffix.equals("_s") && ModernTextureProperties.isLabPbr();
        sidecar.copyFrom((TextureAtlasSprite) (Object) this);
        sprite.optiRefine$alignedResource = this.optiRefine$alignedResource;
        return optiRefine$generateTextureMapMipmaps(atlas, resourceManager, sidecar) ? sidecar : null;
    }

    // ===== generateMipmaps + frame helpers: spriteSingle propagation =====

    @WrapOperation(method = "generateMipmaps", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;generateMipmapData(II[[I)[[I"))
    private int[][] optiRefine$filterShaderMipmaps(int levels, int width, int[][] data, Operation<int[][]> original) {
        return this.isShadersSprite && ModernTextureProperties.isLabPbr()
                ? ModernTextureProperties.generateLabPbrMipmaps(levels, width, this.height, data, this.optiRefine$labPbrSpecular)
                : original.call(levels, width, data);
    }

    @ModifyArg(method = "generateMipmaps", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;setFramesTextureData(Ljava/util/List;)V"))
    private List<int[][]> optiRefine$keepAlignedFrameIndices(List<int[][]> generatedFrames) {
        if (!this.optiRefine$alignedResource || generatedFrames.size() == this.framesTextureData.size()) {
            return generatedFrames;
        }
        // The baseline compacts null slots, but animation metadata indexes the source strip.
        List<int[][]> frames = new ArrayList<>(this.framesTextureData.size());
        int generatedIndex = 0;
        for (int[][] frame : this.framesTextureData) {
            frames.add(frame == null ? null : generatedFrames.get(generatedIndex++));
        }
        return frames;
    }

    @Inject(method = "generateMipmaps", at = @At("RETURN"))
    // [AUDIT-OK] target generateMipmaps(I)V (func_147963_d) matches baseline; RETURN inject replicates OF spriteSingle propagation
    private void optiRefine$generateMipmaps(int mipmapLevelsIn, CallbackInfo ci) {
        if (this.spriteSingle != null && !this.optiRefine$alignedResource) {
            this.spriteSingle.generateMipmaps(mipmapLevelsIn);
        }
    }

    @Inject(method = "allocateFrameTextureData", at = @At("RETURN"))
    // [AUDIT-OK] target allocateFrameTextureData(I)V matches baseline; RETURN inject replicates OF spriteSingle propagation
    private void optiRefine$allocateFrameTextureData(int index, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            optiRefine$allocateSpriteFrameData(this.spriteSingle, index);
        }
    }

    @Inject(method = "clearFramesTextureData", at = @At("RETURN"))
    // [AUDIT-OK] target clearFramesTextureData()V matches baseline; RETURN inject replicates OF spriteSingle propagation
    private void optiRefine$clearFramesTextureData(CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.clearFramesTextureData();
        }
    }

    @Inject(method = "setFramesTextureData", at = @At("RETURN"))
    // [AUDIT-OK] target setFramesTextureData(List)V matches baseline; RETURN inject replicates OF spriteSingle propagation
    private void optiRefine$setFramesTextureData(List<int[][]> newFramesTextureData, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.setFramesTextureData(newFramesTextureData);
        }
    }

    @Inject(method = "resetSprite", at = @At("RETURN"))
    // [AUDIT-OK] target resetSprite()V matches baseline; RETURN inject replicates OF spriteSingle propagation
    private void optiRefine$resetSprite(CallbackInfo ci) {
        this.optiRefine$clearAlignedResource();
        this.spriteNormal = null;
        this.spriteSpecular = null;
        this.animationIndex = -1;
        this.animationActive = false;
        if (this.spriteSingle != null) {
            optiRefine$resetSingleSprite(this.spriteSingle);
        }
    }

    @Shadow
    // [AUDIT-OK] baseline member resetSprite exists in TextureAtlasSprite
    private void resetSprite() {
    }

    // ===== new public methods =====

    @Public
    // [AUDIT-OK] OF-added @Public method getIndexInMap, not in baseline
    public int getIndexInMap() {
        return this.indexInMap;
    }

    @Public
    // [AUDIT-OK] OF-added @Public method setIndexInMap, not in baseline
    public void setIndexInMap(int indexInMap) {
        this.indexInMap = indexInMap;
    }

    @Public
    // [AUDIT-OK] OF-added @Public method updateIndexInMap, not in baseline
    public void updateIndexInMap(CounterInt counterInt) {
        if (this.indexInMap < 0) {
            this.indexInMap = counterInt.nextValue();
        }
    }

    @Public
    // [AUDIT-OK] OF-added @Public method getAnimationIndex, not in baseline
    public int getAnimationIndex() {
        return this.animationIndex;
    }

    @Public
    // [AUDIT-OK] OF-added @Public method setAnimationIndex, not in baseline
    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
        if (this.spriteNormal != null) {
            optiRefine$setTextureAtlasSpriteAnimationIndex(this.spriteNormal, animationIndex);
        }
        if (this.spriteSpecular != null) {
            optiRefine$setTextureAtlasSpriteAnimationIndex(this.spriteSpecular, animationIndex);
        }
    }

    @Public
    // [AUDIT-OK] OF-added @Public method isAnimationActive, not in baseline
    public boolean isAnimationActive() {
        return this.animationActive;
    }

    @Public
    // [AUDIT-OK] OF-added @Public method getSpriteU16, not in baseline
    public double getSpriteU16(float u) {
        float f = this.maxU - this.minU;
        return (double) ((u - this.minU) / f * 16.0F);
    }

    @Public
    // [AUDIT-OK] OF-added @Public method getSpriteV16, not in baseline
    public double getSpriteV16(float v) {
        float f = this.maxV - this.minV;
        return (double) ((v - this.minV) / f * 16.0F);
    }

    @Public
    // [AUDIT-OK] OF-added @Public method bindSpriteTexture, not in baseline
    public void bindSpriteTexture() {
        if (this.glSpriteTextureId < 0) {
            this.glSpriteTextureId = TextureUtil.glGenTextures();
            TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
            TextureUtils.applyAnisotropicLevel();
        }
        TextureUtils.bindTexture(this.glSpriteTextureId);
    }

    @Public
    // [AUDIT-OK] OF-added @Public method deleteSpriteTexture, not in baseline
    public void deleteSpriteTexture() {
        if (this.glSpriteTextureId >= 0) {
            TextureUtil.deleteTexture(this.glSpriteTextureId);
            this.glSpriteTextureId = -1;
        }
    }

    @Public
    // [AUDIT-OK] OF-added @Public method toSingleU, not in baseline
    public float toSingleU(float u) {
        u -= this.baseU;
        float f = (float) this.sheetWidth / this.width;
        return u * f;
    }

    @Public
    // [AUDIT-OK] OF-added @Public method toSingleV, not in baseline
    public float toSingleV(float v) {
        v -= this.baseV;
        float f = (float) this.sheetHeight / this.height;
        return v * f;
    }

    @Public
    // [AUDIT-OK] OF-added @Public method getFramesTextureData() (no-arg), not in baseline
    public List<int[][]> getFramesTextureData() {
        ArrayList<int[][]> arraylist = new ArrayList<>();
        arraylist.addAll(this.framesTextureData);
        return arraylist;
    }

    @Public
    // [AUDIT-OK] OF-added @Public method getAnimationMetadata, not in baseline
    public AnimationMetadataSection getAnimationMetadata() {
        return this.animationMetadata;
    }

    @Public
    // [AUDIT-OK] OF-added @Public method setAnimationMetadata, not in baseline
    public void setAnimationMetadata(AnimationMetadataSection animationMetadata) {
        this.animationMetadata = animationMetadata;
    }

    @Unique
    private void optiRefine$initializeSpriteState() {
        this.indexInMap = -1;
        this.glSpriteTextureId = -1;
        this.spriteSingle = null;
        this.isSpriteSingle = false;
        this.mipmapLevels = 0;
        this.spriteNormal = null;
        this.spriteSpecular = null;
        this.isShadersSprite = false;
        this.isDependencyParent = false;
        this.isEmissive = false;
        this.spriteEmissive = null;
        this.animationIndex = -1;
        this.animationActive = false;
    }
}
