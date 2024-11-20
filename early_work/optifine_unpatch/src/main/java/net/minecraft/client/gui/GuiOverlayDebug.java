/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.Lists
 *  java.lang.Boolean
 *  java.lang.Comparable
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.Runtime
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.List
 *  java.util.Map$Entry
 *  net.minecraft.block.Block
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.ClientBrandRetriever
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiOverlayDebug$1
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.FrameTimer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.EnumSkyBlock
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.WorldType
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.Sys
 *  org.lwjgl.opengl.Display
 */
package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

@SideOnly(value=Side.CLIENT)
public class GuiOverlayDebug
extends Gui {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;

    public GuiOverlayDebug(Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
    }

    public void renderDebugInfo(ScaledResolution scaledResolutionIn) {
        this.mc.profiler.startSection("debug");
        GlStateManager.pushMatrix();
        this.renderDebugInfoLeft();
        this.renderDebugInfoRight(scaledResolutionIn);
        GlStateManager.popMatrix();
        if (this.mc.gameSettings.showLagometer) {
            this.renderLagometer();
        }
        this.mc.profiler.endSection();
    }

    protected void renderDebugInfoLeft() {
        List<String> list = this.call();
        list.add((Object)"");
        list.add((Object)("Debug: Pie [shift]: " + (this.mc.gameSettings.showDebugProfilerChart ? "visible" : "hidden") + " FPS [alt]: " + (this.mc.gameSettings.showLagometer ? "visible" : "hidden")));
        list.add((Object)"For help: press F3 + Q");
        for (int i = 0; i < list.size(); ++i) {
            String s = (String)list.get(i);
            if (Strings.isNullOrEmpty((String)s)) continue;
            int j = this.fontRenderer.FONT_HEIGHT;
            int k = this.fontRenderer.getStringWidth(s);
            int l = 2;
            int i1 = 2 + j * i;
            GuiOverlayDebug.drawRect((int)1, (int)(i1 - 1), (int)(2 + k + 1), (int)(i1 + j - 1), (int)-1873784752);
            this.fontRenderer.drawString(s, 2, i1, 0xE0E0E0);
        }
    }

    protected void renderDebugInfoRight(ScaledResolution scaledRes) {
        List<String> list = this.getDebugInfoRight();
        for (int i = 0; i < list.size(); ++i) {
            String s = (String)list.get(i);
            if (Strings.isNullOrEmpty((String)s)) continue;
            int j = this.fontRenderer.FONT_HEIGHT;
            int k = this.fontRenderer.getStringWidth(s);
            int l = scaledRes.getScaledWidth() - 2 - k;
            int i1 = 2 + j * i;
            GuiOverlayDebug.drawRect((int)(l - 1), (int)(i1 - 1), (int)(l + k + 1), (int)(i1 + j - 1), (int)-1873784752);
            this.fontRenderer.drawString(s, l, i1, 0xE0E0E0);
        }
    }

    protected List<String> call() {
        BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        if (this.mc.isReducedDebug()) {
            return Lists.newArrayList((Object[])new String[]{"Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(), this.mc.world.getProviderName(), "", String.format((String)"Chunk-relative: %d %d %d", (Object[])new Object[]{blockpos.p() & 0xF, blockpos.q() & 0xF, blockpos.r() & 0xF})});
        }
        Entity entity = this.mc.getRenderViewEntity();
        EnumFacing enumfacing = entity.getHorizontalFacing();
        String s = "Invalid";
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[enumfacing.ordinal()]) {
            case 1: {
                s = "Towards negative Z";
                break;
            }
            case 2: {
                s = "Towards positive Z";
                break;
            }
            case 3: {
                s = "Towards negative X";
                break;
            }
            case 4: {
                s = "Towards positive X";
            }
        }
        ArrayList list = Lists.newArrayList((Object[])new String[]{"Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType()) + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(), this.mc.world.getProviderName(), "", String.format((String)"XYZ: %.3f / %.5f / %.3f", (Object[])new Object[]{this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ}), String.format((String)"Block: %d %d %d", (Object[])new Object[]{blockpos.p(), blockpos.q(), blockpos.r()}), String.format((String)"Chunk: %d %d %d in %d %d %d", (Object[])new Object[]{blockpos.p() & 0xF, blockpos.q() & 0xF, blockpos.r() & 0xF, blockpos.p() >> 4, blockpos.q() >> 4, blockpos.r() >> 4}), String.format((String)"Facing: %s (%s) (%.1f / %.1f)", (Object[])new Object[]{enumfacing, s, Float.valueOf((float)MathHelper.wrapDegrees((float)entity.rotationYaw)), Float.valueOf((float)MathHelper.wrapDegrees((float)entity.rotationPitch))})});
        if (this.mc.world != null) {
            Chunk chunk = this.mc.world.getChunk(blockpos);
            if (this.mc.world.isBlockLoaded(blockpos) && blockpos.q() >= 0 && blockpos.q() < 256) {
                if (!chunk.isEmpty()) {
                    EntityPlayerMP entityplayermp;
                    list.add((Object)("Biome: " + chunk.getBiome(blockpos, this.mc.world.getBiomeProvider()).getBiomeName()));
                    list.add((Object)("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)"));
                    DifficultyInstance difficultyinstance = this.mc.world.getDifficultyForLocation(blockpos);
                    if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null && (entityplayermp = this.mc.getIntegratedServer().getPlayerList().getPlayerByUUID(this.mc.player.bm())) != null) {
                        difficultyinstance = entityplayermp.l.getDifficultyForLocation(new BlockPos((Entity)entityplayermp));
                    }
                    list.add((Object)String.format((String)"Local Difficulty: %.2f // %.2f (Day %d)", (Object[])new Object[]{Float.valueOf((float)difficultyinstance.getAdditionalDifficulty()), Float.valueOf((float)difficultyinstance.getClampedAdditionalDifficulty()), this.mc.world.getWorldTime() / 24000L}));
                } else {
                    list.add((Object)"Waiting for chunk...");
                }
            } else {
                list.add((Object)"Outside of world...");
            }
        }
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            list.add((Object)("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName()));
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockpos1 = this.mc.objectMouseOver.getBlockPos();
            list.add((Object)String.format((String)"Looking at: %d %d %d", (Object[])new Object[]{blockpos1.p(), blockpos1.q(), blockpos1.r()}));
        }
        return list;
    }

    protected <T extends Comparable<T>> List<String> getDebugInfoRight() {
        long i = Runtime.getRuntime().maxMemory();
        long j = Runtime.getRuntime().totalMemory();
        long k = Runtime.getRuntime().freeMemory();
        long l = j - k;
        ArrayList list = Lists.newArrayList((Object[])new String[]{String.format((String)"Java: %s %dbit", (Object[])new Object[]{System.getProperty((String)"java.version"), this.mc.isJava64bit() ? 64 : 32}), "LWJGL: " + Sys.getVersion(), String.format((String)"Mem: % 2d%% %03d/%03dMB", (Object[])new Object[]{l * 100L / i, GuiOverlayDebug.bytesToMb(l), GuiOverlayDebug.bytesToMb(i)}), String.format((String)"Allocated: % 2d%% %03dMB", (Object[])new Object[]{j * 100L / i, GuiOverlayDebug.bytesToMb(j)}), "", String.format((String)"CPU: %s", (Object[])new Object[]{OpenGlHelper.getCpu()}), "", String.format((String)"Display: %dx%d (%s)", (Object[])new Object[]{Display.getWidth(), Display.getHeight(), GlStateManager.glGetString((int)7936)}), GlStateManager.glGetString((int)7937), GlStateManager.glGetString((int)7938)});
        list.add((Object)"");
        list.addAll((Collection)FMLCommonHandler.instance().getBrandings(false));
        if (this.mc.isReducedDebug()) {
            return list;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
            IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
            if (this.mc.world.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
                iblockstate = iblockstate.c((IBlockAccess)this.mc.world, blockpos);
            }
            list.add((Object)"");
            list.add((Object)String.valueOf((Object)Block.REGISTRY.getNameForObject((Object)iblockstate.getBlock())));
            for (Map.Entry entry : iblockstate.getProperties().entrySet()) {
                IProperty iproperty = (IProperty)entry.getKey();
                Comparable t = (Comparable)entry.getValue();
                String s = iproperty.getName(t);
                if (Boolean.TRUE.equals((Object)t)) {
                    s = String.valueOf((Object)TextFormatting.GREEN) + s;
                } else if (Boolean.FALSE.equals((Object)t)) {
                    s = String.valueOf((Object)TextFormatting.RED) + s;
                }
                list.add((Object)(iproperty.getName() + ": " + s));
            }
        }
        return list;
    }

    public void renderLagometer() {
        GlStateManager.disableDepth();
        FrameTimer frametimer = this.mc.getFrameTimer();
        int i = frametimer.getLastIndex();
        int j = frametimer.getIndex();
        long[] along = frametimer.getFrames();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int k = i;
        int l = 0;
        GuiOverlayDebug.drawRect((int)0, (int)(scaledresolution.getScaledHeight() - 60), (int)240, (int)scaledresolution.getScaledHeight(), (int)-1873784752);
        while (k != j) {
            int i1 = frametimer.getLagometerValue(along[k], 30);
            int j1 = this.getFrameColor(MathHelper.clamp((int)i1, (int)0, (int)60), 0, 30, 60);
            this.drawVerticalLine(l, scaledresolution.getScaledHeight(), scaledresolution.getScaledHeight() - i1, j1);
            ++l;
            k = frametimer.parseIndex(k + 1);
        }
        GuiOverlayDebug.drawRect((int)1, (int)(scaledresolution.getScaledHeight() - 30 + 1), (int)14, (int)(scaledresolution.getScaledHeight() - 30 + 10), (int)-1873784752);
        this.fontRenderer.drawString("60", 2, scaledresolution.getScaledHeight() - 30 + 2, 0xE0E0E0);
        this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 30, -1);
        GuiOverlayDebug.drawRect((int)1, (int)(scaledresolution.getScaledHeight() - 60 + 1), (int)14, (int)(scaledresolution.getScaledHeight() - 60 + 10), (int)-1873784752);
        this.fontRenderer.drawString("30", 2, scaledresolution.getScaledHeight() - 60 + 2, 0xE0E0E0);
        this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 60, -1);
        this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 1, -1);
        this.drawVerticalLine(0, scaledresolution.getScaledHeight() - 60, scaledresolution.getScaledHeight(), -1);
        this.drawVerticalLine(239, scaledresolution.getScaledHeight() - 60, scaledresolution.getScaledHeight(), -1);
        if (this.mc.gameSettings.limitFramerate <= 120) {
            this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 60 + this.mc.gameSettings.limitFramerate / 2, -16711681);
        }
        GlStateManager.enableDepth();
    }

    private int getFrameColor(int p_181552_1_, int p_181552_2_, int p_181552_3_, int p_181552_4_) {
        return p_181552_1_ < p_181552_3_ ? this.blendColors(-16711936, -256, (float)p_181552_1_ / (float)p_181552_3_) : this.blendColors(-256, -65536, (float)(p_181552_1_ - p_181552_3_) / (float)(p_181552_4_ - p_181552_3_));
    }

    private int blendColors(int p_181553_1_, int p_181553_2_, float p_181553_3_) {
        int i = p_181553_1_ >> 24 & 0xFF;
        int j = p_181553_1_ >> 16 & 0xFF;
        int k = p_181553_1_ >> 8 & 0xFF;
        int l = p_181553_1_ & 0xFF;
        int i1 = p_181553_2_ >> 24 & 0xFF;
        int j1 = p_181553_2_ >> 16 & 0xFF;
        int k1 = p_181553_2_ >> 8 & 0xFF;
        int l1 = p_181553_2_ & 0xFF;
        int i2 = MathHelper.clamp((int)((int)((float)i + (float)(i1 - i) * p_181553_3_)), (int)0, (int)255);
        int j2 = MathHelper.clamp((int)((int)((float)j + (float)(j1 - j) * p_181553_3_)), (int)0, (int)255);
        int k2 = MathHelper.clamp((int)((int)((float)k + (float)(k1 - k) * p_181553_3_)), (int)0, (int)255);
        int l2 = MathHelper.clamp((int)((int)((float)l + (float)(l1 - l) * p_181553_3_)), (int)0, (int)255);
        return i2 << 24 | j2 << 16 | k2 << 8 | l2;
    }

    private static long bytesToMb(long bytes) {
        return bytes / 1024L / 1024L;
    }
}
