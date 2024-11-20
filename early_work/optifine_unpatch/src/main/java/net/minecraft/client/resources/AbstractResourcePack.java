/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonParser
 *  java.awt.image.BufferedImage
 *  java.io.BufferedReader
 *  java.io.File
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.Reader
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.nio.charset.StandardCharsets
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResourcePack
 *  net.minecraft.client.resources.data.IMetadataSection
 *  net.minecraft.client.resources.data.MetadataSerializer
 *  net.minecraft.util.ResourceLocation
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.resources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractResourcePack
implements IResourcePack {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final File resourcePackFile;

    public AbstractResourcePack(File file) {
        this.resourcePackFile = file;
    }

    private static String locationToName(ResourceLocation resourceLocation) {
        return String.format((String)"%s/%s/%s", (Object[])new Object[]{"assets", resourceLocation.getNamespace(), resourceLocation.getPath()});
    }

    protected static String getRelativeName(File file, File file2) {
        return file.toURI().relativize(file2.toURI()).getPath();
    }

    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException {
        return this.getInputStreamByName(AbstractResourcePack.locationToName(resourceLocation));
    }

    public boolean resourceExists(ResourceLocation resourceLocation) {
        return this.hasResourceName(AbstractResourcePack.locationToName(resourceLocation));
    }

    protected abstract InputStream getInputStreamByName(String var1) throws IOException;

    protected abstract boolean hasResourceName(String var1);

    protected void logNameNotLowercase(String string) {
        LOGGER.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", (Object)string, (Object)this.resourcePackFile);
    }

    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String string) throws IOException {
        return AbstractResourcePack.readMetadata(metadataSerializer, this.getInputStreamByName("pack.mcmeta"), string);
    }

    static <T extends IMetadataSection> T readMetadata(MetadataSerializer metadataSerializer, InputStream inputStream, String string) {
        JsonObject jsonObject = null;
        BufferedReader \u26032 = null;
        try {
            \u26032 = new BufferedReader((Reader)new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            jsonObject = new JsonParser().parse((Reader)\u26032).getAsJsonObject();
        }
        catch (RuntimeException \u26033) {
            try {
                throw new JsonParseException((Throwable)\u26033);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(\u26032);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((Reader)\u26032);
        return (T)metadataSerializer.parseMetadataSection(string, jsonObject);
    }

    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage((InputStream)this.getInputStreamByName("pack.png"));
    }

    public String getPackName() {
        return this.resourcePackFile.getName();
    }
}
