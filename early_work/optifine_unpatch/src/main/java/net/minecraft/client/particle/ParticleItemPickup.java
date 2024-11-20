/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
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

public class ParticleItemPickup
extends Particle {
    private final Entity item;
    private final Entity target;
    private int age;
    private final int maxAge;
    private final float yOffset;
    private final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

    public ParticleItemPickup(World world, Entity entity, Entity entity2, float f) {
        super(world, entity.posX, entity.posY, entity.posZ, entity.motionX, entity.motionY, entity.motionZ);
        this.item = entity;
        this.target = entity2;
        this.maxAge = 3;
        this.yOffset = f;
    }

    public void renderParticle(BufferBuilder bufferBuilder, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        \u2603 = ((float)this.age + f) / (float)this.maxAge;
        \u2603 *= \u2603;
        double d = this.item.posX;
        \u2603 = this.item.posY;
        \u2603 = this.item.posZ;
        \u2603 = this.target.lastTickPosX + (this.target.posX - this.target.lastTickPosX) * (double)f;
        \u2603 = this.target.lastTickPosY + (this.target.posY - this.target.lastTickPosY) * (double)f + (double)this.yOffset;
        \u2603 = this.target.lastTickPosZ + (this.target.posZ - this.target.lastTickPosZ) * (double)f;
        \u2603 = d + (\u2603 - d) * (double)\u2603;
        \u2603 = \u2603 + (\u2603 - \u2603) * (double)\u2603;
        \u2603 = \u2603 + (\u2603 - \u2603) * (double)\u2603;
        int \u26032 = this.getBrightnessForRender(f);
        int \u26033 = \u26032 % 65536;
        int \u26034 = \u26032 / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)\u26033, (float)\u26034);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableLighting();
        this.renderManager.renderEntity(this.item, \u2603 -= interpPosX, \u2603 -= interpPosY, \u2603 -= interpPosZ, this.item.rotationYaw, f, false);
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
