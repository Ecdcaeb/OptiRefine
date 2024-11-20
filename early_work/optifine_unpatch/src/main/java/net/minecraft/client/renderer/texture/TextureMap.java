/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  java.io.Closeable
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayDeque
 *  java.util.Deque
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.ITextureMapPopulator
 *  net.minecraft.client.renderer.texture.ITickableTextureObject
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
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.fml.common.FMLLog
 *  net.minecraftforge.fml.common.ProgressManager
 *  net.minecraftforge.fml.common.ProgressManager$ProgressBar
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
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
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(value=Side.CLIENT)
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
    private final Deque<ResourceLocation> loadingSprites = new ArrayDeque();
    private final Set<ResourceLocation> loadedSprites = new HashSet();

    public TextureMap(String basePathIn) {
        this(basePathIn, null);
    }

    public TextureMap(String basePathIn, @Nullable ITextureMapPopulator iconCreatorIn) {
        this(basePathIn, iconCreatorIn, false);
    }

    public TextureMap(String basePathIn, boolean skipFirst) {
        this(basePathIn, null, skipFirst);
    }

    public TextureMap(String basePathIn, @Nullable ITextureMapPopulator iconCreatorIn, boolean skipFirst) {
        this.listAnimatedSprites = Lists.newArrayList();
        this.mapRegisteredSprites = Maps.newHashMap();
        this.mapUploadedSprites = Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = basePathIn;
        this.iconCreator = iconCreatorIn;
    }

    private void initMissingImage() {
        int[] aint = TextureUtil.MISSING_TEXTURE_DATA;
        this.missingImage.setIconWidth(16);
        this.missingImage.setIconHeight(16);
        int[][] aint1 = new int[this.mipmapLevels + 1][];
        aint1[0] = aint;
        this.missingImage.setFramesTextureData((List)Lists.newArrayList((Object[])new int[][][]{aint1}));
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        if (this.iconCreator != null) {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager resourceManager, ITextureMapPopulator iconCreatorIn) {
        this.mapRegisteredSprites.clear();
        ForgeHooksClient.onTextureStitchedPre((TextureMap)this);
        iconCreatorIn.registerSprites(this);
        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }

    public void loadTextureAtlas(IResourceManager resourceManager) {
        int i = Minecraft.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(i, i, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int j = Integer.MAX_VALUE;
        int k = 1 << this.mipmapLevels;
        FMLLog.log.info("Max texture size: {}", (Object)i);
        ProgressManager.ProgressBar bar = ProgressManager.push((String)"Texture stitching", (int)this.mapRegisteredSprites.size());
        this.loadedSprites.clear();
        for (Map.Entry entry : Maps.newHashMap(this.mapRegisteredSprites).entrySet()) {
            ResourceLocation location = new ResourceLocation((String)entry.getKey());
            bar.step(location.toString());
            j = this.loadTexture(stitcher, resourceManager, location, (TextureAtlasSprite)entry.getValue(), bar, j, k);
        }
        this.finishLoading(stitcher, bar, j, k);
    }

    /*
     * Exception decompiling
     */
    private int loadTexture(Stitcher stitcher, IResourceManager resourceManager, ResourceLocation location, TextureAtlasSprite textureatlassprite, ProgressManager.ProgressBar bar, int j, int k) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.NullPointerException: Cannot invoke "org.benf.cfr.reader.bytecode.analysis.types.BindingSuperContainer.getBoundSuperForBase(org.benf.cfr.reader.bytecode.analysis.types.JavaTypeInstance)" because "bindingSuperContainer" is null
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.LoopLivenessClash.getIterableIterType(LoopLivenessClash.java:35)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.LoopLivenessClash.detect(LoopLivenessClash.java:66)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.LoopLivenessClash.detect(LoopLivenessClash.java:25)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:827)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.Main.doClass(Main.java:18)
         *     at org.benf.cfr.reader.PluginRunner.getDecompilationFor(PluginRunner.java:115)
         *     at cn.enaium.joe.service.decompiler.CFRDecompiler.decompile(CFRDecompiler.java:63)
         *     at cn.enaium.joe.task.SaveAllSourceTask.get(SaveAllSourceTask.java:60)
         *     at cn.enaium.joe.task.SaveAllSourceTask.get(SaveAllSourceTask.java:39)
         *     at java.base/java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1768)
         *     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
         *     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
         *     at java.base/java.lang.Thread.run(Thread.java:1583)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void finishLoading(Stitcher stitcher, ProgressManager.ProgressBar bar, int j, int k) {
        ProgressManager.pop((ProgressManager.ProgressBar)bar);
        int l = Math.min((int)j, (int)k);
        int i1 = MathHelper.log2((int)l);
        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        bar = ProgressManager.push((String)"Texture creation", (int)2);
        bar.step("Stitching");
        stitcher.doStitch();
        LOGGER.info("Created: {}x{} {}-atlas", (Object)stitcher.getCurrentWidth(), (Object)stitcher.getCurrentHeight(), (Object)this.basePath);
        bar.step("Allocating GL texture");
        TextureUtil.allocateTextureImpl((int)this.getGlTextureId(), (int)this.mipmapLevels, (int)stitcher.getCurrentWidth(), (int)stitcher.getCurrentHeight());
        HashMap map = Maps.newHashMap(this.mapRegisteredSprites);
        ProgressManager.pop((ProgressManager.ProgressBar)bar);
        bar = ProgressManager.push((String)"Texture mipmap and upload", (int)stitcher.getStichSlots().size());
        for (TextureAtlasSprite textureatlassprite1 : stitcher.getStichSlots()) {
            bar.step(textureatlassprite1.getIconName());
            String s = textureatlassprite1.getIconName();
            map.remove((Object)s);
            this.mapUploadedSprites.put((Object)s, (Object)textureatlassprite1);
            try {
                TextureUtil.uploadTextureMipmap((int[][])textureatlassprite1.getFrameTextureData(0), (int)textureatlassprite1.getIconWidth(), (int)textureatlassprite1.getIconHeight(), (int)textureatlassprite1.getOriginX(), (int)textureatlassprite1.getOriginY(), (boolean)false, (boolean)false);
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Stitching texture atlas");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Texture being stitched together");
                crashreportcategory.addCrashSection("Atlas path", (Object)this.basePath);
                crashreportcategory.addCrashSection("Sprite", (Object)textureatlassprite1);
                throw new ReportedException(crashreport);
            }
            if (!textureatlassprite1.hasAnimationMetadata()) continue;
            this.listAnimatedSprites.add((Object)textureatlassprite1);
        }
        for (TextureAtlasSprite textureatlassprite2 : map.values()) {
            textureatlassprite2.copyFrom(this.missingImage);
        }
        ForgeHooksClient.onTextureStitchedPost((TextureMap)this);
        ProgressManager.pop((ProgressManager.ProgressBar)bar);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean generateMipmaps(IResourceManager resourceManager, TextureAtlasSprite texture) {
        block10: {
            ResourceLocation resourcelocation = this.getResourceLocation(texture);
            IResource iresource = null;
            if (!texture.hasCustomLoader(resourceManager, resourcelocation)) {
                boolean flag;
                try {
                    iresource = resourceManager.getResource(resourcelocation);
                    texture.loadSpriteFrames(iresource, this.mipmapLevels + 1);
                    break block10;
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

    private ResourceLocation getResourceLocation(TextureAtlasSprite p_184396_1_) {
        ResourceLocation resourcelocation = new ResourceLocation(p_184396_1_.getIconName());
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
        TextureUtil.bindTexture((int)this.getGlTextureId());
        for (TextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
            textureatlassprite.updateAnimation();
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

    private /* synthetic */ TextureAtlasSprite lambda$loadTexture$0(ResourceLocation l) {
        return (TextureAtlasSprite)this.mapRegisteredSprites.get((Object)l.toString());
    }
}
