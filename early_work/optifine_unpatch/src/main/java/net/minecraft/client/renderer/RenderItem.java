/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.List
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockDirt$DirtType
 *  net.minecraft.block.BlockDoublePlant$EnumPlantType
 *  net.minecraft.block.BlockFlower$EnumFlowerType
 *  net.minecraft.block.BlockHugeMushroom$EnumType
 *  net.minecraft.block.BlockPlanks$EnumType
 *  net.minecraft.block.BlockPrismarine$EnumType
 *  net.minecraft.block.BlockQuartz$EnumType
 *  net.minecraft.block.BlockRedSandstone$EnumType
 *  net.minecraft.block.BlockSand$EnumType
 *  net.minecraft.block.BlockSandStone$EnumType
 *  net.minecraft.block.BlockSilverfish$EnumType
 *  net.minecraft.block.BlockStone$EnumType
 *  net.minecraft.block.BlockStoneBrick$EnumType
 *  net.minecraft.block.BlockStoneSlab$EnumType
 *  net.minecraft.block.BlockStoneSlabNew$EnumType
 *  net.minecraft.block.BlockTallGrass$EnumType
 *  net.minecraft.block.BlockWall$EnumType
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$CullFace
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.ItemMeshDefinition
 *  net.minecraft.client.renderer.ItemModelMesher
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.block.model.ItemTransformVec3f
 *  net.minecraft.client.renderer.block.model.ModelManager
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.client.renderer.color.ItemColors
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.IResourceManagerReloadListener
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.EnumDyeColor
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemFishFood$FishType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.tileentity.TileEntityStructure$Mode
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.World
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.client.ItemModelMesherForge
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.client.model.pipeline.LightUtil
 *  net.minecraftforge.common.ForgeModContainer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.ItemModelMesherForge;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class RenderItem
implements IResourceManagerReloadListener {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private boolean notRenderingEffectsInGUI = true;
    public float zLevel;
    private final ItemModelMesher itemModelMesher;
    private final TextureManager textureManager;
    private final ItemColors itemColors;

    public RenderItem(TextureManager p_i46552_1_, ModelManager p_i46552_2_, ItemColors p_i46552_3_) {
        this.textureManager = p_i46552_1_;
        this.itemModelMesher = new ItemModelMesherForge(p_i46552_2_);
        this.registerItems();
        this.itemColors = p_i46552_3_;
    }

    public ItemModelMesher getItemModelMesher() {
        return this.itemModelMesher;
    }

    protected void registerItem(Item itm, int subType, String identifier) {
        this.itemModelMesher.register(itm, subType, new ModelResourceLocation(identifier, "inventory"));
    }

    protected void registerBlock(Block blk, int subType, String identifier) {
        this.registerItem(Item.getItemFromBlock((Block)blk), subType, identifier);
    }

    private void registerBlock(Block blk, String identifier) {
        this.registerBlock(blk, 0, identifier);
    }

    private void registerItem(Item itm, String identifier) {
        this.registerItem(itm, 0, identifier);
    }

    private void renderModel(IBakedModel model, ItemStack stack) {
        this.renderModel(model, -1, stack);
    }

    private void renderModel(IBakedModel model, int color) {
        this.renderModel(model, color, ItemStack.EMPTY);
    }

    private void renderModel(IBakedModel model, int color, ItemStack stack) {
        if (ForgeModContainer.allowEmissiveItems) {
            ForgeHooksClient.renderLitItem((RenderItem)this, (IBakedModel)model, (int)color, (ItemStack)stack);
            return;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
        for (EnumFacing enumfacing : EnumFacing.values()) {
            this.renderQuads(bufferbuilder, (List<BakedQuad>)model.getQuads((IBlockState)null, enumfacing, 0L), color, stack);
        }
        this.renderQuads(bufferbuilder, (List<BakedQuad>)model.getQuads((IBlockState)null, (EnumFacing)null, 0L), color, stack);
        tessellator.draw();
    }

    public void renderItem(ItemStack stack, IBakedModel model) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)-0.5f);
            if (model.isBuiltInRenderer()) {
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GlStateManager.enableRescaleNormal();
                stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
            } else {
                this.renderModel(model, stack);
                if (stack.hasEffect()) {
                    this.renderEffect(model);
                }
            }
            GlStateManager.popMatrix();
        }
    }

    private void renderEffect(IBakedModel model) {
        GlStateManager.depthMask((boolean)false);
        GlStateManager.depthFunc((int)514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_COLOR, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        this.textureManager.bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode((int)5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)8.0f, (float)8.0f, (float)8.0f);
        float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
        GlStateManager.translate((float)f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)-50.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        this.renderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)8.0f, (float)8.0f, (float)8.0f);
        float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0f / 8.0f;
        GlStateManager.translate((float)(-f1), (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        this.renderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc((int)515);
        GlStateManager.depthMask((boolean)true);
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    private void putQuadNormal(BufferBuilder renderer, BakedQuad quad) {
        Vec3i vec3i = quad.getFace().getDirectionVec();
        renderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
    }

    private void renderQuad(BufferBuilder renderer, BakedQuad quad, int color) {
        renderer.addVertexData(quad.getVertexData());
        renderer.putColor4(color);
        this.putQuadNormal(renderer, quad);
    }

    public void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
        boolean flag = color == -1 && !stack.isEmpty();
        int j = quads.size();
        for (int i = 0; i < j; ++i) {
            BakedQuad bakedquad = (BakedQuad)quads.get(i);
            int k = color;
            if (flag && bakedquad.hasTintIndex()) {
                k = this.itemColors.colorMultiplier(stack, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor((int)k);
                }
                k |= 0xFF000000;
            }
            LightUtil.renderQuadColor((BufferBuilder)renderer, (BakedQuad)bakedquad, (int)k);
        }
    }

    public boolean shouldRenderItemIn3D(ItemStack stack) {
        IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
        return ibakedmodel == null ? false : ibakedmodel.isGui3d();
    }

    public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType cameraTransformType) {
        if (!stack.isEmpty()) {
            IBakedModel ibakedmodel = this.getItemModelWithOverrides(stack, null, null);
            this.renderItemModel(stack, ibakedmodel, cameraTransformType, false);
        }
    }

    public IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn) {
        IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
        return ibakedmodel.getOverrides().handleItemState(ibakedmodel, stack, worldIn, entitylivingbaseIn);
    }

    public void renderItem(ItemStack stack, EntityLivingBase entitylivingbaseIn, ItemCameraTransforms.TransformType transform, boolean leftHanded) {
        if (!stack.isEmpty() && entitylivingbaseIn != null) {
            IBakedModel ibakedmodel = this.getItemModelWithOverrides(stack, entitylivingbaseIn.l, entitylivingbaseIn);
            this.renderItemModel(stack, ibakedmodel, transform, leftHanded);
        }
    }

    protected void renderItemModel(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded) {
        if (!stack.isEmpty()) {
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc((int)516, (float)0.1f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            bakedmodel = ForgeHooksClient.handleCameraTransforms((IBakedModel)bakedmodel, (ItemCameraTransforms.TransformType)transform, (boolean)leftHanded);
            this.renderItem(stack, bakedmodel);
            GlStateManager.cullFace((GlStateManager.CullFace)GlStateManager.CullFace.BACK);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        }
    }

    private boolean isThereOneNegativeScale(ItemTransformVec3f itemTranformVec) {
        return itemTranformVec.scale.x < 0.0f ^ itemTranformVec.scale.y < 0.0f ^ itemTranformVec.scale.z < 0.0f;
    }

    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
        this.renderItemModelIntoGUI(stack, x, y, this.getItemModelWithOverrides(stack, null, null));
    }

    protected void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedmodel) {
        GlStateManager.pushMatrix();
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.setupGuiTransform(x, y, bakedmodel.isGui3d());
        bakedmodel = ForgeHooksClient.handleCameraTransforms((IBakedModel)bakedmodel, (ItemCameraTransforms.TransformType)ItemCameraTransforms.TransformType.GUI, (boolean)false);
        this.renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d) {
        GlStateManager.translate((float)xPosition, (float)yPosition, (float)(100.0f + this.zLevel));
        GlStateManager.translate((float)8.0f, (float)8.0f, (float)0.0f);
        GlStateManager.scale((float)1.0f, (float)-1.0f, (float)1.0f);
        GlStateManager.scale((float)16.0f, (float)16.0f, (float)16.0f);
        if (isGui3d) {
            GlStateManager.enableLighting();
        } else {
            GlStateManager.disableLighting();
        }
    }

    public void renderItemAndEffectIntoGUI(ItemStack stack, int xPosition, int yPosition) {
        this.renderItemAndEffectIntoGUI((EntityLivingBase)Minecraft.getMinecraft().player, stack, xPosition, yPosition);
    }

    public void renderItemAndEffectIntoGUI(@Nullable EntityLivingBase p_184391_1_, ItemStack p_184391_2_, int p_184391_3_, int p_184391_4_) {
        if (!p_184391_2_.isEmpty()) {
            this.zLevel += 50.0f;
            try {
                this.renderItemModelIntoGUI(p_184391_2_, p_184391_3_, p_184391_4_, this.getItemModelWithOverrides(p_184391_2_, null, p_184391_1_));
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addDetail("Item Type", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                crashreportcategory.addDetail("Registry Name", () -> String.valueOf((Object)p_184391_2_.getItem().getRegistryName()));
                crashreportcategory.addDetail("Item Aux", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                crashreportcategory.addDetail("Item NBT", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                crashreportcategory.addDetail("Item Foil", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                throw new ReportedException(crashreport);
            }
            this.zLevel -= 50.0f;
        }
    }

    public void renderItemOverlays(FontRenderer fr, ItemStack stack, int xPosition, int yPosition) {
        this.renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, null);
    }

    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text) {
        if (!stack.isEmpty()) {
            EntityPlayerSP entityplayersp;
            float f3;
            if (stack.getCount() != 1 || text != null) {
                String s = text == null ? String.valueOf((int)stack.getCount()) : text;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                fr.drawStringWithShadow(s, (float)(xPosition + 19 - 2 - fr.getStringWidth(s)), (float)(yPosition + 6 + 3), 0xFFFFFF);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableBlend();
            }
            if (stack.getItem().showDurabilityBar(stack)) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                double health = stack.getItem().getDurabilityForDisplay(stack);
                int rgbfordisplay = stack.getItem().getRGBDurabilityForDisplay(stack);
                int i = Math.round((float)(13.0f - (float)health * 13.0f));
                int j = rgbfordisplay;
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, j >> 16 & 0xFF, j >> 8 & 0xFF, j & 0xFF, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
            float f = f3 = (entityplayersp = Minecraft.getMinecraft().player) == null ? 0.0f : entityplayersp.dt().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());
            if (f3 > 0.0f) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
                this.draw(bufferbuilder1, xPosition, yPosition + MathHelper.floor((float)(16.0f * (1.0f - f3))), 16, MathHelper.ceil((float)(16.0f * f3)), 255, 255, 255, 127);
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }

    private void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((double)(x + 0), (double)(y + 0), 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + 0), (double)(y + height), 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + height), 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + 0), 0.0).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    private void registerItems() {
        this.registerBlock(Blocks.ANVIL, "anvil_intact");
        this.registerBlock(Blocks.ANVIL, 1, "anvil_slightly_damaged");
        this.registerBlock(Blocks.ANVIL, 2, "anvil_very_damaged");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.BLACK.getMetadata(), "black_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.BLUE.getMetadata(), "blue_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.BROWN.getMetadata(), "brown_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.CYAN.getMetadata(), "cyan_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.GRAY.getMetadata(), "gray_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.GREEN.getMetadata(), "green_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.LIME.getMetadata(), "lime_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.MAGENTA.getMetadata(), "magenta_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.ORANGE.getMetadata(), "orange_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.PINK.getMetadata(), "pink_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.PURPLE.getMetadata(), "purple_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.RED.getMetadata(), "red_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.SILVER.getMetadata(), "silver_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.WHITE.getMetadata(), "white_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.YELLOW.getMetadata(), "yellow_carpet");
        this.registerBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
        this.registerBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.NORMAL.getMetadata(), "cobblestone_wall");
        this.registerBlock(Blocks.DIRT, BlockDirt.DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
        this.registerBlock(Blocks.DIRT, BlockDirt.DirtType.DIRT.getMetadata(), "dirt");
        this.registerBlock(Blocks.DIRT, BlockDirt.DirtType.PODZOL.getMetadata(), "podzol");
        this.registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.FERN.getMeta(), "double_fern");
        this.registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.GRASS.getMeta(), "double_grass");
        this.registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta(), "paeonia");
        this.registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.ROSE.getMeta(), "double_rose");
        this.registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
        this.registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta(), "syringa");
        this.registerBlock((Block)Blocks.LEAVES, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
        this.registerBlock((Block)Blocks.LEAVES, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
        this.registerBlock((Block)Blocks.LEAVES, BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
        this.registerBlock((Block)Blocks.LEAVES, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
        this.registerBlock((Block)Blocks.LEAVES2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
        this.registerBlock((Block)Blocks.LEAVES2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
        this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
        this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
        this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
        this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
        this.registerBlock(Blocks.LOG2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
        this.registerBlock(Blocks.LOG2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
        this.registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
        this.registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
        this.registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
        this.registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
        this.registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
        this.registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
        this.registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ALLIUM.getMeta(), "allium");
        this.registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
        this.registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
        this.registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
        this.registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
        this.registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
        this.registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.POPPY.getMeta(), "poppy");
        this.registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
        this.registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
        this.registerBlock((Block)Blocks.SAND, BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
        this.registerBlock((Block)Blocks.SAND, BlockSand.EnumType.SAND.getMetadata(), "sand");
        this.registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
        this.registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
        this.registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
        this.registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
        this.registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
        this.registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
        this.registerBlock(Blocks.SPONGE, 0, "sponge");
        this.registerBlock(Blocks.SPONGE, 1, "sponge_wet");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.RED.getMetadata(), "red_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.RED.getMetadata(), "red_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass_pane");
        this.registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BLACK.getMetadata(), "black_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BLUE.getMetadata(), "blue_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BROWN.getMetadata(), "brown_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.GRAY.getMetadata(), "gray_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.GREEN.getMetadata(), "green_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.LIME.getMetadata(), "lime_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PINK.getMetadata(), "pink_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.RED.getMetadata(), "red_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.SILVER.getMetadata(), "silver_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.WHITE.getMetadata(), "white_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_hardened_clay");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.GRANITE.getMetadata(), "granite");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.STONE.getMetadata(), "stone");
        this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
        this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
        this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
        this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
        this.registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
        this.registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
        this.registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
        this.registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
        this.registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
        this.registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
        this.registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
        this.registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
        this.registerBlock((Block)Blocks.STONE_SLAB2, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
        this.registerBlock((Block)Blocks.TALLGRASS, BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
        this.registerBlock((Block)Blocks.TALLGRASS, BlockTallGrass.EnumType.FERN.getMeta(), "fern");
        this.registerBlock((Block)Blocks.TALLGRASS, BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
        this.registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
        this.registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
        this.registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
        this.registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
        this.registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
        this.registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.BLACK.getMetadata(), "black_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.BLUE.getMetadata(), "blue_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.BROWN.getMetadata(), "brown_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.CYAN.getMetadata(), "cyan_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.GRAY.getMetadata(), "gray_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.GREEN.getMetadata(), "green_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.LIME.getMetadata(), "lime_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.MAGENTA.getMetadata(), "magenta_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.ORANGE.getMetadata(), "orange_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.PINK.getMetadata(), "pink_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.PURPLE.getMetadata(), "purple_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.RED.getMetadata(), "red_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.SILVER.getMetadata(), "silver_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.WHITE.getMetadata(), "white_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.YELLOW.getMetadata(), "yellow_wool");
        this.registerBlock(Blocks.FARMLAND, "farmland");
        this.registerBlock(Blocks.ACACIA_STAIRS, "acacia_stairs");
        this.registerBlock(Blocks.ACTIVATOR_RAIL, "activator_rail");
        this.registerBlock((Block)Blocks.BEACON, "beacon");
        this.registerBlock(Blocks.BEDROCK, "bedrock");
        this.registerBlock(Blocks.BIRCH_STAIRS, "birch_stairs");
        this.registerBlock(Blocks.BOOKSHELF, "bookshelf");
        this.registerBlock(Blocks.BRICK_BLOCK, "brick_block");
        this.registerBlock(Blocks.BRICK_BLOCK, "brick_block");
        this.registerBlock(Blocks.BRICK_STAIRS, "brick_stairs");
        this.registerBlock((Block)Blocks.BROWN_MUSHROOM, "brown_mushroom");
        this.registerBlock((Block)Blocks.CACTUS, "cactus");
        this.registerBlock(Blocks.CLAY, "clay");
        this.registerBlock(Blocks.COAL_BLOCK, "coal_block");
        this.registerBlock(Blocks.COAL_ORE, "coal_ore");
        this.registerBlock(Blocks.COBBLESTONE, "cobblestone");
        this.registerBlock(Blocks.CRAFTING_TABLE, "crafting_table");
        this.registerBlock(Blocks.DARK_OAK_STAIRS, "dark_oak_stairs");
        this.registerBlock((Block)Blocks.DAYLIGHT_DETECTOR, "daylight_detector");
        this.registerBlock((Block)Blocks.DEADBUSH, "dead_bush");
        this.registerBlock(Blocks.DETECTOR_RAIL, "detector_rail");
        this.registerBlock(Blocks.DIAMOND_BLOCK, "diamond_block");
        this.registerBlock(Blocks.DIAMOND_ORE, "diamond_ore");
        this.registerBlock(Blocks.DISPENSER, "dispenser");
        this.registerBlock(Blocks.DROPPER, "dropper");
        this.registerBlock(Blocks.EMERALD_BLOCK, "emerald_block");
        this.registerBlock(Blocks.EMERALD_ORE, "emerald_ore");
        this.registerBlock(Blocks.ENCHANTING_TABLE, "enchanting_table");
        this.registerBlock(Blocks.END_PORTAL_FRAME, "end_portal_frame");
        this.registerBlock(Blocks.END_STONE, "end_stone");
        this.registerBlock(Blocks.OAK_FENCE, "oak_fence");
        this.registerBlock(Blocks.SPRUCE_FENCE, "spruce_fence");
        this.registerBlock(Blocks.BIRCH_FENCE, "birch_fence");
        this.registerBlock(Blocks.JUNGLE_FENCE, "jungle_fence");
        this.registerBlock(Blocks.DARK_OAK_FENCE, "dark_oak_fence");
        this.registerBlock(Blocks.ACACIA_FENCE, "acacia_fence");
        this.registerBlock(Blocks.OAK_FENCE_GATE, "oak_fence_gate");
        this.registerBlock(Blocks.SPRUCE_FENCE_GATE, "spruce_fence_gate");
        this.registerBlock(Blocks.BIRCH_FENCE_GATE, "birch_fence_gate");
        this.registerBlock(Blocks.JUNGLE_FENCE_GATE, "jungle_fence_gate");
        this.registerBlock(Blocks.DARK_OAK_FENCE_GATE, "dark_oak_fence_gate");
        this.registerBlock(Blocks.ACACIA_FENCE_GATE, "acacia_fence_gate");
        this.registerBlock(Blocks.FURNACE, "furnace");
        this.registerBlock(Blocks.GLASS, "glass");
        this.registerBlock(Blocks.GLASS_PANE, "glass_pane");
        this.registerBlock(Blocks.GLOWSTONE, "glowstone");
        this.registerBlock(Blocks.GOLDEN_RAIL, "golden_rail");
        this.registerBlock(Blocks.GOLD_BLOCK, "gold_block");
        this.registerBlock(Blocks.GOLD_ORE, "gold_ore");
        this.registerBlock((Block)Blocks.GRASS, "grass");
        this.registerBlock(Blocks.GRASS_PATH, "grass_path");
        this.registerBlock(Blocks.GRAVEL, "gravel");
        this.registerBlock(Blocks.HARDENED_CLAY, "hardened_clay");
        this.registerBlock(Blocks.HAY_BLOCK, "hay_block");
        this.registerBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, "heavy_weighted_pressure_plate");
        this.registerBlock((Block)Blocks.HOPPER, "hopper");
        this.registerBlock(Blocks.ICE, "ice");
        this.registerBlock(Blocks.IRON_BARS, "iron_bars");
        this.registerBlock(Blocks.IRON_BLOCK, "iron_block");
        this.registerBlock(Blocks.IRON_ORE, "iron_ore");
        this.registerBlock(Blocks.IRON_TRAPDOOR, "iron_trapdoor");
        this.registerBlock(Blocks.JUKEBOX, "jukebox");
        this.registerBlock(Blocks.JUNGLE_STAIRS, "jungle_stairs");
        this.registerBlock(Blocks.LADDER, "ladder");
        this.registerBlock(Blocks.LAPIS_BLOCK, "lapis_block");
        this.registerBlock(Blocks.LAPIS_ORE, "lapis_ore");
        this.registerBlock(Blocks.LEVER, "lever");
        this.registerBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, "light_weighted_pressure_plate");
        this.registerBlock(Blocks.LIT_PUMPKIN, "lit_pumpkin");
        this.registerBlock(Blocks.MELON_BLOCK, "melon_block");
        this.registerBlock(Blocks.MOSSY_COBBLESTONE, "mossy_cobblestone");
        this.registerBlock((Block)Blocks.MYCELIUM, "mycelium");
        this.registerBlock(Blocks.NETHERRACK, "netherrack");
        this.registerBlock(Blocks.NETHER_BRICK, "nether_brick");
        this.registerBlock(Blocks.NETHER_BRICK_FENCE, "nether_brick_fence");
        this.registerBlock(Blocks.NETHER_BRICK_STAIRS, "nether_brick_stairs");
        this.registerBlock(Blocks.NOTEBLOCK, "noteblock");
        this.registerBlock(Blocks.OAK_STAIRS, "oak_stairs");
        this.registerBlock(Blocks.OBSIDIAN, "obsidian");
        this.registerBlock(Blocks.PACKED_ICE, "packed_ice");
        this.registerBlock((Block)Blocks.PISTON, "piston");
        this.registerBlock(Blocks.PUMPKIN, "pumpkin");
        this.registerBlock(Blocks.QUARTZ_ORE, "quartz_ore");
        this.registerBlock(Blocks.QUARTZ_STAIRS, "quartz_stairs");
        this.registerBlock(Blocks.RAIL, "rail");
        this.registerBlock(Blocks.REDSTONE_BLOCK, "redstone_block");
        this.registerBlock(Blocks.REDSTONE_LAMP, "redstone_lamp");
        this.registerBlock(Blocks.REDSTONE_ORE, "redstone_ore");
        this.registerBlock(Blocks.REDSTONE_TORCH, "redstone_torch");
        this.registerBlock((Block)Blocks.RED_MUSHROOM, "red_mushroom");
        this.registerBlock(Blocks.SANDSTONE_STAIRS, "sandstone_stairs");
        this.registerBlock(Blocks.RED_SANDSTONE_STAIRS, "red_sandstone_stairs");
        this.registerBlock(Blocks.SEA_LANTERN, "sea_lantern");
        this.registerBlock(Blocks.SLIME_BLOCK, "slime");
        this.registerBlock(Blocks.SNOW, "snow");
        this.registerBlock(Blocks.SNOW_LAYER, "snow_layer");
        this.registerBlock(Blocks.SOUL_SAND, "soul_sand");
        this.registerBlock(Blocks.SPRUCE_STAIRS, "spruce_stairs");
        this.registerBlock((Block)Blocks.STICKY_PISTON, "sticky_piston");
        this.registerBlock(Blocks.STONE_BRICK_STAIRS, "stone_brick_stairs");
        this.registerBlock(Blocks.STONE_BUTTON, "stone_button");
        this.registerBlock(Blocks.STONE_PRESSURE_PLATE, "stone_pressure_plate");
        this.registerBlock(Blocks.STONE_STAIRS, "stone_stairs");
        this.registerBlock(Blocks.TNT, "tnt");
        this.registerBlock(Blocks.TORCH, "torch");
        this.registerBlock(Blocks.TRAPDOOR, "trapdoor");
        this.registerBlock((Block)Blocks.TRIPWIRE_HOOK, "tripwire_hook");
        this.registerBlock(Blocks.VINE, "vine");
        this.registerBlock(Blocks.WATERLILY, "waterlily");
        this.registerBlock(Blocks.WEB, "web");
        this.registerBlock(Blocks.WOODEN_BUTTON, "wooden_button");
        this.registerBlock(Blocks.WOODEN_PRESSURE_PLATE, "wooden_pressure_plate");
        this.registerBlock((Block)Blocks.YELLOW_FLOWER, BlockFlower.EnumFlowerType.DANDELION.getMeta(), "dandelion");
        this.registerBlock(Blocks.END_ROD, "end_rod");
        this.registerBlock(Blocks.CHORUS_PLANT, "chorus_plant");
        this.registerBlock(Blocks.CHORUS_FLOWER, "chorus_flower");
        this.registerBlock(Blocks.PURPUR_BLOCK, "purpur_block");
        this.registerBlock(Blocks.PURPUR_PILLAR, "purpur_pillar");
        this.registerBlock(Blocks.PURPUR_STAIRS, "purpur_stairs");
        this.registerBlock((Block)Blocks.PURPUR_SLAB, "purpur_slab");
        this.registerBlock((Block)Blocks.PURPUR_DOUBLE_SLAB, "purpur_double_slab");
        this.registerBlock(Blocks.END_BRICKS, "end_bricks");
        this.registerBlock(Blocks.MAGMA, "magma");
        this.registerBlock(Blocks.NETHER_WART_BLOCK, "nether_wart_block");
        this.registerBlock(Blocks.RED_NETHER_BRICK, "red_nether_brick");
        this.registerBlock(Blocks.BONE_BLOCK, "bone_block");
        this.registerBlock(Blocks.STRUCTURE_VOID, "structure_void");
        this.registerBlock(Blocks.OBSERVER, "observer");
        this.registerBlock(Blocks.WHITE_SHULKER_BOX, "white_shulker_box");
        this.registerBlock(Blocks.ORANGE_SHULKER_BOX, "orange_shulker_box");
        this.registerBlock(Blocks.MAGENTA_SHULKER_BOX, "magenta_shulker_box");
        this.registerBlock(Blocks.LIGHT_BLUE_SHULKER_BOX, "light_blue_shulker_box");
        this.registerBlock(Blocks.YELLOW_SHULKER_BOX, "yellow_shulker_box");
        this.registerBlock(Blocks.LIME_SHULKER_BOX, "lime_shulker_box");
        this.registerBlock(Blocks.PINK_SHULKER_BOX, "pink_shulker_box");
        this.registerBlock(Blocks.GRAY_SHULKER_BOX, "gray_shulker_box");
        this.registerBlock(Blocks.SILVER_SHULKER_BOX, "silver_shulker_box");
        this.registerBlock(Blocks.CYAN_SHULKER_BOX, "cyan_shulker_box");
        this.registerBlock(Blocks.PURPLE_SHULKER_BOX, "purple_shulker_box");
        this.registerBlock(Blocks.BLUE_SHULKER_BOX, "blue_shulker_box");
        this.registerBlock(Blocks.BROWN_SHULKER_BOX, "brown_shulker_box");
        this.registerBlock(Blocks.GREEN_SHULKER_BOX, "green_shulker_box");
        this.registerBlock(Blocks.RED_SHULKER_BOX, "red_shulker_box");
        this.registerBlock(Blocks.BLACK_SHULKER_BOX, "black_shulker_box");
        this.registerBlock(Blocks.WHITE_GLAZED_TERRACOTTA, "white_glazed_terracotta");
        this.registerBlock(Blocks.ORANGE_GLAZED_TERRACOTTA, "orange_glazed_terracotta");
        this.registerBlock(Blocks.MAGENTA_GLAZED_TERRACOTTA, "magenta_glazed_terracotta");
        this.registerBlock(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, "light_blue_glazed_terracotta");
        this.registerBlock(Blocks.YELLOW_GLAZED_TERRACOTTA, "yellow_glazed_terracotta");
        this.registerBlock(Blocks.LIME_GLAZED_TERRACOTTA, "lime_glazed_terracotta");
        this.registerBlock(Blocks.PINK_GLAZED_TERRACOTTA, "pink_glazed_terracotta");
        this.registerBlock(Blocks.GRAY_GLAZED_TERRACOTTA, "gray_glazed_terracotta");
        this.registerBlock(Blocks.SILVER_GLAZED_TERRACOTTA, "silver_glazed_terracotta");
        this.registerBlock(Blocks.CYAN_GLAZED_TERRACOTTA, "cyan_glazed_terracotta");
        this.registerBlock(Blocks.PURPLE_GLAZED_TERRACOTTA, "purple_glazed_terracotta");
        this.registerBlock(Blocks.BLUE_GLAZED_TERRACOTTA, "blue_glazed_terracotta");
        this.registerBlock(Blocks.BROWN_GLAZED_TERRACOTTA, "brown_glazed_terracotta");
        this.registerBlock(Blocks.GREEN_GLAZED_TERRACOTTA, "green_glazed_terracotta");
        this.registerBlock(Blocks.RED_GLAZED_TERRACOTTA, "red_glazed_terracotta");
        this.registerBlock(Blocks.BLACK_GLAZED_TERRACOTTA, "black_glazed_terracotta");
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            this.registerBlock(Blocks.CONCRETE, enumdyecolor.getMetadata(), enumdyecolor.getDyeColorName() + "_concrete");
            this.registerBlock(Blocks.CONCRETE_POWDER, enumdyecolor.getMetadata(), enumdyecolor.getDyeColorName() + "_concrete_powder");
        }
        this.registerBlock((Block)Blocks.CHEST, "chest");
        this.registerBlock(Blocks.TRAPPED_CHEST, "trapped_chest");
        this.registerBlock(Blocks.ENDER_CHEST, "ender_chest");
        this.registerItem(Items.IRON_SHOVEL, "iron_shovel");
        this.registerItem(Items.IRON_PICKAXE, "iron_pickaxe");
        this.registerItem(Items.IRON_AXE, "iron_axe");
        this.registerItem(Items.FLINT_AND_STEEL, "flint_and_steel");
        this.registerItem(Items.APPLE, "apple");
        this.registerItem((Item)Items.BOW, "bow");
        this.registerItem(Items.ARROW, "arrow");
        this.registerItem(Items.SPECTRAL_ARROW, "spectral_arrow");
        this.registerItem(Items.TIPPED_ARROW, "tipped_arrow");
        this.registerItem(Items.COAL, 0, "coal");
        this.registerItem(Items.COAL, 1, "charcoal");
        this.registerItem(Items.DIAMOND, "diamond");
        this.registerItem(Items.IRON_INGOT, "iron_ingot");
        this.registerItem(Items.GOLD_INGOT, "gold_ingot");
        this.registerItem(Items.IRON_SWORD, "iron_sword");
        this.registerItem(Items.WOODEN_SWORD, "wooden_sword");
        this.registerItem(Items.WOODEN_SHOVEL, "wooden_shovel");
        this.registerItem(Items.WOODEN_PICKAXE, "wooden_pickaxe");
        this.registerItem(Items.WOODEN_AXE, "wooden_axe");
        this.registerItem(Items.STONE_SWORD, "stone_sword");
        this.registerItem(Items.STONE_SHOVEL, "stone_shovel");
        this.registerItem(Items.STONE_PICKAXE, "stone_pickaxe");
        this.registerItem(Items.STONE_AXE, "stone_axe");
        this.registerItem(Items.DIAMOND_SWORD, "diamond_sword");
        this.registerItem(Items.DIAMOND_SHOVEL, "diamond_shovel");
        this.registerItem(Items.DIAMOND_PICKAXE, "diamond_pickaxe");
        this.registerItem(Items.DIAMOND_AXE, "diamond_axe");
        this.registerItem(Items.STICK, "stick");
        this.registerItem(Items.BOWL, "bowl");
        this.registerItem(Items.MUSHROOM_STEW, "mushroom_stew");
        this.registerItem(Items.GOLDEN_SWORD, "golden_sword");
        this.registerItem(Items.GOLDEN_SHOVEL, "golden_shovel");
        this.registerItem(Items.GOLDEN_PICKAXE, "golden_pickaxe");
        this.registerItem(Items.GOLDEN_AXE, "golden_axe");
        this.registerItem(Items.STRING, "string");
        this.registerItem(Items.FEATHER, "feather");
        this.registerItem(Items.GUNPOWDER, "gunpowder");
        this.registerItem(Items.WOODEN_HOE, "wooden_hoe");
        this.registerItem(Items.STONE_HOE, "stone_hoe");
        this.registerItem(Items.IRON_HOE, "iron_hoe");
        this.registerItem(Items.DIAMOND_HOE, "diamond_hoe");
        this.registerItem(Items.GOLDEN_HOE, "golden_hoe");
        this.registerItem(Items.WHEAT_SEEDS, "wheat_seeds");
        this.registerItem(Items.WHEAT, "wheat");
        this.registerItem(Items.BREAD, "bread");
        this.registerItem((Item)Items.LEATHER_HELMET, "leather_helmet");
        this.registerItem((Item)Items.LEATHER_CHESTPLATE, "leather_chestplate");
        this.registerItem((Item)Items.LEATHER_LEGGINGS, "leather_leggings");
        this.registerItem((Item)Items.LEATHER_BOOTS, "leather_boots");
        this.registerItem((Item)Items.CHAINMAIL_HELMET, "chainmail_helmet");
        this.registerItem((Item)Items.CHAINMAIL_CHESTPLATE, "chainmail_chestplate");
        this.registerItem((Item)Items.CHAINMAIL_LEGGINGS, "chainmail_leggings");
        this.registerItem((Item)Items.CHAINMAIL_BOOTS, "chainmail_boots");
        this.registerItem((Item)Items.IRON_HELMET, "iron_helmet");
        this.registerItem((Item)Items.IRON_CHESTPLATE, "iron_chestplate");
        this.registerItem((Item)Items.IRON_LEGGINGS, "iron_leggings");
        this.registerItem((Item)Items.IRON_BOOTS, "iron_boots");
        this.registerItem((Item)Items.DIAMOND_HELMET, "diamond_helmet");
        this.registerItem((Item)Items.DIAMOND_CHESTPLATE, "diamond_chestplate");
        this.registerItem((Item)Items.DIAMOND_LEGGINGS, "diamond_leggings");
        this.registerItem((Item)Items.DIAMOND_BOOTS, "diamond_boots");
        this.registerItem((Item)Items.GOLDEN_HELMET, "golden_helmet");
        this.registerItem((Item)Items.GOLDEN_CHESTPLATE, "golden_chestplate");
        this.registerItem((Item)Items.GOLDEN_LEGGINGS, "golden_leggings");
        this.registerItem((Item)Items.GOLDEN_BOOTS, "golden_boots");
        this.registerItem(Items.FLINT, "flint");
        this.registerItem(Items.PORKCHOP, "porkchop");
        this.registerItem(Items.COOKED_PORKCHOP, "cooked_porkchop");
        this.registerItem(Items.PAINTING, "painting");
        this.registerItem(Items.GOLDEN_APPLE, "golden_apple");
        this.registerItem(Items.GOLDEN_APPLE, 1, "golden_apple");
        this.registerItem(Items.SIGN, "sign");
        this.registerItem(Items.OAK_DOOR, "oak_door");
        this.registerItem(Items.SPRUCE_DOOR, "spruce_door");
        this.registerItem(Items.BIRCH_DOOR, "birch_door");
        this.registerItem(Items.JUNGLE_DOOR, "jungle_door");
        this.registerItem(Items.ACACIA_DOOR, "acacia_door");
        this.registerItem(Items.DARK_OAK_DOOR, "dark_oak_door");
        this.registerItem(Items.BUCKET, "bucket");
        this.registerItem(Items.WATER_BUCKET, "water_bucket");
        this.registerItem(Items.LAVA_BUCKET, "lava_bucket");
        this.registerItem(Items.MINECART, "minecart");
        this.registerItem(Items.SADDLE, "saddle");
        this.registerItem(Items.IRON_DOOR, "iron_door");
        this.registerItem(Items.REDSTONE, "redstone");
        this.registerItem(Items.SNOWBALL, "snowball");
        this.registerItem(Items.BOAT, "oak_boat");
        this.registerItem(Items.SPRUCE_BOAT, "spruce_boat");
        this.registerItem(Items.BIRCH_BOAT, "birch_boat");
        this.registerItem(Items.JUNGLE_BOAT, "jungle_boat");
        this.registerItem(Items.ACACIA_BOAT, "acacia_boat");
        this.registerItem(Items.DARK_OAK_BOAT, "dark_oak_boat");
        this.registerItem(Items.LEATHER, "leather");
        this.registerItem(Items.MILK_BUCKET, "milk_bucket");
        this.registerItem(Items.BRICK, "brick");
        this.registerItem(Items.CLAY_BALL, "clay_ball");
        this.registerItem(Items.REEDS, "reeds");
        this.registerItem(Items.PAPER, "paper");
        this.registerItem(Items.BOOK, "book");
        this.registerItem(Items.SLIME_BALL, "slime_ball");
        this.registerItem(Items.CHEST_MINECART, "chest_minecart");
        this.registerItem(Items.FURNACE_MINECART, "furnace_minecart");
        this.registerItem(Items.EGG, "egg");
        this.registerItem(Items.COMPASS, "compass");
        this.registerItem((Item)Items.FISHING_ROD, "fishing_rod");
        this.registerItem(Items.CLOCK, "clock");
        this.registerItem(Items.GLOWSTONE_DUST, "glowstone_dust");
        this.registerItem(Items.FISH, ItemFishFood.FishType.COD.getMetadata(), "cod");
        this.registerItem(Items.FISH, ItemFishFood.FishType.SALMON.getMetadata(), "salmon");
        this.registerItem(Items.FISH, ItemFishFood.FishType.CLOWNFISH.getMetadata(), "clownfish");
        this.registerItem(Items.FISH, ItemFishFood.FishType.PUFFERFISH.getMetadata(), "pufferfish");
        this.registerItem(Items.COOKED_FISH, ItemFishFood.FishType.COD.getMetadata(), "cooked_cod");
        this.registerItem(Items.COOKED_FISH, ItemFishFood.FishType.SALMON.getMetadata(), "cooked_salmon");
        this.registerItem(Items.DYE, EnumDyeColor.BLACK.getDyeDamage(), "dye_black");
        this.registerItem(Items.DYE, EnumDyeColor.RED.getDyeDamage(), "dye_red");
        this.registerItem(Items.DYE, EnumDyeColor.GREEN.getDyeDamage(), "dye_green");
        this.registerItem(Items.DYE, EnumDyeColor.BROWN.getDyeDamage(), "dye_brown");
        this.registerItem(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), "dye_blue");
        this.registerItem(Items.DYE, EnumDyeColor.PURPLE.getDyeDamage(), "dye_purple");
        this.registerItem(Items.DYE, EnumDyeColor.CYAN.getDyeDamage(), "dye_cyan");
        this.registerItem(Items.DYE, EnumDyeColor.SILVER.getDyeDamage(), "dye_silver");
        this.registerItem(Items.DYE, EnumDyeColor.GRAY.getDyeDamage(), "dye_gray");
        this.registerItem(Items.DYE, EnumDyeColor.PINK.getDyeDamage(), "dye_pink");
        this.registerItem(Items.DYE, EnumDyeColor.LIME.getDyeDamage(), "dye_lime");
        this.registerItem(Items.DYE, EnumDyeColor.YELLOW.getDyeDamage(), "dye_yellow");
        this.registerItem(Items.DYE, EnumDyeColor.LIGHT_BLUE.getDyeDamage(), "dye_light_blue");
        this.registerItem(Items.DYE, EnumDyeColor.MAGENTA.getDyeDamage(), "dye_magenta");
        this.registerItem(Items.DYE, EnumDyeColor.ORANGE.getDyeDamage(), "dye_orange");
        this.registerItem(Items.DYE, EnumDyeColor.WHITE.getDyeDamage(), "dye_white");
        this.registerItem(Items.BONE, "bone");
        this.registerItem(Items.SUGAR, "sugar");
        this.registerItem(Items.CAKE, "cake");
        this.registerItem(Items.REPEATER, "repeater");
        this.registerItem(Items.COOKIE, "cookie");
        this.registerItem((Item)Items.SHEARS, "shears");
        this.registerItem(Items.MELON, "melon");
        this.registerItem(Items.PUMPKIN_SEEDS, "pumpkin_seeds");
        this.registerItem(Items.MELON_SEEDS, "melon_seeds");
        this.registerItem(Items.BEEF, "beef");
        this.registerItem(Items.COOKED_BEEF, "cooked_beef");
        this.registerItem(Items.CHICKEN, "chicken");
        this.registerItem(Items.COOKED_CHICKEN, "cooked_chicken");
        this.registerItem(Items.RABBIT, "rabbit");
        this.registerItem(Items.COOKED_RABBIT, "cooked_rabbit");
        this.registerItem(Items.MUTTON, "mutton");
        this.registerItem(Items.COOKED_MUTTON, "cooked_mutton");
        this.registerItem(Items.RABBIT_FOOT, "rabbit_foot");
        this.registerItem(Items.RABBIT_HIDE, "rabbit_hide");
        this.registerItem(Items.RABBIT_STEW, "rabbit_stew");
        this.registerItem(Items.ROTTEN_FLESH, "rotten_flesh");
        this.registerItem(Items.ENDER_PEARL, "ender_pearl");
        this.registerItem(Items.BLAZE_ROD, "blaze_rod");
        this.registerItem(Items.GHAST_TEAR, "ghast_tear");
        this.registerItem(Items.GOLD_NUGGET, "gold_nugget");
        this.registerItem(Items.NETHER_WART, "nether_wart");
        this.registerItem(Items.BEETROOT, "beetroot");
        this.registerItem(Items.BEETROOT_SEEDS, "beetroot_seeds");
        this.registerItem(Items.BEETROOT_SOUP, "beetroot_soup");
        this.registerItem(Items.TOTEM_OF_UNDYING, "totem");
        this.registerItem((Item)Items.POTIONITEM, "bottle_drinkable");
        this.registerItem((Item)Items.SPLASH_POTION, "bottle_splash");
        this.registerItem((Item)Items.LINGERING_POTION, "bottle_lingering");
        this.registerItem(Items.GLASS_BOTTLE, "glass_bottle");
        this.registerItem(Items.DRAGON_BREATH, "dragon_breath");
        this.registerItem(Items.SPIDER_EYE, "spider_eye");
        this.registerItem(Items.FERMENTED_SPIDER_EYE, "fermented_spider_eye");
        this.registerItem(Items.BLAZE_POWDER, "blaze_powder");
        this.registerItem(Items.MAGMA_CREAM, "magma_cream");
        this.registerItem(Items.BREWING_STAND, "brewing_stand");
        this.registerItem(Items.CAULDRON, "cauldron");
        this.registerItem(Items.ENDER_EYE, "ender_eye");
        this.registerItem(Items.SPECKLED_MELON, "speckled_melon");
        this.itemModelMesher.register(Items.SPAWN_EGG, (ItemMeshDefinition)new /* Unavailable Anonymous Inner Class!! */);
        this.registerItem(Items.EXPERIENCE_BOTTLE, "experience_bottle");
        this.registerItem(Items.FIRE_CHARGE, "fire_charge");
        this.registerItem(Items.WRITABLE_BOOK, "writable_book");
        this.registerItem(Items.EMERALD, "emerald");
        this.registerItem(Items.ITEM_FRAME, "item_frame");
        this.registerItem(Items.FLOWER_POT, "flower_pot");
        this.registerItem(Items.CARROT, "carrot");
        this.registerItem(Items.POTATO, "potato");
        this.registerItem(Items.BAKED_POTATO, "baked_potato");
        this.registerItem(Items.POISONOUS_POTATO, "poisonous_potato");
        this.registerItem((Item)Items.MAP, "map");
        this.registerItem(Items.GOLDEN_CARROT, "golden_carrot");
        this.registerItem(Items.SKULL, 0, "skull_skeleton");
        this.registerItem(Items.SKULL, 1, "skull_wither");
        this.registerItem(Items.SKULL, 2, "skull_zombie");
        this.registerItem(Items.SKULL, 3, "skull_char");
        this.registerItem(Items.SKULL, 4, "skull_creeper");
        this.registerItem(Items.SKULL, 5, "skull_dragon");
        this.registerItem(Items.CARROT_ON_A_STICK, "carrot_on_a_stick");
        this.registerItem(Items.NETHER_STAR, "nether_star");
        this.registerItem(Items.END_CRYSTAL, "end_crystal");
        this.registerItem(Items.PUMPKIN_PIE, "pumpkin_pie");
        this.registerItem(Items.FIREWORK_CHARGE, "firework_charge");
        this.registerItem(Items.COMPARATOR, "comparator");
        this.registerItem(Items.NETHERBRICK, "netherbrick");
        this.registerItem(Items.QUARTZ, "quartz");
        this.registerItem(Items.TNT_MINECART, "tnt_minecart");
        this.registerItem(Items.HOPPER_MINECART, "hopper_minecart");
        this.registerItem((Item)Items.ARMOR_STAND, "armor_stand");
        this.registerItem(Items.IRON_HORSE_ARMOR, "iron_horse_armor");
        this.registerItem(Items.GOLDEN_HORSE_ARMOR, "golden_horse_armor");
        this.registerItem(Items.DIAMOND_HORSE_ARMOR, "diamond_horse_armor");
        this.registerItem(Items.LEAD, "lead");
        this.registerItem(Items.NAME_TAG, "name_tag");
        this.itemModelMesher.register(Items.BANNER, (ItemMeshDefinition)new /* Unavailable Anonymous Inner Class!! */);
        this.itemModelMesher.register(Items.BED, (ItemMeshDefinition)new /* Unavailable Anonymous Inner Class!! */);
        this.itemModelMesher.register(Items.SHIELD, (ItemMeshDefinition)new /* Unavailable Anonymous Inner Class!! */);
        this.registerItem(Items.ELYTRA, "elytra");
        this.registerItem(Items.CHORUS_FRUIT, "chorus_fruit");
        this.registerItem(Items.CHORUS_FRUIT_POPPED, "chorus_fruit_popped");
        this.registerItem(Items.SHULKER_SHELL, "shulker_shell");
        this.registerItem(Items.IRON_NUGGET, "iron_nugget");
        this.registerItem(Items.RECORD_13, "record_13");
        this.registerItem(Items.RECORD_CAT, "record_cat");
        this.registerItem(Items.RECORD_BLOCKS, "record_blocks");
        this.registerItem(Items.RECORD_CHIRP, "record_chirp");
        this.registerItem(Items.RECORD_FAR, "record_far");
        this.registerItem(Items.RECORD_MALL, "record_mall");
        this.registerItem(Items.RECORD_MELLOHI, "record_mellohi");
        this.registerItem(Items.RECORD_STAL, "record_stal");
        this.registerItem(Items.RECORD_STRAD, "record_strad");
        this.registerItem(Items.RECORD_WARD, "record_ward");
        this.registerItem(Items.RECORD_11, "record_11");
        this.registerItem(Items.RECORD_WAIT, "record_wait");
        this.registerItem(Items.PRISMARINE_SHARD, "prismarine_shard");
        this.registerItem(Items.PRISMARINE_CRYSTALS, "prismarine_crystals");
        this.registerItem(Items.KNOWLEDGE_BOOK, "knowledge_book");
        this.itemModelMesher.register(Items.ENCHANTED_BOOK, (ItemMeshDefinition)new /* Unavailable Anonymous Inner Class!! */);
        this.itemModelMesher.register((Item)Items.FILLED_MAP, (ItemMeshDefinition)new /* Unavailable Anonymous Inner Class!! */);
        this.registerBlock(Blocks.COMMAND_BLOCK, "command_block");
        this.registerItem(Items.FIREWORKS, "fireworks");
        this.registerItem(Items.COMMAND_BLOCK_MINECART, "command_block_minecart");
        this.registerBlock(Blocks.BARRIER, "barrier");
        this.registerBlock(Blocks.MOB_SPAWNER, "mob_spawner");
        this.registerItem(Items.WRITTEN_BOOK, "written_book");
        this.registerBlock(Blocks.BROWN_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
        this.registerBlock(Blocks.RED_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
        this.registerBlock(Blocks.DRAGON_EGG, "dragon_egg");
        this.registerBlock(Blocks.REPEATING_COMMAND_BLOCK, "repeating_command_block");
        this.registerBlock(Blocks.CHAIN_COMMAND_BLOCK, "chain_command_block");
        this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.SAVE.getModeId(), "structure_block");
        this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.LOAD.getModeId(), "structure_block");
        this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.CORNER.getModeId(), "structure_block");
        this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.DATA.getModeId(), "structure_block");
        ModelLoader.onRegisterItems((ItemModelMesher)this.itemModelMesher);
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.itemModelMesher.rebuildCache();
    }
}
