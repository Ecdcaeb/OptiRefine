package mods.Hileb.optirefine.mixin.minecraft.client.model;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.optifine.entity.model.anim.ModelUpdater;
import net.optifine.model.ModelSprite;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;


@Mixin(ModelRenderer.class)
public abstract class MixinModelRenderer {
    @SuppressWarnings("unused")
    @Shadow
    public float textureWidth;
    @SuppressWarnings("unused")
    @Shadow
    public float textureHeight;
    @Shadow
    private int textureOffsetX;
    @Shadow
    private int textureOffsetY;
    @SuppressWarnings("unused")
    @Shadow
    public float rotationPointX;
    @SuppressWarnings("unused")
    @Shadow
    public float rotationPointY;
    @SuppressWarnings("unused")
    @Shadow
    public float rotationPointZ;
    @SuppressWarnings("unused")
    @Shadow
    public float rotateAngleX;
    @SuppressWarnings("unused")
    @Shadow
    public float rotateAngleY;
    @SuppressWarnings("unused")
    @Shadow
    public float rotateAngleZ;
    @Shadow
    private boolean compiled;
    @Shadow
    private int displayList;
    @Shadow
    public boolean mirror;
    @Shadow
    public boolean showModel;
    @Shadow
    public boolean isHidden;

    @Shadow
    public List<ModelBox> cubeList;
    @Shadow
    public List<ModelRenderer> childModels;
    @SuppressWarnings("unused")
    @Shadow @Final
    public String boxName;
    @SuppressWarnings("unused")
    @Shadow @Final
    private ModelBase baseModel;
    @SuppressWarnings("unused")
    @Shadow
    public float offsetX;
    @SuppressWarnings("unused")
    @Shadow
    public float offsetY;
    @SuppressWarnings("unused")
    @Shadow
    public float offsetZ;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    public List<ModelSprite> spriteList = new ArrayList<>();

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public boolean mirrorV = false;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public float scaleX = 1.0F;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public float scaleY = 1.0F;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public float scaleZ = 1.0F;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private int countResetDisplayList;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private ResourceLocation textureLocation = null;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private String id = null;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private ModelUpdater modelUpdater;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private RenderGlobal renderGlobal = Config.getRenderGlobal();

    @SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
    @Unique
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net/minecraft/client/renderer/RenderGlobal renderOverlayDamaged Z")
    private native static boolean _acc_RenderGlobal_renderOverlayDamaged_(RenderGlobal global);

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net/minecraft/client/renderer/RenderGlobal renderOverlayEyes Z")
    private static native boolean _acc_RenderGlobal_renderOverlayEyes_(RenderGlobal global);

    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net/minecraft/client/renderer/GlStateManager getBoundTexture ()I")
    private native static int _acc_GlStateManager_getBoundTexture_();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void injectPreRender(float p_78785_1_, CallbackInfo ci, @Share("lastTextureId") LocalIntRef lastTextureId){
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();

            lastTextureId.set(0);
            if (this.textureLocation != null && !_acc_RenderGlobal_renderOverlayDamaged_(this.renderGlobal)) {
                if (_acc_RenderGlobal_renderOverlayEyes_(this.renderGlobal)) {
                    ci.cancel();
                }

                lastTextureId.set(_acc_GlStateManager_getBoundTexture_());
                Config.getTextureManager().bindTexture(this.textureLocation);
            }

            if (this.modelUpdater != null) {
                this.modelUpdater.update();
            }
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void injectPostRender(float p_78785_1_, CallbackInfo ci, @Share("lastTextureId") LocalIntRef lastTextureId){
        if (!this.isHidden && this.showModel) {
            if (lastTextureId.get() != 0) {
                GlStateManager.bindTexture(lastTextureId.get());
            }
        }
    }

    @Inject(method = "renderWithRotation", at = @At("HEAD"), cancellable = true)
    public void injectPreRenderWithRotation(float p_78785_1_, CallbackInfo ci, @Share("lastTextureId") LocalIntRef lastTextureId){
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();

            lastTextureId.set(0);
            if (this.textureLocation != null && !_acc_RenderGlobal_renderOverlayDamaged_(this.renderGlobal)) {
                if (_acc_RenderGlobal_renderOverlayEyes_(this.renderGlobal)) {
                    ci.cancel();
                }

                lastTextureId.set(_acc_GlStateManager_getBoundTexture_());
                Config.getTextureManager().bindTexture(this.textureLocation);
            }

            if (this.modelUpdater != null) {
                this.modelUpdater.update();
            }
        }
    }

    @Inject(method = "renderWithRotation", at = @At("RETURN"))
    public void injectPostRenderWithRotation(float p_78785_1_, CallbackInfo ci, @Share("lastTextureId") LocalIntRef lastTextureId){
        if (!this.isHidden && this.showModel) {
            if (lastTextureId.get() != 0) {
                GlStateManager.bindTexture(lastTextureId.get());
            }
        }
    }

    @Inject(method = "postRender", at = @At("HEAD"))
    public void injectPrePostRender(float p_78785_1_, CallbackInfo ci){
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();
        }
    }

    /**
     * @author
     * @reason
     */
    @SideOnly(Side.CLIENT)
    @Overwrite
    private void compileDisplayList(float scale) {
        if (this.displayList == 0) {
            this.displayList = GLAllocation.generateDisplayLists(1);
        }

        GlStateManager.glNewList(this.displayList, 4864);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();

        for (ModelBox box : cubeList) {
            box.render(bufferbuilder, scale);
        }

        for (ModelSprite sprite : spriteList) {
            sprite.render(Tessellator.getInstance(), scale);
        }

        GlStateManager.glEndList();
        this.compiled = true;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void addSprite(float posX, float posY, float posZ, int sizeX, int sizeY, int sizeZ, float sizeAdd) {
        this.spriteList.add(new ModelSprite((ModelRenderer)(Object)this, this.textureOffsetX, this.textureOffsetY, posX, posY, posZ, sizeX, sizeY, sizeZ, sizeAdd));
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public boolean getCompiled() {
        return this.compiled;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public int getDisplayList() {
        return this.displayList;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private void checkResetDisplayList() {
        if (this.countResetDisplayList != Shaders.countResetDisplayLists) {
            this.compiled = false;
            this.countResetDisplayList = Shaders.countResetDisplayLists;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void setTextureLocation(ResourceLocation textureLocation) {
        this.textureLocation = textureLocation;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public String getId() {
        return this.id;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void setId(String id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net/minecraft/client/model/ModelBox (Lnet/minecraft/client/model/ModelRenderer;[[IFFFFFFFZ)V")
    private native static ModelBox _new_ModelBox(AccessibleOperation.Construction construction, ModelRenderer renderer, int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void addBox(int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta) {
        this.cubeList.add(_new_ModelBox(AccessibleOperation.Construction.construction(), _cast_this(), faceUvs, x, y, z, dx, dy, dz, delta, this.mirror));
    }

    @Unique
    @AccessibleOperation
    public ModelRenderer _cast_this(){
        throw new AbstractMethodError();
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "getId ()I")
    private static int _acc_ModelRenderer_getId(ModelRenderer renderer){
        throw new AbstractMethodError();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    public ModelRenderer getChild(String name) {
        if (name == null) {
            return null;
        } else {
            if (this.childModels != null) {
                for (int i = 0; i < this.childModels.size(); i++) {
                    ModelRenderer child = this.childModels.get(i);
                    //TODO
                    if (name.equals(_acc_ModelRenderer_getId(child))) {
                        return child;
                    }
                }
            }

            return null;
        }
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "getChildDeep (Ljava/lang/String;)Lnet/minecraft/client/model/ModelRenderer;")
    private static native ModelRenderer _acc_ModelRenderer_getChildDeep(ModelRenderer renderer, String a);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public ModelRenderer getChildDeep(String name) {
        if (name == null) {
            return null;
        } else {
            ModelRenderer mrChild = this.getChild(name);
            if (mrChild != null) {
                return mrChild;
            } else {
                if (this.childModels != null) {
                    for (int i = 0; i < this.childModels.size(); i++) {
                        ModelRenderer child = this.childModels.get(i);
                        ModelRenderer mr = _acc_ModelRenderer_getChildDeep(child, name);
                        if (mr != null) {
                            return mr;
                        }
                    }
                }

                return null;
            }
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void setModelUpdater(ModelUpdater modelUpdater) {
        this.modelUpdater = modelUpdater;
    }

    @Override
    public String toString() {
        return "id: " + this.id + ", boxes: " + (this.cubeList != null ? this.cubeList.size() : null) + ", submodels: " + (this.childModels != null ? this.childModels.size() : null);
    }

}
