/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.UUID
 *  javax.annotation.Nullable
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
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.ResourceLocation
 */
package net.minecraft.client.renderer.entity.layers;

import java.util.UUID;
import javax.annotation.Nullable;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

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

    public LayerEntityOnShoulder(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    public void doRenderLayer(EntityPlayer entityPlayer, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (entityPlayer.getLeftShoulderEntity() == null && entityPlayer.getRightShoulderEntity() == null) {
            return;
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        NBTTagCompound nBTTagCompound = entityPlayer.getLeftShoulderEntity();
        if (!nBTTagCompound.isEmpty()) {
            \u2603 = this.renderEntityOnShoulder(entityPlayer, this.leftUniqueId, nBTTagCompound, this.leftRenderer, this.leftModel, this.leftResource, this.leftEntityClass, f, f2, f3, f4, f5, f6, f7, true);
            this.leftUniqueId = \u2603.entityId;
            this.leftRenderer = \u2603.renderer;
            this.leftResource = \u2603.textureLocation;
            this.leftModel = \u2603.model;
            this.leftEntityClass = \u2603.clazz;
        }
        if (!(\u2603 = entityPlayer.getRightShoulderEntity()).isEmpty()) {
            DataHolder dataHolder = this.renderEntityOnShoulder(entityPlayer, this.rightUniqueId, \u2603, this.rightRenderer, this.rightModel, this.rightResource, this.rightEntityClass, f, f2, f3, f4, f5, f6, f7, false);
            this.rightUniqueId = dataHolder.entityId;
            this.rightRenderer = dataHolder.renderer;
            this.rightResource = dataHolder.textureLocation;
            this.rightModel = dataHolder.model;
            this.rightEntityClass = dataHolder.clazz;
        }
        GlStateManager.disableRescaleNormal();
    }

    private DataHolder renderEntityOnShoulder(EntityPlayer entityPlayer, @Nullable UUID uUID2, NBTTagCompound nBTTagCompound, RenderLivingBase<? extends EntityLivingBase> renderParrot2, ModelBase \u260332, ResourceLocation \u260342, Class<?> \u260322, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl) {
        ResourceLocation \u260342;
        ModelBase \u260332;
        Class \u260322;
        if (uUID2 == null || !uUID2.equals((Object)nBTTagCompound.getUniqueId("UUID"))) {
            UUID uUID2 = nBTTagCompound.getUniqueId("UUID");
            \u260322 = EntityList.getClassFromName((String)nBTTagCompound.getString("id"));
            if (\u260322 == EntityParrot.class) {
                RenderParrot renderParrot2 = new RenderParrot(this.renderManager);
                \u260332 = new ModelParrot();
                \u260342 = RenderParrot.PARROT_TEXTURES[nBTTagCompound.getInteger("Variant")];
            }
        }
        renderParrot2.bindTexture(\u260342);
        GlStateManager.pushMatrix();
        float f8 = entityPlayer.aU() ? -1.3f : -1.5f;
        \u2603 = bl ? 0.4f : -0.4f;
        GlStateManager.translate((float)\u2603, (float)f8, (float)0.0f);
        if (\u260322 == EntityParrot.class) {
            f4 = 0.0f;
        }
        \u260332.setLivingAnimations((EntityLivingBase)entityPlayer, f, f2, f3);
        \u260332.setRotationAngles(f, f2, f4, f5, f6, f7, (Entity)entityPlayer);
        \u260332.render((Entity)entityPlayer, f, f2, f4, f5, f6, f7);
        GlStateManager.popMatrix();
        return new DataHolder(this, uUID2, (RenderLivingBase)renderParrot2, \u260332, \u260342, \u260322);
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
