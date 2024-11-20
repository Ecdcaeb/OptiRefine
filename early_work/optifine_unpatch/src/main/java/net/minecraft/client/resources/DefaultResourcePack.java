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

public class DefaultResourcePack
implements IResourcePack {
    public static final Set<String> DEFAULT_RESOURCE_DOMAINS = ImmutableSet.of((Object)"minecraft", (Object)"realms");
    private final ResourceIndex resourceIndex;

    public DefaultResourcePack(ResourceIndex resourceIndex) {
        this.resourceIndex = resourceIndex;
    }

    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException {
        InputStream inputStream = this.getInputStreamAssets(resourceLocation);
        if (inputStream != null) {
            return inputStream;
        }
        \u2603 = this.getResourceStream(resourceLocation);
        if (\u2603 != null) {
            return \u2603;
        }
        throw new FileNotFoundException(resourceLocation.getPath());
    }

    @Nullable
    public InputStream getInputStreamAssets(ResourceLocation resourceLocation) throws FileNotFoundException {
        File file = this.resourceIndex.getFile(resourceLocation);
        return file == null || !file.isFile() ? null : new FileInputStream(file);
    }

    @Nullable
    private InputStream getResourceStream(ResourceLocation resourceLocation) {
        String string = "/assets/" + resourceLocation.getNamespace() + "/" + resourceLocation.getPath();
        try {
            URL uRL = DefaultResourcePack.class.getResource(string);
            if (uRL != null && FolderResourcePack.validatePath((File)new File(uRL.getFile()), (String)string)) {
                return DefaultResourcePack.class.getResourceAsStream(string);
            }
        }
        catch (IOException iOException) {
            return DefaultResourcePack.class.getResourceAsStream(string);
        }
        return null;
    }

    public boolean resourceExists(ResourceLocation resourceLocation) {
        return this.getResourceStream(resourceLocation) != null || this.resourceIndex.isFileExisting(resourceLocation);
    }

    public Set<String> getResourceDomains() {
        return DEFAULT_RESOURCE_DOMAINS;
    }

    @Nullable
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String string) throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.resourceIndex.getPackMcmeta());
            return (T)AbstractResourcePack.readMetadata((MetadataSerializer)metadataSerializer, (InputStream)fileInputStream, (String)string);
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
        catch (FileNotFoundException fileNotFoundException) {
            return null;
        }
    }

    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage((InputStream)DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getPath()));
    }

    public String getPackName() {
        return "Default";
    }
}
