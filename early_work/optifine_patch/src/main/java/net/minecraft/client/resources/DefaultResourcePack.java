/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  java.awt.image.BufferedImage
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.FileNotFoundException
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.net.URL
 *  java.util.Set
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.AbstractResourcePack
 *  net.minecraft.client.resources.FolderResourcePack
 *  net.minecraft.client.resources.IResourcePack
 *  net.minecraft.client.resources.ResourceIndex
 *  net.minecraft.client.resources.data.IMetadataSection
 *  net.minecraft.client.resources.data.MetadataSerializer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Util
 *  net.minecraft.util.Util$EnumOS
 *  net.optifine.reflect.ReflectorForge
 */
package net.minecraft.client.resources;

import com.google.common.collect.ImmutableSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.optifine.reflect.ReflectorForge;

public class DefaultResourcePack
implements IResourcePack {
    public static final Set<String> DEFAULT_RESOURCE_DOMAINS = ImmutableSet.of((Object)"minecraft", (Object)"realms");
    private final ResourceIndex resourceIndex;
    private static final boolean ON_WINDOWS = Util.getOSType() == Util.EnumOS.WINDOWS;

    public DefaultResourcePack(ResourceIndex resourceIndexIn) {
        this.resourceIndex = resourceIndexIn;
    }

    public InputStream getInputStream(ResourceLocation location) throws IOException {
        InputStream inputstream = this.getInputStreamAssets(location);
        if (inputstream != null) {
            return inputstream;
        }
        InputStream inputstream1 = this.getResourceStream(location);
        if (inputstream1 != null) {
            return inputstream1;
        }
        throw new FileNotFoundException(location.getPath());
    }

    @Nullable
    public InputStream getInputStreamAssets(ResourceLocation location) throws IOException, FileNotFoundException {
        File file1 = this.resourceIndex.getFile(location);
        return file1 != null && file1.isFile() ? new FileInputStream(file1) : null;
    }

    @Nullable
    private InputStream getResourceStream(ResourceLocation location) {
        String s = "/assets/" + location.getNamespace() + "/" + location.getPath();
        InputStream is = ReflectorForge.getOptiFineResourceStream((String)s);
        if (is != null) {
            return is;
        }
        try {
            URL url = DefaultResourcePack.class.getResource(s);
            return url != null && this.validatePath(new File(url.getFile()), s) ? DefaultResourcePack.class.getResourceAsStream(s) : null;
        }
        catch (IOException var4) {
            return DefaultResourcePack.class.getResourceAsStream(s);
        }
    }

    public boolean resourceExists(ResourceLocation location) {
        return this.getResourceStream(location) != null || this.resourceIndex.isFileExisting(location);
    }

    public Set<String> getResourceDomains() {
        return DEFAULT_RESOURCE_DOMAINS;
    }

    @Nullable
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        try {
            FileInputStream inputstream = new FileInputStream(this.resourceIndex.getPackMcmeta());
            return (T)AbstractResourcePack.readMetadata((MetadataSerializer)metadataSerializer, (InputStream)inputstream, (String)metadataSectionName);
        }
        catch (RuntimeException var4) {
            return (T)((IMetadataSection)null);
        }
        catch (FileNotFoundException var5) {
            return (T)((IMetadataSection)null);
        }
    }

    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage((InputStream)DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getPath()));
    }

    public String getPackName() {
        return "Default";
    }

    private boolean validatePath(File file, String path) throws IOException {
        String s = file.getPath();
        if (s.startsWith("file:")) {
            if (ON_WINDOWS) {
                s = s.replace((CharSequence)"\\", (CharSequence)"/");
            }
            return s.endsWith(path);
        }
        return FolderResourcePack.validatePath((File)file, (String)path);
    }
}
