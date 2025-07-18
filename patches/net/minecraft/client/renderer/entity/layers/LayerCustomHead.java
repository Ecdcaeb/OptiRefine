package net.minecraft.client.renderer.entity.layers;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.StringUtils;

public class LayerCustomHead implements LayerRenderer<EntityLivingBase> {
   private final ModelRenderer modelRenderer;

   public LayerCustomHead(ModelRenderer var1) {
      this.modelRenderer = ☃;
   }

   @Override
   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
      if (!☃.isEmpty()) {
         Item ☃x = ☃.getItem();
         Minecraft ☃xx = Minecraft.getMinecraft();
         GlStateManager.pushMatrix();
         if (☃.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
         }

         boolean ☃xxx = ☃ instanceof EntityVillager || ☃ instanceof EntityZombieVillager;
         if (☃.isChild() && !(☃ instanceof EntityVillager)) {
            float ☃xxxx = 2.0F;
            float ☃xxxxx = 1.4F;
            GlStateManager.translate(0.0F, 0.5F * ☃, 0.0F);
            GlStateManager.scale(0.7F, 0.7F, 0.7F);
            GlStateManager.translate(0.0F, 16.0F * ☃, 0.0F);
         }

         this.modelRenderer.postRender(0.0625F);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         if (☃x == Items.SKULL) {
            float ☃xxxx = 1.1875F;
            GlStateManager.scale(1.1875F, -1.1875F, -1.1875F);
            if (☃xxx) {
               GlStateManager.translate(0.0F, 0.0625F, 0.0F);
            }

            GameProfile ☃xxxxx = null;
            if (☃.hasTagCompound()) {
               NBTTagCompound ☃xxxxxx = ☃.getTagCompound();
               if (☃xxxxxx.hasKey("SkullOwner", 10)) {
                  ☃xxxxx = NBTUtil.readGameProfileFromNBT(☃xxxxxx.getCompoundTag("SkullOwner"));
               } else if (☃xxxxxx.hasKey("SkullOwner", 8)) {
                  String ☃xxxxxxx = ☃xxxxxx.getString("SkullOwner");
                  if (!StringUtils.isBlank(☃xxxxxxx)) {
                     ☃xxxxx = TileEntitySkull.updateGameProfile(new GameProfile(null, ☃xxxxxxx));
                     ☃xxxxxx.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), ☃xxxxx));
                  }
               }
            }

            TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, ☃.getMetadata(), ☃xxxxx, -1, ☃);
         } else if (!(☃x instanceof ItemArmor) || ((ItemArmor)☃x).getEquipmentSlot() != EntityEquipmentSlot.HEAD) {
            float ☃xxxxx = 0.625F;
            GlStateManager.translate(0.0F, -0.25F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(0.625F, -0.625F, -0.625F);
            if (☃xxx) {
               GlStateManager.translate(0.0F, 0.1875F, 0.0F);
            }

            ☃xx.getItemRenderer().renderItem(☃, ☃, ItemCameraTransforms.TransformType.HEAD);
         }

         GlStateManager.popMatrix();
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
