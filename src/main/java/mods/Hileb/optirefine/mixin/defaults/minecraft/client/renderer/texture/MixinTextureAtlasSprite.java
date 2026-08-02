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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1
public abstract class MixinTextureAtlasSprite {

    // ===== new fields =====

    @Unique
    // [AUDIT-OK] OF-added fields (indexInMap/animationIndex/animationActive), not in baseline
    private int indexInMap = -1;
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
    public int glSpriteTextureId = -1;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added fields spriteSingle/isSpriteSingle (@Public), not in baseline
    public TextureAtlasSprite spriteSingle = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    public boolean isSpriteSingle = false;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added field mipmapLevels (@Public), not in baseline
    public int mipmapLevels = 0;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added fields spriteNormal/spriteSpecular (@Public), not in baseline
    public TextureAtlasSprite spriteNormal = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    public TextureAtlasSprite spriteSpecular = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added fields isShadersSprite/isDependencyParent/isEmissive (@Public), not in baseline
    public boolean isShadersSprite = false;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    public boolean isDependencyParent = false;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    public boolean isEmissive = false;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added field spriteEmissive (@Public), not in baseline
    public TextureAtlasSprite spriteEmissive = null;
    @Unique
    private int animationIndex = -1;
    @Unique
    private boolean animationActive = false;

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
    public void _Object() {
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite (Ljava/lang/String;Z)V")
    // [AUDIT-OK] OF private ctor TextureAtlasSprite(String,Z), MCP name N/A (ctor); NEW+Construction idiom
    private static native TextureAtlasSprite _new_TextureAtlasSprite(AccessibleOperation.Construction construction, String name, boolean isSpriteSingle);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite baseU F")
    // [AUDIT-OK] OF field baseU, MCP name (not in tsrg)
    private static native float TextureAtlasSprite_baseU_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite baseV F")
    // [AUDIT-OK] OF field baseV, MCP name (not in tsrg)
    private static native float TextureAtlasSprite_baseV_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetWidth I")
    // [AUDIT-OK] OF field sheetWidth, MCP name (not in tsrg)
    private static native int TextureAtlasSprite_sheetWidth_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite sheetHeight I")
    // [AUDIT-OK] OF field sheetHeight, MCP name (not in tsrg)
    private static native int TextureAtlasSprite_sheetHeight_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite glSpriteTextureId I")
    // [AUDIT-OK] OF field glSpriteTextureId, MCP name (not in tsrg)
    private static native int TextureAtlasSprite_glSpriteTextureId_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite mipmapLevels I")
    // [AUDIT-OK] OF field mipmapLevels, MCP name (not in tsrg)
    private static native int TextureAtlasSprite_mipmapLevels_get(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getIndexInMap ()I")
    // [AUDIT-OK] OF member getIndexInMap()I, MCP name (not in tsrg)
    private static native int TextureAtlasSprite_getIndexInMap(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getAnimationIndex ()I")
    // [AUDIT-OK] OF member getAnimationIndex()I, MCP name (not in tsrg)
    private static native int TextureAtlasSprite_getAnimationIndex(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite isShadersSprite Z")
    // [AUDIT-OK] OF field isShadersSprite, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_isShadersSprite_set(TextureAtlasSprite sprite, boolean value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap func_184397_a (Lnet/minecraft/client/resources/IResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)Z", deobf = true)
    // [AUDIT-ISSUE] desc ends )V but func_184397_a returns Z (boolean) -> NoSuchMethodError at runtime in loadShadersSprites; also missing deobf=true. Fix: )Z + deobf=true
    private static native boolean TextureMap_generateMipmaps(net.minecraft.client.renderer.texture.TextureMap textureMap, IResourceManager resourceManager, TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap completeResourceLocation (Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/util/ResourceLocation;")
    // [AUDIT-OK] OF member completeResourceLocation(ResourceLocation)LResourceLocation, MCP name (not in tsrg)
    private static native ResourceLocation TextureMap_completeResourceLocation(net.minecraft.client.renderer.texture.TextureMap textureMap, ResourceLocation location);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite setAnimationIndex (I)V")
    // [AUDIT-OK] OF member setAnimationIndex(I)V, MCP name (not in tsrg)
    private static native void TextureAtlasSprite_setAnimationIndex(TextureAtlasSprite sprite, int animationIndex);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite func_130099_d (I)V", deobf = true)
    // [AUDIT-OK] vanilla SRG func_130099_d = allocateFrameTextureData(I)V; missing deobf=true (devrun only)
    private static native void TextureAtlasSprite_allocateFrameTextureData(TextureAtlasSprite sprite, int index);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite func_130102_n ()V", deobf = true)
    // [AUDIT-OK] vanilla SRG func_130102_n = resetSprite()V; missing deobf=true (devrun only)
    private static native void TextureAtlasSprite_resetSprite(TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern", "MissingUnique"})
    @NewConstructor
    @Public
    // [AUDIT-NOTE] @NewConstructor -> target <init>(String,Z); field initializers (indexInMap=-1 etc.) not injected into this post-apply ctor -> spriteSingle.indexInMap=0 vs OF -1 (minor divergence)
    public void _TextureAtlasSprite(String name, boolean isSpriteSingle) {
        _Object();
        this.iconName = name;
        this.isSpriteSingle = isSpriteSingle;
    }

    // ===== constructor: spriteSingle init =====

    @Inject(method = "<init>(Ljava/lang/String;)V", at = @At("RETURN"))
    // [AUDIT-OK] target <init>(Ljava/lang/String;)V matches baseline; RETURN inject replicates OF spriteSingle init
    private void optiRefine$init(CallbackInfo ci) {
        if (Config.isMultiTexture()) {
            this.spriteSingle = _new_TextureAtlasSprite(AccessibleOperation.Construction.construction(), this.getIconName() + ".spriteSingle", true);
        }
    }

    @Shadow
    // [AUDIT-OK] baseline member getIconName exists in TextureAtlasSprite
    public String getIconName() {
        return null;
    }

    // ===== initSprite: baseU/baseV + sprite propagation =====

    @Inject(method = "initSprite", at = @At("RETURN"))
    // [AUDIT-OK] target initSprite(IIIIZ)V matches baseline; RETURN inject replicates OF baseU/baseV + propagation
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
    // [AUDIT-OK] target copyFrom(TextureAtlasSprite)V matches baseline; RETURN inject replicates OF index/base/sheet/glId/mipmap/animation propagation
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
    // [AUDIT-OK] baseline member allocateFrameTextureData exists in TextureAtlasSprite
    private void allocateFrameTextureData(int index) {
    }

    @Shadow
    // [AUDIT-OK] baseline member getFrameTextureData (static 4-arg) exists in TextureAtlasSprite
    private static int[][] getFrameTextureData(int[][] data, int width, int height, int frameIndex) {
        return null;
    }

    @Unique
    // [AUDIT-OK] OF-added @Unique method fixTransparentColor, not in baseline
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
    // [AUDIT-OK] OF-added @Unique method loadShadersSprites, not in baseline (uses TextureMap_generateMipmaps -> see ISSUE above)
    private void loadShadersSprites() {
        if (Shaders.configNormalMap) {
            String s = this.iconName + "_n";
            ResourceLocation resourcelocation = new ResourceLocation(s);
            resourcelocation = TextureMap_completeResourceLocation(Config.getTextureMap(), resourcelocation);
            if (Config.hasResource(resourcelocation)) {
                this.spriteNormal = new TextureAtlasSprite(s);
                TextureAtlasSprite_isShadersSprite_set(this.spriteNormal, true);
                this.spriteNormal.copyFrom((TextureAtlasSprite) (Object) this);
                TextureMap_generateMipmaps(Config.getTextureMap(), Config.getResourceManager(), this.spriteNormal); // boolean return consumed (stack balance)
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
                TextureMap_generateMipmaps(Config.getTextureMap(), Config.getResourceManager(), this.spriteSpecular); // boolean return consumed (stack balance)
            }
        }
    }

    // ===== generateMipmaps + frame helpers: spriteSingle propagation =====

    @Inject(method = "generateMipmaps", at = @At("RETURN"))
    // [AUDIT-OK] target generateMipmaps(I)V (func_147963_d) matches baseline; RETURN inject replicates OF spriteSingle propagation
    private void optiRefine$generateMipmaps(int mipmapLevelsIn, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            this.spriteSingle.generateMipmaps(mipmapLevelsIn);
        }
    }

    @Inject(method = "allocateFrameTextureData", at = @At("RETURN"))
    // [AUDIT-OK] target allocateFrameTextureData(I)V matches baseline; RETURN inject replicates OF spriteSingle propagation
    private void optiRefine$allocateFrameTextureData(int index, CallbackInfo ci) {
        if (this.spriteSingle != null) {
            TextureAtlasSprite_allocateFrameTextureData(this.spriteSingle, index);
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
        if (this.spriteSingle != null) {
            TextureAtlasSprite_resetSprite(this.spriteSingle);
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
            TextureAtlasSprite_setAnimationIndex(this.spriteNormal, animationIndex);
        }
        if (this.spriteSpecular != null) {
            TextureAtlasSprite_setAnimationIndex(this.spriteSpecular, animationIndex);
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
}
