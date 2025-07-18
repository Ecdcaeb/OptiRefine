package net.minecraft.client.resources;

import com.google.gson.JsonParseException;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackListEntryServer extends ResourcePackListEntry {
   private static final Logger LOGGER = LogManager.getLogger();
   private final IResourcePack resourcePack;
   private final ResourceLocation resourcePackIcon;

   public ResourcePackListEntryServer(GuiScreenResourcePacks var1, IResourcePack var2) {
      super(☃);
      this.resourcePack = ☃;

      DynamicTexture ☃;
      try {
         ☃ = new DynamicTexture(☃.getPackImage());
      } catch (IOException var5) {
         ☃ = TextureUtil.MISSING_TEXTURE;
      }

      this.resourcePackIcon = this.mc.getTextureManager().getDynamicTextureLocation("texturepackicon", ☃);
   }

   @Override
   protected int getResourcePackFormat() {
      return 3;
   }

   @Override
   protected String getResourcePackDescription() {
      try {
         PackMetadataSection ☃ = this.resourcePack.getPackMetadata(this.mc.getResourcePackRepository().rprMetadataSerializer, "pack");
         if (☃ != null) {
            return ☃.getPackDescription().getFormattedText();
         }
      } catch (JsonParseException var2) {
         LOGGER.error("Couldn't load metadata info", var2);
      } catch (IOException var3) {
         LOGGER.error("Couldn't load metadata info", var3);
      }

      return TextFormatting.RED + "Missing " + "pack.mcmeta" + " :(";
   }

   @Override
   protected boolean canMoveRight() {
      return false;
   }

   @Override
   protected boolean canMoveLeft() {
      return false;
   }

   @Override
   protected boolean canMoveUp() {
      return false;
   }

   @Override
   protected boolean canMoveDown() {
      return false;
   }

   @Override
   protected String getResourcePackName() {
      return "Server";
   }

   @Override
   protected void bindResourcePackIcon() {
      this.mc.getTextureManager().bindTexture(this.resourcePackIcon);
   }

   @Override
   protected boolean showHoverOverlay() {
      return false;
   }

   @Override
   public boolean isServerPack() {
      return true;
   }
}
