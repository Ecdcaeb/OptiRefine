/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.mojang.authlib.GameProfile
 *  java.io.File
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.UUID
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.client.renderer.IImageBuffer
 *  net.minecraft.client.renderer.ImageBufferDownload
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.attributes.IAttributeInstance
 *  net.minecraft.entity.passive.EntityShoulderRiding
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemBow
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.StringUtils
 *  net.minecraft.world.GameType
 *  net.minecraft.world.World
 *  net.optifine.player.CapeUtils
 *  net.optifine.player.PlayerConfigurations
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 */
package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.optifine.player.CapeUtils;
import net.optifine.player.PlayerConfigurations;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;

public abstract class AbstractClientPlayer
extends EntityPlayer {
    private NetworkPlayerInfo playerInfo;
    public float rotateElytraX;
    public float rotateElytraY;
    public float rotateElytraZ;
    private ResourceLocation locationOfCape = null;
    private long reloadCapeTimeMs = 0L;
    private boolean elytraOfCape = false;
    private String nameClear = null;
    public EntityShoulderRiding entityShoulderLeft;
    public EntityShoulderRiding entityShoulderRight;
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");

    public AbstractClientPlayer(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
        this.nameClear = playerProfile.getName();
        if (this.nameClear != null && !this.nameClear.isEmpty()) {
            this.nameClear = StringUtils.stripControlCodes((String)this.nameClear);
        }
        CapeUtils.downloadCape((AbstractClientPlayer)this);
        PlayerConfigurations.getPlayerConfiguration((AbstractClientPlayer)this);
    }

    public boolean isSpectator() {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(this.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == GameType.SPECTATOR;
    }

    public boolean isCreative() {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(this.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == GameType.CREATIVE;
    }

    public boolean hasPlayerInfo() {
        return this.getPlayerInfo() != null;
    }

    @Nullable
    protected NetworkPlayerInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            this.playerInfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(this.bm());
        }
        return this.playerInfo;
    }

    public boolean hasSkin() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo != null && networkplayerinfo.hasLocationSkin();
    }

    public ResourceLocation getLocationSkin() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultSkin((UUID)this.bm()) : networkplayerinfo.getLocationSkin();
    }

    @Nullable
    public ResourceLocation getLocationCape() {
        if (!Config.isShowCapes()) {
            return null;
        }
        if (this.reloadCapeTimeMs != 0L && System.currentTimeMillis() > this.reloadCapeTimeMs) {
            CapeUtils.reloadCape((AbstractClientPlayer)this);
            this.reloadCapeTimeMs = 0L;
        }
        if (this.locationOfCape != null) {
            return this.locationOfCape;
        }
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
    }

    public boolean isPlayerInfoSet() {
        return this.getPlayerInfo() != null;
    }

    @Nullable
    public ResourceLocation getLocationElytra() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? null : networkplayerinfo.getLocationElytra();
    }

    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourceLocationIn);
        if (itextureobject == null) {
            itextureobject = new ThreadDownloadImageData((File)null, String.format((String)"http://skins.minecraft.net/MinecraftSkins/%s.png", (Object[])new Object[]{StringUtils.stripControlCodes((String)username)}), DefaultPlayerSkin.getDefaultSkin((UUID)AbstractClientPlayer.getOfflineUUID((String)username)), (IImageBuffer)new ImageBufferDownload());
            texturemanager.loadTexture(resourceLocationIn, itextureobject);
        }
        return (ThreadDownloadImageData)itextureobject;
    }

    public static ResourceLocation getLocationSkin(String username) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes((String)username));
    }

    public String getSkinType() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? DefaultPlayerSkin.getSkinType((UUID)this.bm()) : networkplayerinfo.getSkinType();
    }

    public float getFovModifier() {
        float f = 1.0f;
        if (this.capabilities.isFlying) {
            f *= 1.1f;
        }
        IAttributeInstance iattributeinstance = this.a(SharedMonsterAttributes.MOVEMENT_SPEED);
        f = (float)((double)f * ((iattributeinstance.getAttributeValue() / (double)this.capabilities.getWalkSpeed() + 1.0) / 2.0));
        if (this.capabilities.getWalkSpeed() == 0.0f || Float.isNaN((float)f) || Float.isInfinite((float)f)) {
            f = 1.0f;
        }
        if (this.cG() && this.cJ().getItem() instanceof ItemBow) {
            int i = this.cL();
            float f1 = (float)i / 20.0f;
            f1 = f1 > 1.0f ? 1.0f : (f1 *= f1);
            f *= 1.0f - f1 * 0.15f;
        }
        if (Reflector.ForgeHooksClient_getOffsetFOV.exists()) {
            return Reflector.callFloat((ReflectorMethod)Reflector.ForgeHooksClient_getOffsetFOV, (Object[])new Object[]{this, Float.valueOf((float)f)});
        }
        return f;
    }

    public String getNameClear() {
        return this.nameClear;
    }

    public ResourceLocation getLocationOfCape() {
        return this.locationOfCape;
    }

    public void setLocationOfCape(ResourceLocation locationOfCape) {
        this.locationOfCape = locationOfCape;
    }

    public boolean hasElytraCape() {
        ResourceLocation loc = this.getLocationCape();
        if (loc == null) {
            return false;
        }
        if (loc == this.locationOfCape) {
            return this.elytraOfCape;
        }
        return true;
    }

    public void setElytraOfCape(boolean elytraOfCape) {
        this.elytraOfCape = elytraOfCape;
    }

    public boolean isElytraOfCape() {
        return this.elytraOfCape;
    }

    public long getReloadCapeTimeMs() {
        return this.reloadCapeTimeMs;
    }

    public void setReloadCapeTimeMs(long reloadCapeTimeMs) {
        this.reloadCapeTimeMs = reloadCapeTimeMs;
    }
}
