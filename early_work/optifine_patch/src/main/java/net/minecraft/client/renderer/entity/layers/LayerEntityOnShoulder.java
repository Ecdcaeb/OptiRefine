/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.UUID
 *  javax.annotation.Nullable
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelParrot
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.entity.RenderParrot
 *  net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder$DataHolder
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityList
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.passive.EntityParrot
 *  net.minecraft.entity.passive.EntityShoulderRiding
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.ResourceLocation
 *  net.optifine.shaders.Shaders
 */
package net.minecraft.client.renderer.entity.layers;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.Shaders;

public class LayerEntityOnShoulder
implements LayerRenderer<EntityPlayer> {
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

    public LayerEntityOnShoulder(RenderManager p_i47370_1_) {
        this.renderManager = p_i47370_1_;
    }

    public void doRenderLayer(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn.getLeftShoulderEntity() != null || entitylivingbaseIn.getRightShoulderEntity() != null) {
            NBTTagCompound nbttagcompound1;
            GlStateManager.enableRescaleNormal();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            NBTTagCompound nbttagcompound = entitylivingbaseIn.getLeftShoulderEntity();
            if (!nbttagcompound.isEmpty()) {
                DataHolder layerentityonshoulder$dataholder = this.renderEntityOnShoulder(entitylivingbaseIn, this.leftUniqueId, nbttagcompound, this.leftRenderer, this.leftModel, this.leftResource, this.leftEntityClass, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, true);
                this.leftUniqueId = layerentityonshoulder$dataholder.entityId;
                this.leftRenderer = layerentityonshoulder$dataholder.renderer;
                this.leftResource = layerentityonshoulder$dataholder.textureLocation;
                this.leftModel = layerentityonshoulder$dataholder.model;
                this.leftEntityClass = layerentityonshoulder$dataholder.clazz;
            }
            if (!(nbttagcompound1 = entitylivingbaseIn.getRightShoulderEntity()).isEmpty()) {
                DataHolder layerentityonshoulder$dataholder1 = this.renderEntityOnShoulder(entitylivingbaseIn, this.rightUniqueId, nbttagcompound1, this.rightRenderer, this.rightModel, this.rightResource, this.rightEntityClass, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, false);
                this.rightUniqueId = layerentityonshoulder$dataholder1.entityId;
                this.rightRenderer = layerentityonshoulder$dataholder1.renderer;
                this.rightResource = layerentityonshoulder$dataholder1.textureLocation;
                this.rightModel = layerentityonshoulder$dataholder1.model;
                this.rightEntityClass = layerentityonshoulder$dataholder1.clazz;
            }
            GlStateManager.disableRescaleNormal();
        }
    }

    private DataHolder renderEntityOnShoulder(EntityPlayer p_192864_1_, @Nullable UUID p_192864_2_, NBTTagCompound p_192864_3_, RenderLivingBase<? extends EntityLivingBase> p_192864_4_, ModelBase p_192864_5_, ResourceLocation p_192864_6_, Class<?> p_192864_7_, float p_192864_8_, float p_192864_9_, float p_192864_10_, float p_192864_11_, float p_192864_12_, float p_192864_13_, float p_192864_14_, boolean p_192864_15_) {
        if (p_192864_2_ == null || !p_192864_2_.equals((Object)p_192864_3_.getUniqueId("UUID"))) {
            p_192864_2_ = p_192864_3_.getUniqueId("UUID");
            p_192864_7_ = EntityList.getClassFromName((String)p_192864_3_.getString("id"));
            if (p_192864_7_ == EntityParrot.class) {
                p_192864_4_ = new RenderParrot(this.renderManager);
                p_192864_5_ = new ModelParrot();
                p_192864_6_ = RenderParrot.PARROT_TEXTURES[p_192864_3_.getInteger("Variant")];
            }
        }
        Entity renderedEntityOld = Config.getRenderGlobal().renderedEntity;
        if (p_192864_1_ instanceof AbstractClientPlayer) {
            EntityShoulderRiding entityShoulder;
            AbstractClientPlayer acp = (AbstractClientPlayer)p_192864_1_;
            EntityShoulderRiding entityShoulderRiding = entityShoulder = p_192864_2_ == this.leftUniqueId ? acp.entityShoulderLeft : acp.entityShoulderRight;
            if (entityShoulder != null) {
                Config.getRenderGlobal().renderedEntity = entityShoulder;
                if (Config.isShaders()) {
                    Shaders.nextEntity((Entity)entityShoulder);
                }
            }
        }
        p_192864_4_.bindTexture(p_192864_6_);
        GlStateManager.pushMatrix();
        float f = p_192864_1_.aU() ? -1.3f : -1.5f;
        float f1 = p_192864_15_ ? 0.4f : -0.4f;
        GlStateManager.translate((float)f1, (float)f, (float)0.0f);
        if (p_192864_7_ == EntityParrot.class) {
            p_192864_11_ = 0.0f;
        }
        p_192864_5_.setLivingAnimations((EntityLivingBase)p_192864_1_, p_192864_8_, p_192864_9_, p_192864_10_);
        p_192864_5_.setRotationAngles(p_192864_8_, p_192864_9_, p_192864_11_, p_192864_12_, p_192864_13_, p_192864_14_, (Entity)p_192864_1_);
        p_192864_5_.render((Entity)p_192864_1_, p_192864_8_, p_192864_9_, p_192864_11_, p_192864_12_, p_192864_13_, p_192864_14_);
        GlStateManager.popMatrix();
        Config.getRenderGlobal().renderedEntity = renderedEntityOld;
        if (Config.isShaders()) {
            Shaders.nextEntity((Entity)renderedEntityOld);
        }
        return new DataHolder(this, p_192864_2_, (RenderLivingBase)p_192864_4_, p_192864_5_, p_192864_6_, p_192864_7_);
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
