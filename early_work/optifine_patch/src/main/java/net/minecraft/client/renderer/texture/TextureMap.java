/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  java.awt.Dimension
 *  java.awt.image.BufferedImage
 *  java.io.Closeable
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  java.util.TreeSet
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.ITextureMapPopulator
 *  net.minecraft.client.renderer.texture.ITickableTextureObject
 *  net.minecraft.client.renderer.texture.PngSizeInfo
 *  net.minecraft.client.renderer.texture.Stitcher
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  net.optifine.BetterGrass
 *  net.optifine.ConnectedTextures
 *  net.optifine.CustomItems
 *  net.optifine.EmissiveTextures
 *  net.optifine.SmartAnimations
 *  net.optifine.SpriteDependencies
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorForge
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.shaders.ShadersTex
 *  net.optifine.util.CounterInt
 *  net.optifine.util.TextureUtils
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.ShadersTex;
import net.optifine.util.CounterInt;
import net.optifine.util.TextureUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureMap
extends AbstractTexture
implements ITickableTextureObject {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    public static final ResourceLocation LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
    private final List<TextureAtlasSprite> listAnimatedSprites;
    private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
    private final Map<String, TextureAtlasSprite> mapUploadedSprites;
    private final String basePath;
    private final ITextureMapPopulator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage;
    private TextureAtlasSprite[] iconGrid = null;
    private int iconGridSize = -1;
    private int iconGridCountX = -1;
    private int iconGridCountY = -1;
    private double iconGridSizeU = -1.0;
    private double iconGridSizeV = -1.0;
    private CounterInt counterIndexInMap = new CounterInt(0);
    public int atlasWidth = 0;
    public int atlasHeight = 0;
    private int countAnimationsActive;
    private int frameCountAnimations;

    public TextureMap(String basePathIn) {
        this(basePathIn, null);
    }

    public TextureMap(String basePathIn, boolean skipFirst) {
        this(basePathIn, null, skipFirst);
    }

    public TextureMap(String basePathIn, @Nullable ITextureMapPopulator iconCreatorIn) {
        this(basePathIn, iconCreatorIn, false);
    }

    public TextureMap(String basePathIn, ITextureMapPopulator iconCreatorIn, boolean skipFirst) {
        this.listAnimatedSprites = Lists.newArrayList();
        this.mapRegisteredSprites = Maps.newHashMap();
        this.mapUploadedSprites = Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = basePathIn;
        this.iconCreator = iconCreatorIn;
    }

    private void initMissingImage() {
        int size = this.getMinSpriteSize();
        int[] aint = this.getMissingImageData(size);
        this.missingImage.setIconWidth(size);
        this.missingImage.setIconHeight(size);
        int[][] aint1 = new int[this.mipmapLevels + 1][];
        aint1[0] = aint;
        this.missingImage.setFramesTextureData((List)Lists.newArrayList((Object[])new int[][][]{aint1}));
        this.missingImage.setIndexInMap(this.counterIndexInMap.nextValue());
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        if (this.iconCreator != null) {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager resourceManager, ITextureMapPopulator iconCreatorIn) {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap.reset();
        Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_onTextureStitchedPre, (Object[])new Object[]{this});
        iconCreatorIn.registerSprites(this);
        if (this.mipmapLevels >= 4) {
            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
            Config.log((String)("Mipmap levels: " + this.mipmapLevels));
        }
        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    public void loadTextureAtlas(IResourceManager resourceManager) {
        int l2;
        int i1;
        int minSpriteSize;
        Config.dbg((String)("Multitexture: " + Config.isMultiTexture()));
        if (Config.isMultiTexture()) {
            for (TextureAtlasSprite ts : this.mapUploadedSprites.values()) {
                ts.deleteSpriteTexture();
            }
        }
        ConnectedTextures.updateIcons((TextureMap)this);
        CustomItems.updateIcons((TextureMap)this);
        BetterGrass.updateIcons((TextureMap)this);
        int i = TextureUtils.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(i, i, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int j = Integer.MAX_VALUE;
        this.iconGridSize = minSpriteSize = this.getMinSpriteSize();
        int k = 1 << this.mipmapLevels;
        int countCustomLoader = 0;
        int countCustomLoaderSkipped = 0;
        SpriteDependencies.reset();
        ArrayList listRegisteredSprites = new ArrayList(this.mapRegisteredSprites.values());
        for (int ix = 0; ix < listRegisteredSprites.size(); ++ix) {
            ResourceLocation resourcelocation;
            TextureAtlasSprite textureatlassprite;
            block39: {
                textureatlassprite = SpriteDependencies.resolveDependencies((List)listRegisteredSprites, (int)ix, (TextureMap)this);
                resourcelocation = this.getResourceLocation(textureatlassprite);
                IResource iresource = null;
                textureatlassprite.updateIndexInMap(this.counterIndexInMap);
                if (textureatlassprite.hasCustomLoader(resourceManager, resourcelocation)) {
                    if (textureatlassprite.load(resourceManager, resourcelocation, l -> (TextureAtlasSprite)this.mapRegisteredSprites.get((Object)l.toString()))) {
                        Config.detail((String)("Custom loader (skipped): " + textureatlassprite));
                        ++countCustomLoaderSkipped;
                        continue;
                    }
                    Config.detail((String)("Custom loader: " + textureatlassprite));
                    ++countCustomLoader;
                } else {
                    PngSizeInfo pngsizeinfo = PngSizeInfo.makeFromResource((IResource)resourceManager.getResource(resourcelocation));
                    iresource = resourceManager.getResource(resourcelocation);
                    boolean flag = iresource.getMetadata("animation") != null;
                    textureatlassprite.loadSprite(pngsizeinfo, flag);
                    IOUtils.closeQuietly((Closeable)iresource);
                }
                break block39;
                catch (RuntimeException runtimeexception) {
                    LOGGER.error("Unable to parse metadata from {}", (Object)resourcelocation, (Object)runtimeexception);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture((ResourceLocation)resourcelocation, (String)runtimeexception.getMessage());
                    IOUtils.closeQuietly((Closeable)iresource);
                    continue;
                }
                catch (IOException ioexception) {
                    LOGGER.error("Using missing texture, unable to load " + resourcelocation + ", " + ioexception.getClass().getName());
                    ReflectorForge.FMLClientHandler_trackMissingTexture((ResourceLocation)resourcelocation);
                    {
                        catch (Throwable throwable) {
                            IOUtils.closeQuietly(iresource);
                            throw throwable;
                        }
                    }
                    IOUtils.closeQuietly((Closeable)iresource);
                    continue;
                }
            }
            int ws = textureatlassprite.getIconWidth();
            int hs = textureatlassprite.getIconHeight();
            if (ws < 1 || hs < 1) {
                Config.warn((String)("Invalid sprite size: " + textureatlassprite));
                continue;
            }
            if (ws < minSpriteSize || this.mipmapLevels > 0) {
                int ws2;
                int n = ws2 = this.mipmapLevels > 0 ? TextureUtils.scaleToGrid((int)ws, (int)minSpriteSize) : TextureUtils.scaleToMin((int)ws, (int)minSpriteSize);
                if (ws2 != ws) {
                    if (!TextureUtils.isPowerOfTwo((int)ws)) {
                        Config.log((String)("Scaled non power of 2: " + textureatlassprite.getIconName() + ", " + ws + " -> " + ws2));
                    } else {
                        Config.log((String)("Scaled too small texture: " + textureatlassprite.getIconName() + ", " + ws + " -> " + ws2));
                    }
                    int hs2 = hs * ws2 / ws;
                    textureatlassprite.setIconWidth(ws2);
                    textureatlassprite.setIconHeight(hs2);
                }
            }
            j = Math.min((int)j, (int)Math.min((int)textureatlassprite.getIconWidth(), (int)textureatlassprite.getIconHeight()));
            int j1 = Math.min((int)Integer.lowestOneBit((int)textureatlassprite.getIconWidth()), (int)Integer.lowestOneBit((int)textureatlassprite.getIconHeight()));
            if (j1 < k) {
                LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", (Object)resourcelocation, (Object)textureatlassprite.getIconWidth(), (Object)textureatlassprite.getIconHeight(), (Object)MathHelper.log2((int)k), (Object)MathHelper.log2((int)j1));
                k = j1;
            }
            if (!this.generateMipmaps(resourceManager, textureatlassprite)) continue;
            stitcher.addSprite(textureatlassprite);
        }
        if (countCustomLoader > 0) {
            Config.dbg((String)("Custom loader sprites: " + countCustomLoader));
        }
        if (countCustomLoaderSkipped > 0) {
            Config.dbg((String)("Custom loader sprites (skipped): " + countCustomLoaderSkipped));
        }
        if (SpriteDependencies.getCountDependencies() > 0) {
            Config.dbg((String)("Sprite dependencies: " + SpriteDependencies.getCountDependencies()));
        }
        if ((i1 = MathHelper.log2((int)(l2 = Math.min((int)j, (int)k)))) < 0) {
            i1 = 0;
        }
        if (i1 < this.mipmapLevels) {
            LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", (Object)this.basePath, (Object)this.mipmapLevels, (Object)i1, (Object)l2);
            this.mipmapLevels = i1;
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        stitcher.doStitch();
        LOGGER.info("Created: {}x{} {}-atlas", (Object)stitcher.getCurrentWidth(), (Object)stitcher.getCurrentHeight(), (Object)this.basePath);
        if (Config.isShaders()) {
            ShadersTex.allocateTextureMap((int)this.getGlTextureId(), (int)this.mipmapLevels, (int)stitcher.getCurrentWidth(), (int)stitcher.getCurrentHeight(), (Stitcher)stitcher, (TextureMap)this);
        } else {
            TextureUtil.allocateTextureImpl((int)this.getGlTextureId(), (int)this.mipmapLevels, (int)stitcher.getCurrentWidth(), (int)stitcher.getCurrentHeight());
        }
        HashMap map = Maps.newHashMap(this.mapRegisteredSprites);
        for (TextureAtlasSprite textureatlassprite1 : stitcher.getStichSlots()) {
            String s = textureatlassprite1.getIconName();
            map.remove((Object)s);
            this.mapUploadedSprites.put((Object)s, (Object)textureatlassprite1);
            try {
                if (Config.isShaders()) {
                    ShadersTex.uploadTexSubForLoadAtlas((TextureMap)this, (String)textureatlassprite1.getIconName(), (int[][])textureatlassprite1.getFrameTextureData(0), (int)textureatlassprite1.getIconWidth(), (int)textureatlassprite1.getIconHeight(), (int)textureatlassprite1.getOriginX(), (int)textureatlassprite1.getOriginY(), (boolean)false, (boolean)false);
                } else {
                    TextureUtil.uploadTextureMipmap((int[][])textureatlassprite1.getFrameTextureData(0), (int)textureatlassprite1.getIconWidth(), (int)textureatlassprite1.getIconHeight(), (int)textureatlassprite1.getOriginX(), (int)textureatlassprite1.getOriginY(), (boolean)false, (boolean)false);
                }
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Stitching texture atlas");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Texture being stitched together");
                crashreportcategory.addCrashSection("Atlas path", (Object)this.basePath);
                crashreportcategory.addCrashSection("Sprite", (Object)textureatlassprite1);
                throw new ReportedException(crashreport);
            }
            if (!textureatlassprite1.hasAnimationMetadata()) continue;
            textureatlassprite1.setAnimationIndex(this.listAnimatedSprites.size());
            this.listAnimatedSprites.add((Object)textureatlassprite1);
        }
        for (TextureAtlasSprite textureatlassprite2 : map.values()) {
            textureatlassprite2.copyFrom(this.missingImage);
        }
        Config.log((String)("Animated sprites: " + this.listAnimatedSprites.size()));
        if (Config.isMultiTexture()) {
            int sheetWidth = stitcher.getCurrentWidth();
            int sheetHeight = stitcher.getCurrentHeight();
            List listSprites = stitcher.getStichSlots();
            for (TextureAtlasSprite tas : listSprites) {
                tas.sheetWidth = sheetWidth;
                tas.sheetHeight = sheetHeight;
                tas.mipmapLevels = this.mipmapLevels;
                TextureAtlasSprite ss = tas.spriteSingle;
                if (ss == null) continue;
                if (ss.getIconWidth() <= 0) {
                    ss.setIconWidth(tas.getIconWidth());
                    ss.setIconHeight(tas.getIconHeight());
                    ss.initSprite(tas.getIconWidth(), tas.getIconHeight(), 0, 0, false);
                    ss.clearFramesTextureData();
                    List frameDatas = tas.getFramesTextureData();
                    ss.setFramesTextureData(frameDatas);
                    ss.setAnimationMetadata(tas.getAnimationMetadata());
                }
                ss.sheetWidth = sheetWidth;
                ss.sheetHeight = sheetHeight;
                ss.mipmapLevels = this.mipmapLevels;
                ss.setAnimationIndex(tas.getAnimationIndex());
                tas.bindSpriteTexture();
                boolean texBlur = false;
                boolean texClamp = true;
                try {
                    TextureUtil.uploadTextureMipmap((int[][])ss.getFrameTextureData(0), (int)ss.getIconWidth(), (int)ss.getIconHeight(), (int)ss.getOriginX(), (int)ss.getOriginY(), (boolean)texBlur, (boolean)texClamp);
                }
                catch (Exception e) {
                    Config.dbg((String)("Error uploading sprite single: " + ss + ", parent: " + tas));
                    e.printStackTrace();
                }
            }
            Config.getMinecraft().getTextureManager().bindTexture(LOCATION_BLOCKS_TEXTURE);
        }
        Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_onTextureStitchedPost, (Object[])new Object[]{this});
        this.updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        if (Config.equals((Object)System.getProperty((String)"saveTextureMap"), (Object)"true")) {
            Config.dbg((String)("Exporting texture map: " + this.basePath));
            TextureUtils.saveGlTexture((String)("debug/" + this.basePath.replaceAll("/", "_")), (int)this.getGlTextureId(), (int)this.mipmapLevels, (int)stitcher.getCurrentWidth(), (int)stitcher.getCurrentHeight());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean generateMipmaps(IResourceManager resourceManager, TextureAtlasSprite texture) {
        block11: {
            ResourceLocation resourcelocation = this.getResourceLocation(texture);
            IResource iresource = null;
            if (texture.hasCustomLoader(resourceManager, resourcelocation)) {
                TextureUtils.generateCustomMipmaps((TextureAtlasSprite)texture, (int)this.mipmapLevels);
            } else {
                boolean flag;
                try {
                    iresource = resourceManager.getResource(resourcelocation);
                    texture.loadSpriteFrames(iresource, this.mipmapLevels + 1);
                    break block11;
                }
                catch (RuntimeException runtimeexception) {
                    LOGGER.error("Unable to parse metadata from {}", (Object)resourcelocation, (Object)runtimeexception);
                    flag = false;
                }
                catch (IOException ioexception) {
                    boolean flag2;
                    LOGGER.error("Using missing texture, unable to load {}", (Object)resourcelocation, (Object)ioexception);
                    boolean bl = flag2 = false;
                    return bl;
                }
                finally {
                    IOUtils.closeQuietly((Closeable)iresource);
                }
                return flag;
            }
        }
        try {
            texture.generateMipmaps(this.mipmapLevels);
            return true;
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Applying mipmap");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
            crashreportcategory.addDetail("Sprite name", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
            crashreportcategory.addDetail("Sprite size", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
            crashreportcategory.addDetail("Sprite frames", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
            crashreportcategory.addCrashSection("Mipmap levels", (Object)this.mipmapLevels);
            throw new ReportedException(crashreport);
        }
    }

    public ResourceLocation getResourceLocation(TextureAtlasSprite p_184396_1_) {
        ResourceLocation resourcelocation = new ResourceLocation(p_184396_1_.getIconName());
        return this.completeResourceLocation(resourcelocation);
    }

    public ResourceLocation completeResourceLocation(ResourceLocation resourcelocation) {
        if (this.isAbsoluteLocation(resourcelocation)) {
            return new ResourceLocation(resourcelocation.getNamespace(), resourcelocation.getPath() + ".png");
        }
        return new ResourceLocation(resourcelocation.getNamespace(), String.format((String)"%s/%s%s", (Object[])new Object[]{this.basePath, resourcelocation.getPath(), ".png"}));
    }

    public TextureAtlasSprite getAtlasSprite(String iconName) {
        TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.mapUploadedSprites.get((Object)iconName);
        if (textureatlassprite == null) {
            textureatlassprite = this.missingImage;
        }
        return textureatlassprite;
    }

    public void updateAnimations() {
        int frameCount;
        boolean hasNormal = false;
        boolean hasSpecular = false;
        TextureUtil.bindTexture((int)this.getGlTextureId());
        int countActive = 0;
        for (TextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
            if (!this.isTerrainAnimationActive(textureatlassprite)) continue;
            textureatlassprite.updateAnimation();
            if (textureatlassprite.isAnimationActive()) {
                ++countActive;
            }
            if (textureatlassprite.spriteNormal != null) {
                hasNormal = true;
            }
            if (textureatlassprite.spriteSpecular == null) continue;
            hasSpecular = true;
        }
        if (Config.isMultiTexture()) {
            for (TextureAtlasSprite ts : this.listAnimatedSprites) {
                TextureAtlasSprite spriteSingle;
                if (!this.isTerrainAnimationActive(ts) || (spriteSingle = ts.spriteSingle) == null) continue;
                if (ts == TextureUtils.iconClock || ts == TextureUtils.iconCompass) {
                    spriteSingle.frameCounter = ts.frameCounter;
                }
                ts.bindSpriteTexture();
                spriteSingle.updateAnimation();
                if (!spriteSingle.isAnimationActive()) continue;
                ++countActive;
            }
            TextureUtil.bindTexture((int)this.getGlTextureId());
        }
        if (Config.isShaders()) {
            if (hasNormal) {
                TextureUtil.bindTexture((int)this.getMultiTexID().norm);
                for (TextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
                    if (textureatlassprite.spriteNormal == null || !this.isTerrainAnimationActive(textureatlassprite)) continue;
                    if (textureatlassprite == TextureUtils.iconClock || textureatlassprite == TextureUtils.iconCompass) {
                        textureatlassprite.spriteNormal.frameCounter = textureatlassprite.frameCounter;
                    }
                    textureatlassprite.spriteNormal.updateAnimation();
                    if (!textureatlassprite.spriteNormal.isAnimationActive()) continue;
                    ++countActive;
                }
            }
            if (hasSpecular) {
                TextureUtil.bindTexture((int)this.getMultiTexID().spec);
                for (TextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
                    if (textureatlassprite.spriteSpecular == null || !this.isTerrainAnimationActive(textureatlassprite)) continue;
                    if (textureatlassprite == TextureUtils.iconClock || textureatlassprite == TextureUtils.iconCompass) {
                        textureatlassprite.spriteNormal.frameCounter = textureatlassprite.frameCounter;
                    }
                    textureatlassprite.spriteSpecular.updateAnimation();
                    if (!textureatlassprite.spriteSpecular.isAnimationActive()) continue;
                    ++countActive;
                }
            }
            if (hasNormal || hasSpecular) {
                TextureUtil.bindTexture((int)this.getGlTextureId());
            }
        }
        if ((frameCount = Config.getMinecraft().entityRenderer.frameCount) != this.frameCountAnimations) {
            this.countAnimationsActive = countActive;
            this.frameCountAnimations = frameCount;
        }
        if (SmartAnimations.isActive()) {
            SmartAnimations.resetSpritesRendered();
        }
    }

    public TextureAtlasSprite registerSprite(ResourceLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.mapRegisteredSprites.get((Object)location.toString());
        if (textureatlassprite == null) {
            textureatlassprite = TextureAtlasSprite.makeAtlasSprite((ResourceLocation)location);
            this.mapRegisteredSprites.put((Object)location.toString(), (Object)textureatlassprite);
            textureatlassprite.updateIndexInMap(this.counterIndexInMap);
            if (Config.isEmissiveTextures()) {
                this.checkEmissive(location, textureatlassprite);
            }
        }
        return textureatlassprite;
    }

    public void tick() {
        this.updateAnimations();
    }

    public void setMipmapLevels(int mipmapLevelsIn) {
        this.mipmapLevels = mipmapLevelsIn;
    }

    public TextureAtlasSprite getMissingSprite() {
        return this.missingImage;
    }

    @Nullable
    public TextureAtlasSprite getTextureExtry(String name) {
        return (TextureAtlasSprite)this.mapRegisteredSprites.get((Object)name);
    }

    public boolean setTextureEntry(TextureAtlasSprite entry) {
        String name = entry.getIconName();
        if (!this.mapRegisteredSprites.containsKey((Object)name)) {
            this.mapRegisteredSprites.put((Object)name, (Object)entry);
            entry.updateIndexInMap(this.counterIndexInMap);
            return true;
        }
        return false;
    }

    public String getBasePath() {
        return this.basePath;
    }

    public int getMipmapLevels() {
        return this.mipmapLevels;
    }

    private boolean isAbsoluteLocation(ResourceLocation loc) {
        String path = loc.getPath();
        return this.isAbsoluteLocationPath(path);
    }

    private boolean isAbsoluteLocationPath(String resPath) {
        String path = resPath.toLowerCase();
        return path.startsWith("mcpatcher/") || path.startsWith("optifine/");
    }

    public TextureAtlasSprite getSpriteSafe(String name) {
        ResourceLocation loc = new ResourceLocation(name);
        return (TextureAtlasSprite)this.mapRegisteredSprites.get((Object)loc.toString());
    }

    public TextureAtlasSprite getRegisteredSprite(ResourceLocation loc) {
        return (TextureAtlasSprite)this.mapRegisteredSprites.get((Object)loc.toString());
    }

    private boolean isTerrainAnimationActive(TextureAtlasSprite ts) {
        if (ts == TextureUtils.iconWaterStill || ts == TextureUtils.iconWaterFlow) {
            return Config.isAnimatedWater();
        }
        if (ts == TextureUtils.iconLavaStill || ts == TextureUtils.iconLavaFlow) {
            return Config.isAnimatedLava();
        }
        if (ts == TextureUtils.iconFireLayer0 || ts == TextureUtils.iconFireLayer1) {
            return Config.isAnimatedFire();
        }
        if (ts == TextureUtils.iconPortal) {
            return Config.isAnimatedPortal();
        }
        if (ts == TextureUtils.iconClock || ts == TextureUtils.iconCompass) {
            return true;
        }
        return Config.isAnimatedTerrain();
    }

    public int getCountRegisteredSprites() {
        return this.counterIndexInMap.getValue();
    }

    private int detectMaxMipmapLevel(Map mapSprites, IResourceManager rm) {
        int minLevel;
        int minSize = this.detectMinimumSpriteSize(mapSprites, rm, 20);
        if (minSize < 16) {
            minSize = 16;
        }
        if ((minSize = MathHelper.smallestEncompassingPowerOfTwo((int)minSize)) > 16) {
            Config.log((String)("Sprite size: " + minSize));
        }
        if ((minLevel = MathHelper.log2((int)minSize)) < 4) {
            minLevel = 4;
        }
        return minLevel;
    }

    private int detectMinimumSpriteSize(Map mapSprites, IResourceManager rm, int percentScale) {
        HashMap mapSizeCounts = new HashMap();
        Set entrySetSprites = mapSprites.entrySet();
        for (Map.Entry entry : entrySetSprites) {
            TextureAtlasSprite sprite = (TextureAtlasSprite)entry.getValue();
            ResourceLocation loc = new ResourceLocation(sprite.getIconName());
            ResourceLocation locComplete = this.completeResourceLocation(loc);
            if (sprite.hasCustomLoader(rm, loc)) continue;
            try {
                InputStream in;
                IResource res = rm.getResource(locComplete);
                if (res == null || (in = res.getInputStream()) == null) continue;
                Dimension dim = TextureUtils.getImageSize((InputStream)in, (String)"png");
                in.close();
                if (dim == null) continue;
                int width = dim.width;
                int width2 = MathHelper.smallestEncompassingPowerOfTwo((int)width);
                if (!mapSizeCounts.containsKey((Object)width2)) {
                    mapSizeCounts.put((Object)width2, (Object)1);
                    continue;
                }
                int count = (Integer)mapSizeCounts.get((Object)width2);
                mapSizeCounts.put((Object)width2, (Object)(count + 1));
            }
            catch (Exception e) {}
        }
        int countSprites = 0;
        Set setSizes = mapSizeCounts.keySet();
        TreeSet setSizesSorted = new TreeSet((Collection)setSizes);
        Iterator it = setSizesSorted.iterator();
        while (it.hasNext()) {
            int size = (Integer)it.next();
            int count = (Integer)mapSizeCounts.get((Object)size);
            countSprites += count;
        }
        int minSize = 16;
        int countScale = 0;
        int countScaleMax = countSprites * percentScale / 100;
        Iterator it2 = setSizesSorted.iterator();
        while (it2.hasNext()) {
            int size = (Integer)it2.next();
            int count = (Integer)mapSizeCounts.get((Object)size);
            countScale += count;
            if (size > minSize) {
                minSize = size;
            }
            if (countScale <= countScaleMax) continue;
            return minSize;
        }
        return minSize;
    }

    private int getMinSpriteSize() {
        int minSize = 1 << this.mipmapLevels;
        if (minSize < 8) {
            minSize = 8;
        }
        return minSize;
    }

    private int[] getMissingImageData(int size) {
        BufferedImage bi = new BufferedImage(16, 16, 2);
        bi.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
        BufferedImage bi2 = TextureUtils.scaleImage((BufferedImage)bi, (int)size);
        int[] data = new int[size * size];
        bi2.getRGB(0, 0, size, size, data, 0, size);
        return data;
    }

    public boolean isTextureBound() {
        int texId;
        int boundTexId = GlStateManager.getBoundTexture();
        return boundTexId == (texId = this.getGlTextureId());
    }

    private void updateIconGrid(int sheetWidth, int sheetHeight) {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;
        if (this.iconGridSize <= 0) {
            return;
        }
        this.iconGridCountX = sheetWidth / this.iconGridSize;
        this.iconGridCountY = sheetHeight / this.iconGridSize;
        this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
        this.iconGridSizeU = 1.0 / (double)this.iconGridCountX;
        this.iconGridSizeV = 1.0 / (double)this.iconGridCountY;
        for (TextureAtlasSprite ts : this.mapUploadedSprites.values()) {
            double deltaU = 0.5 / (double)sheetWidth;
            double deltaV = 0.5 / (double)sheetHeight;
            double uMin = (double)Math.min((float)ts.getMinU(), (float)ts.getMaxU()) + deltaU;
            double vMin = (double)Math.min((float)ts.getMinV(), (float)ts.getMaxV()) + deltaV;
            double uMax = (double)Math.max((float)ts.getMinU(), (float)ts.getMaxU()) - deltaU;
            double vMax = (double)Math.max((float)ts.getMinV(), (float)ts.getMaxV()) - deltaV;
            int iuMin = (int)(uMin / this.iconGridSizeU);
            int ivMin = (int)(vMin / this.iconGridSizeV);
            int iuMax = (int)(uMax / this.iconGridSizeU);
            int ivMax = (int)(vMax / this.iconGridSizeV);
            for (int iu = iuMin; iu <= iuMax; ++iu) {
                if (iu < 0 || iu >= this.iconGridCountX) {
                    Config.warn((String)("Invalid grid U: " + iu + ", icon: " + ts.getIconName()));
                    continue;
                }
                for (int iv = ivMin; iv <= ivMax; ++iv) {
                    if (iv < 0 || iv >= this.iconGridCountX) {
                        Config.warn((String)("Invalid grid V: " + iv + ", icon: " + ts.getIconName()));
                        continue;
                    }
                    int index = iv * this.iconGridCountX + iu;
                    this.iconGrid[index] = ts;
                }
            }
        }
    }

    public TextureAtlasSprite getIconByUV(double u, double v) {
        if (this.iconGrid == null) {
            return null;
        }
        int iv = (int)(v / this.iconGridSizeV);
        int iu = (int)(u / this.iconGridSizeU);
        int index = iv * this.iconGridCountX + iu;
        if (index < 0 || index > this.iconGrid.length) {
            return null;
        }
        return this.iconGrid[index];
    }

    private void checkEmissive(ResourceLocation locSprite, TextureAtlasSprite sprite) {
        String suffixEm = EmissiveTextures.getSuffixEmissive();
        if (suffixEm == null) {
            return;
        }
        if (locSprite.getPath().endsWith(suffixEm)) {
            return;
        }
        ResourceLocation locSpriteEm = new ResourceLocation(locSprite.getNamespace(), locSprite.getPath() + suffixEm);
        ResourceLocation locPngEm = this.completeResourceLocation(locSpriteEm);
        if (!Config.hasResource((ResourceLocation)locPngEm)) {
            return;
        }
        TextureAtlasSprite spriteEmissive = this.registerSprite(locSpriteEm);
        spriteEmissive.isEmissive = true;
        sprite.spriteEmissive = spriteEmissive;
    }

    public int getCountAnimations() {
        return this.listAnimatedSprites.size();
    }

    public int getCountAnimationsActive() {
        return this.countAnimationsActive;
    }
}
