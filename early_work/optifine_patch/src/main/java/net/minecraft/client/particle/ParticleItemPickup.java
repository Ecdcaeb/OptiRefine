/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Object
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 *  net.optifine.shaders.Program
 *  net.optifine.shaders.Shaders
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;

public class ParticleItemPickup
extends Particle {
    private final Entity item;
    private final Entity target;
    private int age;
    private final int maxAge;
    private final float yOffset;
    private final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

    public ParticleItemPickup(World worldIn, Entity p_i1233_2_, Entity p_i1233_3_, float p_i1233_4_) {
        super(worldIn, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ, p_i1233_2_.motionX, p_i1233_2_.motionY, p_i1233_2_.motionZ);
        this.item = p_i1233_2_;
        this.target = p_i1233_3_;
        this.maxAge = 3;
        this.yOffset = p_i1233_4_;
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Program oldShadersProgram = null;
        if (Config.isShaders()) {
            oldShadersProgram = Shaders.activeProgram;
            Shaders.nextEntity((Entity)this.item);
        }
        float f = ((float)this.age + partialTicks) / (float)this.maxAge;
        f *= f;
        double d0 = this.item.posX;
        double d1 = this.item.posY;
        double d2 = this.item.posZ;
        double d3 = this.target.lastTickPosX + (this.target.posX - this.target.lastTickPosX) * (double)partialTicks;
        double d4 = this.target.lastTickPosY + (this.target.posY - this.target.lastTickPosY) * (double)partialTicks + (double)this.yOffset;
        double d5 = this.target.lastTickPosZ + (this.target.posZ - this.target.lastTickPosZ) * (double)partialTicks;
        double d6 = d0 + (d3 - d0) * (double)f;
        double d7 = d1 + (d4 - d1) * (double)f;
        double d8 = d2 + (d5 - d2) * (double)f;
        int i = this.getBrightnessForRender(partialTicks);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableLighting();
        this.renderManager.renderEntity(this.item, d6 -= interpPosX, d7 -= interpPosY, d8 -= interpPosZ, this.item.rotationYaw, partialTicks, false);
        if (Config.isShaders()) {
            Shaders.setEntityId(null);
            Shaders.useProgram((Program)oldShadersProgram);
        }
    }

    public void onUpdate() {
        ++this.age;
        if (this.age == this.maxAge) {
            this.setExpired();
        }
    }

    public int getFXLayer() {
        return 3;
    }
}
