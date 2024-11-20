/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.collect.Maps
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.layers.LayerArmorBase$1
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.optifine.CustomItems
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorForge
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.shaders.Shaders
 *  net.optifine.shaders.ShadersRender
 */
package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;

public abstract class LayerArmorBase<T extends ModelBase>
implements LayerRenderer<EntityLivingBase> {
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    protected T modelLeggings;
    protected T modelArmor;
    private final RenderLivingBase<?> renderer;
    private float alpha = 1.0f;
    private float colorR = 1.0f;
    private float colorG = 1.0f;
    private float colorB = 1.0f;
    private boolean skipRenderGlint;
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    public LayerArmorBase(RenderLivingBase<?> rendererIn) {
        this.renderer = rendererIn;
        this.initArmor();
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn) {
        ItemArmor itemarmor;
        ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);
        if (itemstack.getItem() instanceof ItemArmor && (itemarmor = (ItemArmor)itemstack.getItem()).getEquipmentSlot() == slotIn) {
            T t = this.getModelFromSlot(slotIn);
            if (Reflector.ForgeHooksClient.exists()) {
                t = this.getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t);
            }
            t.setModelAttributes(this.renderer.getMainModel());
            t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.setModelSlotVisible(t, slotIn);
            boolean flag = this.isLegSlot(slotIn);
            if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture((ItemStack)itemstack, (EntityEquipmentSlot)slotIn, null)) {
                if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                    this.renderer.bindTexture(this.getArmorResource((Entity)entityLivingBaseIn, itemstack, slotIn, null));
                } else {
                    this.renderer.bindTexture(this.getArmorResource(itemarmor, flag));
                }
            }
            if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                if (ReflectorForge.armorHasOverlay((ItemArmor)itemarmor, (ItemStack)itemstack)) {
                    int i = itemarmor.getColor(itemstack);
                    float f = (float)(i >> 16 & 0xFF) / 255.0f;
                    float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
                    float f2 = (float)(i & 0xFF) / 255.0f;
                    GlStateManager.color((float)(this.colorR * f), (float)(this.colorG * f1), (float)(this.colorB * f2), (float)this.alpha);
                    t.render((Entity)entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture((ItemStack)itemstack, (EntityEquipmentSlot)slotIn, (String)"overlay")) {
                        this.renderer.bindTexture(this.getArmorResource((Entity)entityLivingBaseIn, itemstack, slotIn, "overlay"));
                    }
                }
                GlStateManager.color((float)this.colorR, (float)this.colorG, (float)this.colorB, (float)this.alpha);
                t.render((Entity)entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (!(this.skipRenderGlint || !itemstack.hasEffect() || Config.isCustomItems() && CustomItems.renderCustomArmorEffect((EntityLivingBase)entityLivingBaseIn, (ItemStack)itemstack, t, (float)limbSwing, (float)limbSwingAmount, (float)partialTicks, (float)ageInTicks, (float)netHeadYaw, (float)headPitch, (float)scale))) {
                    LayerArmorBase.renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
                return;
            }
            switch (1.$SwitchMap$net$minecraft$item$ItemArmor$ArmorMaterial[itemarmor.getArmorMaterial().ordinal()]) {
                case 1: {
                    int i = itemarmor.getColor(itemstack);
                    float f = (float)(i >> 16 & 0xFF) / 255.0f;
                    float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
                    float f2 = (float)(i & 0xFF) / 255.0f;
                    GlStateManager.color((float)(this.colorR * f), (float)(this.colorG * f1), (float)(this.colorB * f2), (float)this.alpha);
                    t.render((Entity)entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture((ItemStack)itemstack, (EntityEquipmentSlot)slotIn, (String)"overlay")) {
                        this.renderer.bindTexture(this.getArmorResource(itemarmor, flag, "overlay"));
                    }
                }
                case 2: 
                case 3: 
                case 4: 
                case 5: {
                    GlStateManager.color((float)this.colorR, (float)this.colorG, (float)this.colorB, (float)this.alpha);
                    t.render((Entity)entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
            if (!(this.skipRenderGlint || !itemstack.isItemEnchanted() || Config.isCustomItems() && CustomItems.renderCustomArmorEffect((EntityLivingBase)entityLivingBaseIn, (ItemStack)itemstack, t, (float)limbSwing, (float)limbSwingAmount, (float)partialTicks, (float)ageInTicks, (float)netHeadYaw, (float)headPitch, (float)scale))) {
                LayerArmorBase.renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }

    public T getModelFromSlot(EntityEquipmentSlot slotIn) {
        return this.isLegSlot(slotIn) ? this.modelLeggings : this.modelArmor;
    }

    private boolean isLegSlot(EntityEquipmentSlot slotIn) {
        return slotIn == EntityEquipmentSlot.LEGS;
    }

    public static void renderEnchantedGlint(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_) {
        if (Config.isShaders() && Shaders.isShadowPass) {
            return;
        }
        float f = (float)p_188364_1_.T + p_188364_5_;
        p_188364_0_.bindTexture(ENCHANTED_ITEM_GLINT_RES);
        if (Config.isShaders()) {
            ShadersRender.renderEnchantedGlintBegin();
        }
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc((int)514);
        GlStateManager.depthMask((boolean)false);
        float f1 = 0.5f;
        GlStateManager.color((float)0.5f, (float)0.5f, (float)0.5f, (float)1.0f);
        for (int i = 0; i < 2; ++i) {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_COLOR, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
            float f2 = 0.76f;
            GlStateManager.color((float)0.38f, (float)0.19f, (float)0.608f, (float)1.0f);
            GlStateManager.matrixMode((int)5890);
            GlStateManager.loadIdentity();
            float f3 = 0.33333334f;
            GlStateManager.scale((float)0.33333334f, (float)0.33333334f, (float)0.33333334f);
            GlStateManager.rotate((float)(30.0f - (float)i * 60.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.translate((float)0.0f, (float)(f * (0.001f + (float)i * 0.003f) * 20.0f), (float)0.0f);
            GlStateManager.matrixMode((int)5888);
            model.render((Entity)p_188364_1_, p_188364_3_, p_188364_4_, p_188364_6_, p_188364_7_, p_188364_8_, p_188364_9_);
            GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        }
        GlStateManager.matrixMode((int)5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.depthFunc((int)515);
        GlStateManager.disableBlend();
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        if (Config.isShaders()) {
            ShadersRender.renderEnchantedGlintEnd();
        }
    }

    private ResourceLocation getArmorResource(ItemArmor armor, boolean p_177181_2_) {
        return this.getArmorResource(armor, p_177181_2_, null);
    }

    private ResourceLocation getArmorResource(ItemArmor armor, boolean p_177178_2_, String p_177178_3_) {
        String s = String.format((String)"textures/models/armor/%s_layer_%d%s.png", (Object[])new Object[]{armor.getArmorMaterial().getName(), p_177178_2_ ? 2 : 1, p_177178_3_ == null ? "" : String.format((String)"_%s", (Object[])new Object[]{p_177178_3_})});
        ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get((Object)s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            ARMOR_TEXTURE_RES_MAP.put((Object)s, (Object)resourcelocation);
        }
        return resourcelocation;
    }

    protected abstract void initArmor();

    protected abstract void setModelSlotVisible(T var1, EntityEquipmentSlot var2);

    protected T getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, T model) {
        return model;
    }

    public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type) {
        ItemArmor item = (ItemArmor)stack.getItem();
        String texture = item.getArmorMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(58);
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format((String)"%s:textures/models/armor/%s_layer_%d%s.png", (Object[])new Object[]{domain, texture, this.isLegSlot(slot) ? 2 : 1, type == null ? "" : String.format((String)"_%s", (Object[])new Object[]{type})});
        ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get((Object)(s1 = Reflector.callString((ReflectorMethod)Reflector.ForgeHooksClient_getArmorTexture, (Object[])new Object[]{entity, stack, s1, slot, type})));
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put((Object)s1, (Object)resourcelocation);
        }
        return resourcelocation;
    }
}
