package net.minecraft.client.renderer.entity.layers;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class LayerEntityOnShoulder implements LayerRenderer<EntityPlayer> {
   private final RenderManager renderManager;
   protected RenderLivingBase<? extends EntityLivingBase> leftRenderer;
   private ModelBase leftModel;
   private ResourceLocation leftResource;
   private UUID leftUniqueId;
   private Class<?> leftEntityClass;
   protected RenderLivingBase<? extends EntityLivingBase> rightRenderer;
   private ModelBase rightModel;
   private ResourceLocation rightResource;
   private UUID rightUniqueId;
   private Class<?> rightEntityClass;

   public LayerEntityOnShoulder(RenderManager var1) {
      this.renderManager = ☃;
   }

   public void doRenderLayer(EntityPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (☃.getLeftShoulderEntity() != null || ☃.getRightShoulderEntity() != null) {
         GlStateManager.enableRescaleNormal();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         NBTTagCompound ☃ = ☃.getLeftShoulderEntity();
         if (!☃.isEmpty()) {
            LayerEntityOnShoulder.DataHolder ☃x = this.renderEntityOnShoulder(
               ☃, this.leftUniqueId, ☃, this.leftRenderer, this.leftModel, this.leftResource, this.leftEntityClass, ☃, ☃, ☃, ☃, ☃, ☃, ☃, true
            );
            this.leftUniqueId = ☃x.entityId;
            this.leftRenderer = ☃x.renderer;
            this.leftResource = ☃x.textureLocation;
            this.leftModel = ☃x.model;
            this.leftEntityClass = ☃x.clazz;
         }

         NBTTagCompound ☃x = ☃.getRightShoulderEntity();
         if (!☃x.isEmpty()) {
            LayerEntityOnShoulder.DataHolder ☃xx = this.renderEntityOnShoulder(
               ☃, this.rightUniqueId, ☃x, this.rightRenderer, this.rightModel, this.rightResource, this.rightEntityClass, ☃, ☃, ☃, ☃, ☃, ☃, ☃, false
            );
            this.rightUniqueId = ☃xx.entityId;
            this.rightRenderer = ☃xx.renderer;
            this.rightResource = ☃xx.textureLocation;
            this.rightModel = ☃xx.model;
            this.rightEntityClass = ☃xx.clazz;
         }

         GlStateManager.disableRescaleNormal();
      }
   }

   private LayerEntityOnShoulder.DataHolder renderEntityOnShoulder(
      EntityPlayer var1,
      @Nullable UUID var2,
      NBTTagCompound var3,
      RenderLivingBase<? extends EntityLivingBase> var4,
      ModelBase var5,
      ResourceLocation var6,
      Class<?> var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      float var14,
      boolean var15
   ) {
      if (☃ == null || !☃.equals(☃.getUniqueId("UUID"))) {
         ☃ = ☃.getUniqueId("UUID");
         ☃ = EntityList.getClassFromName(☃.getString("id"));
         if (☃ == EntityParrot.class) {
            ☃ = new RenderParrot(this.renderManager);
            ☃ = new ModelParrot();
            ☃ = RenderParrot.PARROT_TEXTURES[☃.getInteger("Variant")];
         }
      }

      ☃.bindTexture(☃);
      GlStateManager.pushMatrix();
      float ☃ = ☃.isSneaking() ? -1.3F : -1.5F;
      float ☃x = ☃ ? 0.4F : -0.4F;
      GlStateManager.translate(☃x, ☃, 0.0F);
      if (☃ == EntityParrot.class) {
         ☃ = 0.0F;
      }

      ☃.setLivingAnimations(☃, ☃, ☃, ☃);
      ☃.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      ☃.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      GlStateManager.popMatrix();
      return new LayerEntityOnShoulder.DataHolder(☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }

   class DataHolder {
      public UUID entityId;
      public RenderLivingBase<? extends EntityLivingBase> renderer;
      public ModelBase model;
      public ResourceLocation textureLocation;
      public Class<?> clazz;

      public DataHolder(UUID var2, RenderLivingBase<? extends EntityLivingBase> var3, ModelBase var4, ResourceLocation var5, Class<?> var6) {
         this.entityId = ☃;
         this.renderer = ☃;
         this.model = ☃;
         this.textureLocation = ☃;
         this.clazz = ☃;
      }
   }
}
