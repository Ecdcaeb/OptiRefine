package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.client.resources.AbstractResourcePack;
import org.spongepowered.asm.mixin.Mixin;

import java.io.File;

@Checked
@Mixin(AbstractResourcePack.class)
public abstract class MixinAbstractResourcePack {

    @AccessTransformer(name = "field_110597_b", deobf = true)
    public final File acc__resourcePackFile;

    protected MixinAbstractResourcePack( File acc__resourcePackFile) {
        this.acc__resourcePackFile = acc__resourcePackFile;
    }
}

/*
+++ net/minecraft/client/resources/AbstractResourcePack.java	Tue Aug 19 14:59:58 2025
@@ -17,13 +17,13 @@
 import org.apache.commons.io.IOUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public abstract class AbstractResourcePack implements IResourcePack {
    private static final Logger LOGGER = LogManager.getLogger();
-   protected final File resourcePackFile;
+   public final File resourcePackFile;

    public AbstractResourcePack(File var1) {
       this.resourcePackFile = var1;
    }

    private static String locationToName(ResourceLocation var0) {
@@ -64,13 +64,13 @@
       } catch (RuntimeException var9) {
          throw new JsonParseException(var9);
       } finally {
          IOUtils.closeQuietly(var4);
       }

-      return var0.parseMetadataSection(var2, (JsonObject)var3);
+      return (T)var0.parseMetadataSection(var2, (JsonObject)var3);
    }

    public BufferedImage getPackImage() throws IOException {
       return TextureUtil.readBufferedImage(this.getInputStreamByName("pack.png"));
    }

 */

