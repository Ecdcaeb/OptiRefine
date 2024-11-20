/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  java.lang.Math
 *  java.lang.Object
 *  java.util.Objects
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.ItemRenderer$1
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.entity.RenderPlayer
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemMap
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.EnumBlockRenderType
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.EnumHandSide
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraft.world.storage.MapData
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType
 *  net.minecraftforge.event.ForgeEventFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class ItemRenderer {
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    private final Minecraft mc;
    private ItemStack itemStackMainHand = ItemStack.EMPTY;
    private ItemStack itemStackOffHand = ItemStack.EMPTY;
    private float equippedProgressMainHand;
    private float prevEquippedProgressMainHand;
    private float equippedProgressOffHand;
    private float prevEquippedProgressOffHand;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;

    public ItemRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {
        this.renderItemSide(entityIn, heldStack, transform, false);
    }

    public void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded) {
        if (!heldStack.isEmpty()) {
            boolean flag;
            Item item = heldStack.getItem();
            Block block = Block.getBlockFromItem((Item)item);
            GlStateManager.pushMatrix();
            boolean bl = flag = this.itemRenderer.shouldRenderItemIn3D(heldStack) && block.getRenderLayer() == BlockRenderLayer.TRANSLUCENT;
            if (flag) {
                GlStateManager.depthMask((boolean)false);
            }
            this.itemRenderer.renderItem(heldStack, entitylivingbaseIn, transform, leftHanded);
            if (flag) {
                GlStateManager.depthMask((boolean)true);
            }
            GlStateManager.popMatrix();
        }
    }

    private void rotateArroundXAndY(float angle, float angleY) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate((float)angle, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)angleY, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void setLightmap() {
        EntityPlayerSP abstractclientplayer = this.mc.player;
        int i = this.mc.world.getCombinedLight(new BlockPos(abstractclientplayer.p, abstractclientplayer.q + (double)abstractclientplayer.getEyeHeight(), abstractclientplayer.r), 0);
        float f = i & 0xFFFF;
        float f1 = i >> 16;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)f, (float)f1);
    }

    private void rotateArm(float p_187458_1_) {
        EntityPlayerSP entityplayersp = this.mc.player;
        float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
        float f1 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
        GlStateManager.rotate((float)((entityplayersp.w - f) * 0.1f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((entityplayersp.v - f1) * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
    }

    private float getMapAngleFromPitch(float pitch) {
        float f = 1.0f - pitch / 45.0f + 0.1f;
        f = MathHelper.clamp((float)f, (float)0.0f, (float)1.0f);
        f = -MathHelper.cos((float)(f * (float)Math.PI)) * 0.5f + 0.5f;
        return f;
    }

    private void renderArms() {
        if (!this.mc.player.aX()) {
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            this.renderArm(EnumHandSide.RIGHT);
            this.renderArm(EnumHandSide.LEFT);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }

    private void renderArm(EnumHandSide p_187455_1_) {
        this.mc.getTextureManager().bindTexture(this.mc.player.m());
        Render render = this.renderManager.getEntityRenderObject((Entity)this.mc.player);
        RenderPlayer renderplayer = (RenderPlayer)render;
        GlStateManager.pushMatrix();
        float f = p_187455_1_ == EnumHandSide.RIGHT ? 1.0f : -1.0f;
        GlStateManager.rotate((float)92.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)45.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)(f * -41.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.translate((float)(f * 0.3f), (float)-1.1f, (float)0.45f);
        if (p_187455_1_ == EnumHandSide.RIGHT) {
            renderplayer.renderRightArm((AbstractClientPlayer)this.mc.player);
        } else {
            renderplayer.renderLeftArm((AbstractClientPlayer)this.mc.player);
        }
        GlStateManager.popMatrix();
    }

    private void renderMapFirstPersonSide(float p_187465_1_, EnumHandSide hand, float p_187465_3_, ItemStack stack) {
        float f = hand == EnumHandSide.RIGHT ? 1.0f : -1.0f;
        GlStateManager.translate((float)(f * 0.125f), (float)-0.125f, (float)0.0f);
        if (!this.mc.player.aX()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate((float)(f * 10.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            this.renderArmFirstPerson(p_187465_1_, p_187465_3_, hand);
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(f * 0.51f), (float)(-0.08f + p_187465_1_ * -1.2f), (float)-0.75f);
        float f1 = MathHelper.sqrt((float)p_187465_3_);
        float f2 = MathHelper.sin((float)(f1 * (float)Math.PI));
        float f3 = -0.5f * f2;
        float f4 = 0.4f * MathHelper.sin((float)(f1 * ((float)Math.PI * 2)));
        float f5 = -0.3f * MathHelper.sin((float)(p_187465_3_ * (float)Math.PI));
        GlStateManager.translate((float)(f * f3), (float)(f4 - 0.3f * f2), (float)f5);
        GlStateManager.rotate((float)(f2 * -45.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)(f * f2 * -30.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        this.renderMapFirstPerson(stack);
        GlStateManager.popMatrix();
    }

    private void renderMapFirstPerson(float p_187463_1_, float p_187463_2_, float p_187463_3_) {
        float f = MathHelper.sqrt((float)p_187463_3_);
        float f1 = -0.2f * MathHelper.sin((float)(p_187463_3_ * (float)Math.PI));
        float f2 = -0.4f * MathHelper.sin((float)(f * (float)Math.PI));
        GlStateManager.translate((float)0.0f, (float)(-f1 / 2.0f), (float)f2);
        float f3 = this.getMapAngleFromPitch(p_187463_1_);
        GlStateManager.translate((float)0.0f, (float)(0.04f + p_187463_2_ * -1.2f + f3 * -0.5f), (float)-0.72f);
        GlStateManager.rotate((float)(f3 * -85.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        this.renderArms();
        float f4 = MathHelper.sin((float)(f * (float)Math.PI));
        GlStateManager.rotate((float)(f4 * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        this.renderMapFirstPerson(this.itemStackMainHand);
    }

    private void renderMapFirstPerson(ItemStack stack) {
        GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.scale((float)0.38f, (float)0.38f, (float)0.38f);
        GlStateManager.disableLighting();
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)0.0f);
        GlStateManager.scale((float)0.0078125f, (float)0.0078125f, (float)0.0078125f);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        bufferbuilder.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        bufferbuilder.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        bufferbuilder.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        MapData mapdata = ((ItemMap)stack.getItem()).getMapData(stack, (World)this.mc.world);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
        GlStateManager.enableLighting();
    }

    private void renderArmFirstPerson(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_) {
        boolean flag = p_187456_3_ != EnumHandSide.LEFT;
        float f = flag ? 1.0f : -1.0f;
        float f1 = MathHelper.sqrt((float)p_187456_2_);
        float f2 = -0.3f * MathHelper.sin((float)(f1 * (float)Math.PI));
        float f3 = 0.4f * MathHelper.sin((float)(f1 * ((float)Math.PI * 2)));
        float f4 = -0.4f * MathHelper.sin((float)(p_187456_2_ * (float)Math.PI));
        GlStateManager.translate((float)(f * (f2 + 0.64000005f)), (float)(f3 + -0.6f + p_187456_1_ * -0.6f), (float)(f4 + -0.71999997f));
        GlStateManager.rotate((float)(f * 45.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        float f5 = MathHelper.sin((float)(p_187456_2_ * p_187456_2_ * (float)Math.PI));
        float f6 = MathHelper.sin((float)(f1 * (float)Math.PI));
        GlStateManager.rotate((float)(f * f6 * 70.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f * f5 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        EntityPlayerSP abstractclientplayer = this.mc.player;
        this.mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.translate((float)(f * -1.0f), (float)3.6f, (float)3.5f);
        GlStateManager.rotate((float)(f * 120.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)200.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)(f * -135.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.translate((float)(f * 5.6f), (float)0.0f, (float)0.0f);
        RenderPlayer renderplayer = (RenderPlayer)this.renderManager.getEntityRenderObject((Entity)abstractclientplayer);
        GlStateManager.disableCull();
        if (flag) {
            renderplayer.renderRightArm((AbstractClientPlayer)abstractclientplayer);
        } else {
            renderplayer.renderLeftArm((AbstractClientPlayer)abstractclientplayer);
        }
        GlStateManager.enableCull();
    }

    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack) {
        float f = (float)this.mc.player.cK() - p_187454_1_ + 1.0f;
        float f1 = f / (float)stack.getMaxItemUseDuration();
        if (f1 < 0.8f) {
            float f2 = MathHelper.abs((float)(MathHelper.cos((float)(f / 4.0f * (float)Math.PI)) * 0.1f));
            GlStateManager.translate((float)0.0f, (float)f2, (float)0.0f);
        }
        float f3 = 1.0f - (float)Math.pow((double)f1, (double)27.0);
        int i = hand == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)(f3 * 0.6f * (float)i), (float)(f3 * -0.5f), (float)(f3 * 0.0f));
        GlStateManager.rotate((float)((float)i * f3 * 90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f3 * 10.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)i * f3 * 30.0f), (float)0.0f, (float)0.0f, (float)1.0f);
    }

    private void transformFirstPerson(EnumHandSide hand, float p_187453_2_) {
        int i = hand == EnumHandSide.RIGHT ? 1 : -1;
        float f = MathHelper.sin((float)(p_187453_2_ * p_187453_2_ * (float)Math.PI));
        GlStateManager.rotate((float)((float)i * (45.0f + f * -20.0f)), (float)0.0f, (float)1.0f, (float)0.0f);
        float f1 = MathHelper.sin((float)(MathHelper.sqrt((float)p_187453_2_) * (float)Math.PI));
        GlStateManager.rotate((float)((float)i * f1 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)(f1 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)i * -45.0f), (float)0.0f, (float)1.0f, (float)0.0f);
    }

    private void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_) {
        int i = hand == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)((float)i * 0.56f), (float)(-0.52f + p_187459_2_ * -0.6f), (float)-0.72f);
    }

    public void renderItemInFirstPerson(float partialTicks) {
        float f6;
        float f4;
        float f5;
        float f3;
        ItemStack itemstack;
        EntityPlayerSP abstractclientplayer = this.mc.player;
        float f = abstractclientplayer.l(partialTicks);
        EnumHand enumhand = (EnumHand)MoreObjects.firstNonNull((Object)abstractclientplayer.av, (Object)EnumHand.MAIN_HAND);
        float f1 = abstractclientplayer.y + (abstractclientplayer.w - abstractclientplayer.y) * partialTicks;
        float f2 = abstractclientplayer.x + (abstractclientplayer.v - abstractclientplayer.x) * partialTicks;
        boolean flag = true;
        boolean flag1 = true;
        if (abstractclientplayer.cG() && (itemstack = abstractclientplayer.cJ()).getItem() instanceof ItemBow) {
            EnumHand enumhand1 = abstractclientplayer.cH();
            flag = enumhand1 == EnumHand.MAIN_HAND;
            flag1 = !flag;
        }
        this.rotateArroundXAndY(f1, f2);
        this.setLightmap();
        this.rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();
        if (flag && !ForgeHooksClient.renderSpecificFirstPersonHand((EnumHand)EnumHand.MAIN_HAND, (float)partialTicks, (float)f1, (float)(f3 = enumhand == EnumHand.MAIN_HAND ? f : 0.0f), (float)(f5 = 1.0f - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * partialTicks)), (ItemStack)this.itemStackMainHand)) {
            this.renderItemInFirstPerson((AbstractClientPlayer)abstractclientplayer, partialTicks, f1, EnumHand.MAIN_HAND, f3, this.itemStackMainHand, f5);
        }
        if (flag1 && !ForgeHooksClient.renderSpecificFirstPersonHand((EnumHand)EnumHand.OFF_HAND, (float)partialTicks, (float)f1, (float)(f4 = enumhand == EnumHand.OFF_HAND ? f : 0.0f), (float)(f6 = 1.0f - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * partialTicks)), (ItemStack)this.itemStackOffHand)) {
            this.renderItemInFirstPerson((AbstractClientPlayer)abstractclientplayer, partialTicks, f1, EnumHand.OFF_HAND, f4, this.itemStackOffHand, f6);
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    public void renderItemInFirstPerson(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_) {
        boolean flag = hand == EnumHand.MAIN_HAND;
        EnumHandSide enumhandside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
        GlStateManager.pushMatrix();
        if (stack.isEmpty()) {
            if (flag && !player.aX()) {
                this.renderArmFirstPerson(p_187457_7_, p_187457_5_, enumhandside);
            }
        } else if (stack.getItem() instanceof ItemMap) {
            if (flag && this.itemStackOffHand.isEmpty()) {
                this.renderMapFirstPerson(p_187457_3_, p_187457_7_, p_187457_5_);
            } else {
                this.renderMapFirstPersonSide(p_187457_7_, enumhandside, p_187457_5_, stack);
            }
        } else {
            boolean flag1;
            boolean bl = flag1 = enumhandside == EnumHandSide.RIGHT;
            if (player.cG() && player.cK() > 0 && player.cH() == hand) {
                int j = flag1 ? 1 : -1;
                switch (1.$SwitchMap$net$minecraft$item$EnumAction[stack.getItemUseAction().ordinal()]) {
                    case 1: {
                        this.transformSideFirstPerson(enumhandside, p_187457_7_);
                        break;
                    }
                    case 2: 
                    case 3: {
                        this.transformEatFirstPerson(p_187457_2_, enumhandside, stack);
                        this.transformSideFirstPerson(enumhandside, p_187457_7_);
                        break;
                    }
                    case 4: {
                        this.transformSideFirstPerson(enumhandside, p_187457_7_);
                        break;
                    }
                    case 5: {
                        this.transformSideFirstPerson(enumhandside, p_187457_7_);
                        GlStateManager.translate((float)((float)j * -0.2785682f), (float)0.18344387f, (float)0.15731531f);
                        GlStateManager.rotate((float)-13.935f, (float)1.0f, (float)0.0f, (float)0.0f);
                        GlStateManager.rotate((float)((float)j * 35.3f), (float)0.0f, (float)1.0f, (float)0.0f);
                        GlStateManager.rotate((float)((float)j * -9.785f), (float)0.0f, (float)0.0f, (float)1.0f);
                        float f5 = (float)stack.getMaxItemUseDuration() - ((float)this.mc.player.cK() - p_187457_2_ + 1.0f);
                        float f6 = f5 / 20.0f;
                        f6 = (f6 * f6 + f6 * 2.0f) / 3.0f;
                        if (f6 > 1.0f) {
                            f6 = 1.0f;
                        }
                        if (f6 > 0.1f) {
                            float f7 = MathHelper.sin((float)((f5 - 0.1f) * 1.3f));
                            float f3 = f6 - 0.1f;
                            float f4 = f7 * f3;
                            GlStateManager.translate((float)(f4 * 0.0f), (float)(f4 * 0.004f), (float)(f4 * 0.0f));
                        }
                        GlStateManager.translate((float)(f6 * 0.0f), (float)(f6 * 0.0f), (float)(f6 * 0.04f));
                        GlStateManager.scale((float)1.0f, (float)1.0f, (float)(1.0f + f6 * 0.2f));
                        GlStateManager.rotate((float)((float)j * 45.0f), (float)0.0f, (float)-1.0f, (float)0.0f);
                    }
                }
            } else {
                float f = -0.4f * MathHelper.sin((float)(MathHelper.sqrt((float)p_187457_5_) * (float)Math.PI));
                float f1 = 0.2f * MathHelper.sin((float)(MathHelper.sqrt((float)p_187457_5_) * ((float)Math.PI * 2)));
                float f2 = -0.2f * MathHelper.sin((float)(p_187457_5_ * (float)Math.PI));
                int i = flag1 ? 1 : -1;
                GlStateManager.translate((float)((float)i * f), (float)f1, (float)f2);
                this.transformSideFirstPerson(enumhandside, p_187457_7_);
                this.transformFirstPerson(enumhandside, p_187457_5_);
            }
            this.renderItemSide((EntityLivingBase)player, stack, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
        }
        GlStateManager.popMatrix();
    }

    public void renderOverlays(float partialTicks) {
        GlStateManager.disableAlpha();
        if (this.mc.player.aD()) {
            IBlockState iblockstate = this.mc.world.getBlockState(new BlockPos((Entity)this.mc.player));
            BlockPos overlayPos = new BlockPos((Entity)this.mc.player);
            EntityPlayerSP entityplayer = this.mc.player;
            for (int i = 0; i < 8; ++i) {
                double d0 = entityplayer.p + (double)(((float)((i >> 0) % 2) - 0.5f) * entityplayer.G * 0.8f);
                double d1 = entityplayer.q + (double)(((float)((i >> 1) % 2) - 0.5f) * 0.1f);
                double d2 = entityplayer.r + (double)(((float)((i >> 2) % 2) - 0.5f) * entityplayer.G * 0.8f);
                BlockPos blockpos = new BlockPos(d0, d1 + (double)entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = this.mc.world.getBlockState(blockpos);
                if (!iblockstate1.r()) continue;
                iblockstate = iblockstate1;
                overlayPos = blockpos;
            }
            if (iblockstate.i() != EnumBlockRenderType.INVISIBLE && !ForgeEventFactory.renderBlockOverlay((EntityPlayer)this.mc.player, (float)partialTicks, (RenderBlockOverlayEvent.OverlayType)RenderBlockOverlayEvent.OverlayType.BLOCK, (IBlockState)iblockstate, (BlockPos)overlayPos)) {
                this.renderSuffocationOverlay(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
            }
        }
        if (!this.mc.player.y()) {
            if (this.mc.player.a(Material.WATER) && !ForgeEventFactory.renderWaterOverlay((EntityPlayer)this.mc.player, (float)partialTicks)) {
                this.renderWaterOverlayTexture(partialTicks);
            }
            if (this.mc.player.aR() && !ForgeEventFactory.renderFireOverlay((EntityPlayer)this.mc.player, (float)partialTicks)) {
                this.renderFireInFirstPerson();
            }
        }
        GlStateManager.enableAlpha();
    }

    private void renderSuffocationOverlay(TextureAtlasSprite sprite) {
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = 0.1f;
        GlStateManager.color((float)0.1f, (float)0.1f, (float)0.1f, (float)0.5f);
        GlStateManager.pushMatrix();
        float f1 = -1.0f;
        float f2 = 1.0f;
        float f3 = -1.0f;
        float f4 = 1.0f;
        float f5 = -0.5f;
        float f6 = sprite.getMinU();
        float f7 = sprite.getMaxU();
        float f8 = sprite.getMinV();
        float f9 = sprite.getMaxV();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-1.0, -1.0, -0.5).tex((double)f7, (double)f9).endVertex();
        bufferbuilder.pos(1.0, -1.0, -0.5).tex((double)f6, (double)f9).endVertex();
        bufferbuilder.pos(1.0, 1.0, -0.5).tex((double)f6, (double)f8).endVertex();
        bufferbuilder.pos(-1.0, 1.0, -0.5).tex((double)f7, (double)f8).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private void renderWaterOverlayTexture(float partialTicks) {
        this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = this.mc.player.aw();
        GlStateManager.color((float)f, (float)f, (float)f, (float)0.5f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        float f1 = 4.0f;
        float f2 = -1.0f;
        float f3 = 1.0f;
        float f4 = -1.0f;
        float f5 = 1.0f;
        float f6 = -0.5f;
        float f7 = -this.mc.player.v / 64.0f;
        float f8 = this.mc.player.w / 64.0f;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-1.0, -1.0, -0.5).tex((double)(4.0f + f7), (double)(4.0f + f8)).endVertex();
        bufferbuilder.pos(1.0, -1.0, -0.5).tex((double)(0.0f + f7), (double)(4.0f + f8)).endVertex();
        bufferbuilder.pos(1.0, 1.0, -0.5).tex((double)(0.0f + f7), (double)(0.0f + f8)).endVertex();
        bufferbuilder.pos(-1.0, 1.0, -0.5).tex((double)(4.0f + f7), (double)(0.0f + f8)).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.disableBlend();
    }

    private void renderFireInFirstPerson() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
        GlStateManager.depthFunc((int)519);
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        float f = 1.0f;
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            float f5 = -0.5f;
            float f6 = 0.5f;
            float f7 = -0.5f;
            float f8 = 0.5f;
            float f9 = -0.5f;
            GlStateManager.translate((float)((float)(-(i * 2 - 1)) * 0.24f), (float)-0.3f, (float)0.0f);
            GlStateManager.rotate((float)((float)(i * 2 - 1) * 10.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-0.5, -0.5, -0.5).tex((double)f2, (double)f4).endVertex();
            bufferbuilder.pos(0.5, -0.5, -0.5).tex((double)f1, (double)f4).endVertex();
            bufferbuilder.pos(0.5, 0.5, -0.5).tex((double)f1, (double)f3).endVertex();
            bufferbuilder.pos(-0.5, 0.5, -0.5).tex((double)f2, (double)f3).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.depthFunc((int)515);
    }

    public void updateEquippedItem() {
        this.prevEquippedProgressMainHand = this.equippedProgressMainHand;
        this.prevEquippedProgressOffHand = this.equippedProgressOffHand;
        EntityPlayerSP entityplayersp = this.mc.player;
        ItemStack itemstack = entityplayersp.co();
        ItemStack itemstack1 = entityplayersp.cp();
        if (entityplayersp.isRowingBoat()) {
            this.equippedProgressMainHand = MathHelper.clamp((float)(this.equippedProgressMainHand - 0.4f), (float)0.0f, (float)1.0f);
            this.equippedProgressOffHand = MathHelper.clamp((float)(this.equippedProgressOffHand - 0.4f), (float)0.0f, (float)1.0f);
        } else {
            float f = entityplayersp.n(1.0f);
            boolean requipM = ForgeHooksClient.shouldCauseReequipAnimation((ItemStack)this.itemStackMainHand, (ItemStack)itemstack, (int)entityplayersp.bv.currentItem);
            boolean requipO = ForgeHooksClient.shouldCauseReequipAnimation((ItemStack)this.itemStackOffHand, (ItemStack)itemstack1, (int)-1);
            if (!requipM && !Objects.equals((Object)this.itemStackMainHand, (Object)itemstack)) {
                this.itemStackMainHand = itemstack;
            }
            if (!requipM && !Objects.equals((Object)this.itemStackOffHand, (Object)itemstack1)) {
                this.itemStackOffHand = itemstack1;
            }
            this.equippedProgressMainHand += MathHelper.clamp((float)((!requipM ? f * f * f : 0.0f) - this.equippedProgressMainHand), (float)-0.4f, (float)0.4f);
            this.equippedProgressOffHand += MathHelper.clamp((float)((float)(!requipO ? 1 : 0) - this.equippedProgressOffHand), (float)-0.4f, (float)0.4f);
        }
        if (this.equippedProgressMainHand < 0.1f) {
            this.itemStackMainHand = itemstack;
        }
        if (this.equippedProgressOffHand < 0.1f) {
            this.itemStackOffHand = itemstack1;
        }
    }

    public void resetEquippedProgress(EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            this.equippedProgressMainHand = 0.0f;
        } else {
            this.equippedProgressOffHand = 0.0f;
        }
    }
}
