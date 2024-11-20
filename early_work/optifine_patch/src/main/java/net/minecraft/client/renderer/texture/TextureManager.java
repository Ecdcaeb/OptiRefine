/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  java.io.IOException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.ITickable
 *  net.minecraft.client.renderer.texture.ITickableTextureObject
 *  net.minecraft.client.renderer.texture.LayeredColorMaskTexture
 *  net.minecraft.client.renderer.texture.SimpleTexture
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.IResourceManagerReloadListener
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.ResourceLocation
 *  net.optifine.CustomGuis
 *  net.optifine.EmissiveTextures
 *  net.optifine.RandomEntities
 *  net.optifine.shaders.ShadersTex
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomGuis;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.shaders.ShadersTex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureManager
implements ITickable,
IResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation("");
    private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
    private final List<ITickable> listTickables = Lists.newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
    private final IResourceManager resourceManager;
    private ITextureObject boundTexture;
    private ResourceLocation boundTextureLocation;

    public TextureManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void bindTexture(ResourceLocation resource) {
        if (Config.isRandomEntities()) {
            resource = RandomEntities.getTextureLocation((ResourceLocation)resource);
        }
        if (Config.isCustomGuis()) {
            resource = CustomGuis.getTextureLocation((ResourceLocation)resource);
        }
        ITextureObject itextureobject = (ITextureObject)this.mapTextureObjects.get((Object)resource);
        if (EmissiveTextures.isActive()) {
            itextureobject = EmissiveTextures.getEmissiveTexture((ITextureObject)itextureobject, this.mapTextureObjects);
        }
        if (itextureobject == null) {
            itextureobject = new SimpleTexture(resource);
            this.loadTexture(resource, itextureobject);
        }
        if (Config.isShaders()) {
            ShadersTex.bindTexture((ITextureObject)itextureobject);
        } else {
            TextureUtil.bindTexture((int)itextureobject.getGlTextureId());
        }
        this.boundTexture = itextureobject;
        this.boundTextureLocation = resource;
    }

    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        if (this.loadTexture(textureLocation, (ITextureObject)textureObj)) {
            this.listTickables.add((Object)textureObj);
            return true;
        }
        return false;
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        boolean flag = true;
        try {
            textureObj.loadTexture(this.resourceManager);
        }
        catch (IOException ioexception) {
            if (textureLocation != RESOURCE_LOCATION_EMPTY) {
                LOGGER.warn("Failed to load texture: {}", (Object)textureLocation, (Object)ioexception);
            }
            textureObj = TextureUtil.MISSING_TEXTURE;
            this.mapTextureObjects.put((Object)textureLocation, (Object)textureObj);
            flag = false;
        }
        catch (Throwable throwable) {
            ITextureObject textureObjf = textureObj;
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", (Object)textureLocation);
            crashreportcategory.addDetail("Texture object class", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
            throw new ReportedException(crashreport);
        }
        this.mapTextureObjects.put((Object)textureLocation, (Object)textureObj);
        return flag;
    }

    public ITextureObject getTexture(ResourceLocation textureLocation) {
        return (ITextureObject)this.mapTextureObjects.get((Object)textureLocation);
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        Integer integer;
        if (name.equals((Object)"logo")) {
            texture = Config.getMojangLogoTexture((DynamicTexture)texture);
        }
        integer = (integer = (Integer)this.mapTextureCounters.get((Object)name)) == null ? Integer.valueOf((int)1) : Integer.valueOf((int)(integer + 1));
        this.mapTextureCounters.put((Object)name, (Object)integer);
        ResourceLocation resourcelocation = new ResourceLocation(String.format((String)"dynamic/%s_%d", (Object[])new Object[]{name, integer}));
        this.loadTexture(resourcelocation, (ITextureObject)texture);
        return resourcelocation;
    }

    public void tick() {
        for (ITickable itickable : this.listTickables) {
            itickable.tick();
        }
    }

    public void deleteTexture(ResourceLocation textureLocation) {
        ITextureObject itextureobject = this.getTexture(textureLocation);
        if (itextureobject != null) {
            this.mapTextureObjects.remove((Object)textureLocation);
            TextureUtil.deleteTexture((int)itextureobject.getGlTextureId());
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        Config.dbg((String)"*** Reloading textures ***");
        Config.log((String)("Resource packs: " + Config.getResourcePackNames()));
        Iterator it = this.mapTextureObjects.keySet().iterator();
        while (it.hasNext()) {
            ResourceLocation loc = (ResourceLocation)it.next();
            String path = loc.getPath();
            if (!path.startsWith("mcpatcher/") && !path.startsWith("optifine/") && !EmissiveTextures.isEmissive((ResourceLocation)loc)) continue;
            ITextureObject tex = (ITextureObject)this.mapTextureObjects.get((Object)loc);
            if (tex instanceof AbstractTexture) {
                AbstractTexture at = (AbstractTexture)tex;
                at.deleteGlTexture();
            }
            it.remove();
        }
        EmissiveTextures.update();
        HashSet entries = new HashSet((Collection)this.mapTextureObjects.entrySet());
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            ITextureObject itextureobject = (ITextureObject)entry.getValue();
            if (itextureobject == TextureUtil.MISSING_TEXTURE) {
                iterator.remove();
                continue;
            }
            this.loadTexture((ResourceLocation)entry.getKey(), itextureobject);
        }
    }

    public void reloadBannerTextures() {
        HashSet entries = new HashSet((Collection)this.mapTextureObjects.entrySet());
        for (Map.Entry entry : entries) {
            ResourceLocation loc = (ResourceLocation)entry.getKey();
            ITextureObject tex = (ITextureObject)entry.getValue();
            if (!(tex instanceof LayeredColorMaskTexture)) continue;
            this.loadTexture(loc, tex);
        }
    }

    public ITextureObject getBoundTexture() {
        return this.boundTexture;
    }

    public ResourceLocation getBoundTextureLocation() {
        return this.boundTextureLocation;
    }
}
