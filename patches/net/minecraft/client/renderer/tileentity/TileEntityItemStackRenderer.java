package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.StringUtils;

public class TileEntityItemStackRenderer {
   private static final TileEntityShulkerBox[] SHULKER_BOXES = new TileEntityShulkerBox[16];
   public static TileEntityItemStackRenderer instance;
   private final TileEntityChest chestBasic = new TileEntityChest(BlockChest.Type.BASIC);
   private final TileEntityChest chestTrap = new TileEntityChest(BlockChest.Type.TRAP);
   private final TileEntityEnderChest enderChest = new TileEntityEnderChest();
   private final TileEntityBanner banner = new TileEntityBanner();
   private final TileEntityBed bed = new TileEntityBed();
   private final TileEntitySkull skull = new TileEntitySkull();
   private final ModelShield modelShield = new ModelShield();

   public void renderByItem(ItemStack var1) {
      this.renderByItem(☃, 1.0F);
   }

   public void renderByItem(ItemStack var1, float var2) {
      Item ☃ = ☃.getItem();
      if (☃ == Items.BANNER) {
         this.banner.setItemValues(☃, false);
         TileEntityRendererDispatcher.instance.render(this.banner, 0.0, 0.0, 0.0, 0.0F, ☃);
      } else if (☃ == Items.BED) {
         this.bed.setItemValues(☃);
         TileEntityRendererDispatcher.instance.render(this.bed, 0.0, 0.0, 0.0, 0.0F);
      } else if (☃ == Items.SHIELD) {
         if (☃.getSubCompound("BlockEntityTag") != null) {
            this.banner.setItemValues(☃, true);
            Minecraft.getMinecraft()
               .getTextureManager()
               .bindTexture(
                  BannerTextures.SHIELD_DESIGNS
                     .getResourceLocation(this.banner.getPatternResourceLocation(), this.banner.getPatternList(), this.banner.getColorList())
               );
         } else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
         }

         GlStateManager.pushMatrix();
         GlStateManager.scale(1.0F, -1.0F, -1.0F);
         this.modelShield.render();
         GlStateManager.popMatrix();
      } else if (☃ == Items.SKULL) {
         GameProfile ☃x = null;
         if (☃.hasTagCompound()) {
            NBTTagCompound ☃xx = ☃.getTagCompound();
            if (☃xx.hasKey("SkullOwner", 10)) {
               ☃x = NBTUtil.readGameProfileFromNBT(☃xx.getCompoundTag("SkullOwner"));
            } else if (☃xx.hasKey("SkullOwner", 8) && !StringUtils.isBlank(☃xx.getString("SkullOwner"))) {
               GameProfile var6 = new GameProfile(null, ☃xx.getString("SkullOwner"));
               ☃x = TileEntitySkull.updateGameProfile(var6);
               ☃xx.removeTag("SkullOwner");
               ☃xx.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), ☃x));
            }
         }

         if (TileEntitySkullRenderer.instance != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            TileEntitySkullRenderer.instance.renderSkull(0.0F, 0.0F, 0.0F, EnumFacing.UP, 180.0F, ☃.getMetadata(), ☃x, -1, 0.0F);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
         }
      } else if (☃ == Item.getItemFromBlock(Blocks.ENDER_CHEST)) {
         TileEntityRendererDispatcher.instance.render(this.enderChest, 0.0, 0.0, 0.0, 0.0F, ☃);
      } else if (☃ == Item.getItemFromBlock(Blocks.TRAPPED_CHEST)) {
         TileEntityRendererDispatcher.instance.render(this.chestTrap, 0.0, 0.0, 0.0, 0.0F, ☃);
      } else if (Block.getBlockFromItem(☃) instanceof BlockShulkerBox) {
         TileEntityRendererDispatcher.instance.render(SHULKER_BOXES[BlockShulkerBox.getColorFromItem(☃).getMetadata()], 0.0, 0.0, 0.0, 0.0F, ☃);
      } else {
         TileEntityRendererDispatcher.instance.render(this.chestBasic, 0.0, 0.0, 0.0, 0.0F, ☃);
      }
   }

   static {
      for (EnumDyeColor ☃ : EnumDyeColor.values()) {
         SHULKER_BOXES[☃.getMetadata()] = new TileEntityShulkerBox(☃);
      }

      instance = new TileEntityItemStackRenderer();
   }
}
