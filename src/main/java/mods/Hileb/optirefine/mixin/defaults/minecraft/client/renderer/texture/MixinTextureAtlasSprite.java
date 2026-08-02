package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.ShadowSuper;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
import net.optifine.util.CounterInt;
import net.optifine.util.TextureUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * OptiFine additions to {@link TextureAtlasSprite}.
 *
 * <p>Implements the OptiFine-specific fields and methods (spriteSingle/spriteNormal/spriteSpecular
 * bookkeeping, SmartAnimations integration, transparent color fixing, shaders sprites, mipmap
 * propagation, custom UV helpers). The {@code hasCustomLoader}/{@code load}/{@code getDependencies}
 * methods and the UV inset change in {@code initSprite} are already provided by the Cleanroom
 * patches and thus skipped here.</p>
 */
@Mixin(TextureAtlasSprite.class)
public abstract class MixinTextureAtlasSprite {

    // ===== new fields =====

    @Unique
    private int indexInMap = -1;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public float baseU;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public float baseV;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public int sheetWidth;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public int sheetHeight;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public int glSpriteTextureId = -1;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public TextureAtlasSprite spriteSingle = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public boolean isSpriteSingle = false;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public int mipmapLevels = 0;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public TextureAtlasSprite spriteNormal = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public TextureAtlasSprite spriteSpecular = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public boolean isShadersSprite = false;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public boolean isDependencyParent = false;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public boolean isEmissive = false;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public TextureAtlasSprite spriteEmissive = null;
    @Unique
    private int animationIndex = -1;
    @Unique
    private boolean animationActive = false;

    // ===== shadowed existing fields =====

    @Shadow
    private String iconName;
    @Shadow
    protected List<int[][]> framesTextureData;
    @Shadow
    protected int[][] interpolatedFrameData;
    @Shadow
    private AnimationMetadataSection animationMetadata;
    @Shadow
    protected int frameCounter;
    @Shadow
    protected int tickCounter;
    @Shadow
    protected int originX;
    @Shadow
    protected int originY;
    @Shadow
    protected int width;
    @Shadow
    protected int height;
    @Shadow
    protected boolean rotated;
    @Shadow
    private float minU;
    @Shadow
    private float maxU;
    @Shadow
    private float minV;
    @Shadow
    private float maxV;

    @ShadowSuper("<init>")
    public void _Object() {
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite (Ljava/lang/String;Z)V")
    private static native TextureAtlasSprite _new_TextureAtlasSprite(AccessibleOperation.Construction construction, String name, boolean isSpriteSingle);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite baseU F")
    private static native float TextureAtlasSprite_baseU_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite baseV F")
    private static native float TextureAtlasSprite_baseV_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetWidth I")
    private static native int TextureAtlasSprite_sheetWidth_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetHeight I")
    private static native int TextureAtlasSprite_sheetHeight_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite glSpriteTextureId I")
    private static native int TextureAtlasSprite_glSpriteTextureId_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite mipmapLevels I")
    private static native int TextureAtlasSprite_mipmapLevels_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getIndexInMap ()I")
    private static native int TextureAtlasSprite_getIndexInMap(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getAnimationIndex ()I")
    private static native int TextureAtlasSprite_getAnimationIndex(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite isShadersSprite Z")
    private static native void TextureAtlasSprite_isShadersSprite_set(TextureAtlasSprite sprite, boolean value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap generateMipmaps (Lnet/minecraft/client/resources/IResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
    private static native void TextureMap_generateMipmaps(net.minecraft.client.renderer.texture.TextureMap textureMap, IResourceManager resourceManager, TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap completeResourceLocation (Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/util/ResourceLocation;")
    private static native ResourceLocation TextureMap_completeResourceLocation(net.minecraft.client.renderer.texture.TextureMap textureMap, ResourceLocation location);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite setAnimationIndex (I)V")
    private static native void TextureAtlasSprite_setAnimationIndex(TextureAtlasSprite sprite, int animationIndex);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite allocateFrameTextureData (I)V")
    private static native void TextureAtlasSprite_allocateFrameTextureData(TextureAtlasSprite sprite, int index);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite resetSprite ()V")
    private static native void TextureAtlasSprite_resetSprite(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern", "MissingUnique"})
    @NewConstructor
    @Public
    public void _TextureAtlasSprite(String name, boolean isSpriteSingle) {
        _Object();
        this.iconName = name;
        this.isSpriteSingle = isSpriteSingle;
    }

    // ===== constructor: spriteSingle init =====

    @Inject(method = "<init>(Ljava/lang/String;)V", at = @At("RETURN"))
    private void optiRefine$init(CallbackInfo ci) {
        if (Config.isMultiTexture()) {
            this.spriteSingle = _new_TextureAtlasSprite(AccessibleOperation.Construction.construction(), this.getIconName() + ".spriteSingle", true);
        }
    }

    @Shadow
    public String getIconName() {
        return null;
    }

    // ===== initSprite: baseU/baseV + sprite propagation =====

    @Inject(method = "initSprite", at = @At("RETURN"))
    private void optiRefine$initSprite(CallbackInfo ci) {
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
    private void optiRefine$copyFrom(TextureAtlasSprite atlasSpirit, CallbackInfo ci) {
        if (atlasSpirit != Config.getTextureMap().getMissingSprite()) {
            this.indexInMap = TextureAtlasSprite_getIndexInMap(atlasSpirit);
        }
        this.baseU = TextureAtlasSprite_baseU_get(atlasSpirit);
        this.baseV = TextureAtlasSprite_baseV_get(atlasSpirit);
        this.sheetWidth = TextureAtlasSprite_sheetWidth_get(atlasSpirit);
        this.sheetHeight = TextureAtlasSprite_sheetHeight_get(atlasSpirit);
        this.glSpriteTextureId = TextureAtlasSprite_glSpriteTextureId_get(atlasSpirit);
        this.mipmapLevels = TextureAtlasSprite_mipmapLevels_get(atlasSpirit);
        if (this.spriteSingle != null) {
            this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
        }
        this.animationIndex = TextureAtlasSprite_getAnimationIndex(atlasSpirit);
    }

    // ===== updateAnimation: SmartAnimations + null guard =====

    /**
     * @author OptiRefine
     * @reason OptiFine: null animation guard + SmartAnimations active tracking
     */
    @WrapMethod(method = "updateAnimation")
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
                    TextureUtil.uploadTextureMipmap(this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, flag, flag1);
                }
            } else if (this.animationMetadata.isInterpolate()) {
                if (!this.animationActive) {
                    return;
                }
                this.updateAnimationInterpolated();
            }
        }
    }

    @Shadow
    private void updateAnimationInterpolated() {
    }

    // ===== setIconWidth/setIconHeight: spriteSingle propagation =====

    @Inject(method = "setIconWidth", at = @At("RETURN"))
    private void optiRefine$setIconWidth(int widthIn, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.setIconWidth(this.width);
        }
    }

    @Inject(method = "setIconHeight", at = @At("RETURN"))
    private void optiRefine$setIconHeight(int heightIn, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.setIconHeight(this.height);
        }
    }

    // ===== loadSprite: spriteSingle size propagation =====

    @Inject(method = "loadSprite", at = @At("RETURN"))
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
    private void optiRefine$loadSpriteFrames(IResource resource, int mipmapLevelsIn, Operation<Void> original) throws java.io.IOException {
        java.awt.image.BufferedImage bufferedimage = TextureUtil.readBufferedImage(resource.getInputStream());
        if (this.width != bufferedimage.getWidth()) {
            bufferedimage = TextureUtils.scaleImage(bufferedimage, this.width);
        }
        AnimationMetadataSection animationmetadatasection = resource.getMetadata("animation");
        int[][] aint = new int[mipmapLevelsIn][];
        aint[0] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
        bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[0], 0, bufferedimage.getWidth());
        if (animationmetadatasection == null) {
            this.framesTextureData.add(aint);
        } else {
            int i = bufferedimage.getHeight() / this.width;
            int j;
            if (animationmetadatasection.getFrameCount() > 0) {
                for (j = 0; j < animationmetadatasection.getFrameCount(); ++j) {
                    int k = animationmetadatasection.getFrameIndex(j);
                    if (k >= i) {
                        throw new RuntimeException("invalid frameindex " + k);
                    }
                    this.allocateFrameTextureData(k);
                    this.framesTextureData.set(k, getFrameTextureData(aint, this.width, this.width, k));
                }
                this.animationMetadata = animationmetadatasection;
            } else {
                ArrayList<AnimationFrame> arraylist = new ArrayList<>();
                for (j = 0; j < i; ++j) {
                    this.framesTextureData.add(getFrameTextureData(aint, this.width, this.width, j));
                    arraylist.add(new AnimationFrame(j, -1));
                }
                this.animationMetadata = new AnimationMetadataSection(arraylist, this.width, this.height, animationmetadatasection.getFrameTime(), animationmetadatasection.isInterpolate());
            }
        }

        if (!this.isShadersSprite) {
            if (Config.isShaders()) {
                this.loadShadersSprites();
            }
            for (int l = 0; l < this.framesTextureData.size(); ++l) {
                int[][] aint1 = this.framesTextureData.get(l);
                if (aint1 != null && !this.iconName.startsWith("minecraft:blocks/leaves_")) {
                    for (int i1 = 0; i1 < aint1.length; ++i1) {
                        int[] aint2 = aint1[i1];
                        this.fixTransparentColor(aint2);
                    }
                }
            }
            if (this.spriteSingle != null) {
                IResource iresource = Config.getResourceManager().getResource(resource.getResourceLocation());
                this.spriteSingle.loadSpriteFrames(iresource, mipmapLevelsIn);
            }
        }
    }

    @Shadow
    private void allocateFrameTextureData(int index) {
    }

    @Shadow
    private static int[][] getFrameTextureData(int[][] data, int width, int height, int frameIndex) {
        return null;
    }

    @Unique
    private void fixTransparentColor(int[] data) {
        if (data != null) {
            long l1 = 0L;
            long l2 = 0L;
            long l3 = 0L;
            long l4 = 0L;
            for (int i = 0; i < data.length; ++i) {
                int j = data[i];
                int k = j >> 24 & 255;
                if (k >= 16) {
                    int l = j >> 16 & 255;
                    int i1 = j >> 8 & 255;
                    int j1 = j & 255;
                    l1 += l;
                    l2 += i1;
                    l3 += j1;
                    ++l4;
                }
            }
            if (l4 > 0L) {
                int k1 = (int) (l1 / l4);
                int l5 = (int) (l2 / l4);
                int i2 = (int) (l3 / l4);
                int j2 = k1 << 16 | l5 << 8 | i2;
                for (int k2 = 0; k2 < data.length; ++k2) {
                    int l6 = data[k2];
                    int i3 = l6 >> 24 & 255;
                    if (i3 <= 16) {
                        data[k2] = j2;
                    }
                }
            }
        }
    }

    @Unique
    private void loadShadersSprites() {
        if (Shaders.configNormalMap) {
            String s = this.iconName + "_n";
            ResourceLocation resourcelocation = new ResourceLocation(s);
            resourcelocation = TextureMap_completeResourceLocation(Config.getTextureMap(), resourcelocation);
            if (Config.hasResource(resourcelocation)) {
                this.spriteNormal = new TextureAtlasSprite(s);
                TextureAtlasSprite_isShadersSprite_set(this.spriteNormal, true);
                this.spriteNormal.copyFrom((TextureAtlasSprite) (Object) this);
                TextureMap_generateMipmaps(Config.getTextureMap(), Config.getResourceManager(), this.spriteNormal);
            }
        }
        if (Shaders.configSpecularMap) {
            String s1 = this.iconName + "_s";
            ResourceLocation resourcelocation1 = new ResourceLocation(s1);
            resourcelocation1 = TextureMap_completeResourceLocation(Config.getTextureMap(), resourcelocation1);
            if (Config.hasResource(resourcelocation1)) {
                this.spriteSpecular = new TextureAtlasSprite(s1);
                TextureAtlasSprite_isShadersSprite_set(this.spriteSpecular, true);
                this.spriteSpecular.copyFrom((TextureAtlasSprite) (Object) this);
                TextureMap_generateMipmaps(Config.getTextureMap(), Config.getResourceManager(), this.spriteSpecular);
            }
        }
    }

    // ===== generateMipmaps + frame helpers: spriteSingle propagation =====

    @Inject(method = "generateMipmaps", at = @At("RETURN"))
    private void optiRefine$generateMipmaps(int mipmapLevelsIn, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.generateMipmaps(mipmapLevelsIn);
        }
    }

    @Inject(method = "allocateFrameTextureData", at = @At("RETURN"))
    private void optiRefine$allocateFrameTextureData(int index, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            TextureAtlasSprite_allocateFrameTextureData(this.spriteSingle, index);
        }
    }

    @Inject(method = "clearFramesTextureData", at = @At("RETURN"))
    private void optiRefine$clearFramesTextureData(CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.clearFramesTextureData();
        }
    }

    @Inject(method = "setFramesTextureData", at = @At("RETURN"))
    private void optiRefine$setFramesTextureData(List<int[][]> newFramesTextureData, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.setFramesTextureData(newFramesTextureData);
        }
    }

    @Inject(method = "resetSprite", at = @At("RETURN"))
    private void optiRefine$resetSprite(CallbackInfo ci) {
        if (this.spriteSingle != null) {
            TextureAtlasSprite_resetSprite(this.spriteSingle);
        }
    }

    @Shadow
    private void resetSprite() {
    }

    // ===== new public methods =====

    @Unique @Public
    public int getIndexInMap() {
        return this.indexInMap;
    }

    @Unique @Public
    public void setIndexInMap(int indexInMap) {
        this.indexInMap = indexInMap;
    }

    @Unique @Public
    public void updateIndexInMap(CounterInt counterInt) {
        if (this.indexInMap < 0) {
            this.indexInMap = counterInt.nextValue();
        }
    }

    @Unique @Public
    public int getAnimationIndex() {
        return this.animationIndex;
    }

    @Unique @Public
    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
        if (this.spriteNormal != null) {
            TextureAtlasSprite_setAnimationIndex(this.spriteNormal, animationIndex);
        }
        if (this.spriteSpecular != null) {
            TextureAtlasSprite_setAnimationIndex(this.spriteSpecular, animationIndex);
        }
    }

    @Unique @Public
    public boolean isAnimationActive() {
        return this.animationActive;
    }

    @Unique @Public
    public double getSpriteU16(float u) {
        float f = this.maxU - this.minU;
        return (double) ((u - this.minU) / f * 16.0F);
    }

    @Unique @Public
    public double getSpriteV16(float v) {
        float f = this.maxV - this.minV;
        return (double) ((v - this.minV) / f * 16.0F);
    }

    @Unique @Public
    public void bindSpriteTexture() {
        if (this.glSpriteTextureId < 0) {
            this.glSpriteTextureId = TextureUtil.glGenTextures();
            TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
            TextureUtils.applyAnisotropicLevel();
        }
        TextureUtils.bindTexture(this.glSpriteTextureId);
    }

    @Unique @Public
    public void deleteSpriteTexture() {
        if (this.glSpriteTextureId >= 0) {
            TextureUtil.deleteTexture(this.glSpriteTextureId);
            this.glSpriteTextureId = -1;
        }
    }

    @Unique @Public
    public float toSingleU(float u) {
        u -= this.baseU;
        float f = (float) this.sheetWidth / this.width;
        return u * f;
    }

    @Unique @Public
    public float toSingleV(float v) {
        v -= this.baseV;
        float f = (float) this.sheetHeight / this.height;
        return v * f;
    }

    @Unique @Public
    public List<int[][]> getFramesTextureData() {
        ArrayList<int[][]> arraylist = new ArrayList<>();
        arraylist.addAll(this.framesTextureData);
        return arraylist;
    }

    @Unique @Public
    public AnimationMetadataSection getAnimationMetadata() {
        return this.animationMetadata;
    }

    @Unique @Public
    public void setAnimationMetadata(AnimationMetadataSection animationMetadata) {
        this.animationMetadata = animationMetadata;
    }
}
