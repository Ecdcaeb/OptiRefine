package mods.Hileb.optirefine.mixin.defaults.minecraft.client.model;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.cem.CemHandAttachments;
import mods.Hileb.optirefine.optifine.cem.CemPartVisibility;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
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
public abstract class MixinModelRenderer implements CemPartVisibility.Part, CemHandAttachments.Part {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member textureWidth exists in target class
    @Shadow
    public float textureWidth;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member textureHeight exists in target class
    @Shadow
    public float textureHeight;
// [AUDIT-OK] baseline member textureOffsetX exists in target class
    @Shadow
    private int textureOffsetX;
// [AUDIT-OK] baseline member textureOffsetY exists in target class
    @Shadow
    private int textureOffsetY;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member rotationPointX exists in target class
    @Shadow
    public float rotationPointX;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member rotationPointY exists in target class
    @Shadow
    public float rotationPointY;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member rotationPointZ exists in target class
    @Shadow
    public float rotationPointZ;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member rotateAngleX exists in target class
    @Shadow
    public float rotateAngleX;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member rotateAngleY exists in target class
    @Shadow
    public float rotateAngleY;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member rotateAngleZ exists in target class
    @Shadow
    public float rotateAngleZ;
// [AUDIT-OK] baseline member compileDisplayList exists in target class (protected, vanilla)
    @Shadow
    protected abstract void compileDisplayList(float scale);
// [AUDIT-OK] baseline member compiled exists in target class
    @Shadow
    private boolean compiled;
// [AUDIT-OK] baseline member displayList exists in target class
    @Shadow
    private int displayList;
// [AUDIT-OK] baseline member mirror exists in target class
    @Shadow
    public boolean mirror;
// [AUDIT-OK] baseline member showModel exists in target class
    @Shadow
    public boolean showModel;
// [AUDIT-OK] baseline member isHidden exists in target class
    @Shadow
    public boolean isHidden;

    @Unique private boolean optiRefine$hiddenByAnimation;
    @Unique private boolean optiRefine$hideBoxes;
    @Unique private CemHandAttachments optiRefine$handAttachments;
    @Unique private CemHandAttachments.Part optiRefine$attachmentParent;
    @Unique private boolean optiRefine$hasHandAttachments;

    @Inject(method = "addChild", at = @At("RETURN"))
    private void optiRefine$rememberAttachmentParent(ModelRenderer child, CallbackInfo ci) {
        if (child instanceof CemHandAttachments.Part part) {
            part.optiRefine$setAttachmentParent(this);
        }
    }

    @Override
    public void optiRefine$setAttachmentParent(CemHandAttachments.Part parent) {
        this.optiRefine$attachmentParent = parent;
        if (this.optiRefine$hasHandAttachments) {
            parent.optiRefine$markHandAttachments();
        }
    }

    @Override
    public void optiRefine$setHandAttachments(CemHandAttachments attachments) {
        this.optiRefine$handAttachments = attachments;
        this.optiRefine$markHandAttachments();
    }

    @Override
    public void optiRefine$markHandAttachments() {
        if (!this.optiRefine$hasHandAttachments) {
            this.optiRefine$hasHandAttachments = true;
            if (this.optiRefine$attachmentParent != null) {
                this.optiRefine$attachmentParent.optiRefine$markHandAttachments();
            }
        }
    }

    @Override
    public boolean optiRefine$isAttachmentRoot() {
        return this.optiRefine$hasHandAttachments && this.optiRefine$attachmentParent == null;
    }

    @Override
    public boolean optiRefine$applyHandAttachment(EnumHandSide side, float scale) {
        if (!this.optiRefine$hasHandAttachments || !this.optiRefine$isPartVisible()) {
            return false;
        }
        Vec3d anchor = this.optiRefine$handAttachments == null ? null : this.optiRefine$handAttachments.forHand(side);
        if (anchor != null) {
            this.optiRefine$applyAttachmentTransforms(scale);
            GlStateManager.translate(anchor.x * scale, anchor.y * scale, anchor.z * scale);
            return true;
        }
        if (this.childModels != null) {
            for (ModelRenderer child : this.childModels) {
                if (((CemHandAttachments.Part) child).optiRefine$applyHandAttachment(side, scale)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void optiRefine$applyAttachmentTransforms(float scale) {
        if (this.optiRefine$attachmentParent != null) {
            this.optiRefine$attachmentParent.optiRefine$applyAttachmentTransforms(scale);
        }
        if (this.offsetX != 0.0F || this.offsetY != 0.0F || this.offsetZ != 0.0F) {
            GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
        }
        if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
            GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
        }
        this.optiRefine$rotatePart();
        if (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F) {
            GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
        }
    }

    @Unique
    private void optiRefine$rotatePart() {
        if (this.rotateAngleZ != 0.0F) {
            GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
        }
        if (this.rotateAngleY != 0.0F) {
            GlStateManager.rotate(this.rotateAngleY * (180.0F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        }
        if (this.rotateAngleX != 0.0F) {
            GlStateManager.rotate(this.rotateAngleX * (180.0F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
        }
    }

    @Override
    public boolean optiRefine$areBoxesVisible() {
        return !this.optiRefine$hideBoxes;
    }

    @Override
    public void optiRefine$setBoxesVisible(boolean visible) {
        this.optiRefine$hideBoxes = !visible;
    }

    @Override
    public boolean optiRefine$isPartVisible() {
        return !this.isHidden && this.showModel && !this.optiRefine$hiddenByAnimation;
    }

    @Override
    public void optiRefine$setPartVisible(boolean visible) {
        this.optiRefine$hiddenByAnimation = !visible;
    }

    @Override
    public void optiRefine$updateHiddenBranch() {
        if (this.isHidden || !this.showModel) {
            return;
        }
        if (this.modelUpdater != null) {
            this.modelUpdater.update();
        }
        this.optiRefine$updateHiddenChildren();
    }

    @Unique
    private void optiRefine$updateHiddenChildren() {
        if (this.childModels != null) {
            for (ModelRenderer child : this.childModels) {
                ((CemPartVisibility.Part) child).optiRefine$updateHiddenBranch();
            }
        }
    }

    @Unique
    private boolean optiRefine$prepareGeometry(float scale) {
        if (this.optiRefine$hiddenByAnimation) {
            this.optiRefine$updateHiddenChildren();
            return false;
        }
        if (!this.optiRefine$hideBoxes) {
            this.checkResetDisplayList();
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
        }
        return true;
    }

// [AUDIT-OK] baseline member cubeList exists in target class
    @Shadow
    public List<ModelBox> cubeList;
// [AUDIT-OK] baseline member childModels exists in target class
    @Shadow
    public List<ModelRenderer> childModels;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member boxName exists in target class
    @Shadow @Final
    public String boxName;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member baseModel exists in target class
    @Shadow @Final
    private ModelBase baseModel;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member offsetX exists in target class
    @Shadow
    public float offsetX;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member offsetY exists in target class
    @Shadow
    public float offsetY;
    @SuppressWarnings("unused")
// [AUDIT-OK] baseline member offsetZ exists in target class
    @Shadow
    public float offsetZ;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member (public in OF), not in baseline; @Public for OF-jar access
    @Unique
    @Public
    private List<ModelSprite> spriteList;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added member (public in OF), not in baseline; @Public for OF-jar access
    @Unique
    @Public
    private boolean mirrorV;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added member (public in OF), not in baseline; @Public for OF-jar access
    @Unique
    @Public
    private float scaleX;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added member (public in OF), not in baseline; @Public for OF-jar access
    @Unique
    @Public
    private float scaleY;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added member (public in OF), not in baseline; @Public for OF-jar access
    @Unique
    @Public
    private float scaleZ;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline; @Public informational (OF keeps private)
    @Public
    private int countResetDisplayList;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline; @Public informational (OF keeps private)
    @Unique
    @Public
    private ResourceLocation textureLocation;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline; @Public informational (OF keeps private)
    @Unique
    @Public
    private String id;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline; @Public informational (OF keeps private)
    @Public
    private ModelUpdater modelUpdater;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline; @Public informational (OF keeps private)
    @Unique
    @Public
    private RenderGlobal renderGlobal;

    @SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
// [AUDIT-OK] OF-added member (not in tsrg): MCP name renderOverlayDamaged correct
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net/minecraft/client/renderer/RenderGlobal renderOverlayDamaged Z")
    private native static boolean optiRefine$isDamageOverlay(RenderGlobal global);

    @SuppressWarnings("unused")
    @Unique
// [AUDIT-OK] OF-added member (not in tsrg): MCP name renderOverlayEyes correct
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net/minecraft/client/renderer/RenderGlobal renderOverlayEyes Z")
    private static native boolean optiRefine$isEyesOverlay(RenderGlobal global);

// [AUDIT-OK] OF-added member (not in tsrg): MCP name getBoundTexture correct
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net/minecraft/client/renderer/GlStateManager getBoundTexture ()I")
    private native static int optiRefine$getBoundTexture();

// [AUDIT-OK] target render(F)V matches baseline; instance handler; replicates OF render
    @WrapMethod(method = "render")
    public void optiRefine$renderCustomModel(float scale, Operation<Void> original) {
        if (this.isHidden || !this.showModel) {
            return;
        }
        boolean customTexture = this.textureLocation != null && !optiRefine$isDamageOverlay(this.renderGlobal);
        if (customTexture && optiRefine$isEyesOverlay(this.renderGlobal)) {
            return;
        }
        int previousTexture = customTexture ? optiRefine$getBoundTexture() : 0;
        try {
            if (customTexture) {
                Config.getTextureManager().bindTexture(this.textureLocation);
            }
            if (this.modelUpdater != null) {
                this.modelUpdater.update();
            }
            if (!this.optiRefine$prepareGeometry(scale)) {
                return;
            }

            float offsetX = this.offsetX, offsetY = this.offsetY, offsetZ = this.offsetZ;
            float pivotX = this.rotationPointX, pivotY = this.rotationPointY, pivotZ = this.rotationPointZ;
            boolean hasOffset = offsetX != 0.0F || offsetY != 0.0F || offsetZ != 0.0F;
            boolean hasPivot = pivotX != 0.0F || pivotY != 0.0F || pivotZ != 0.0F;
            if (hasPivot) {
                pivotX *= scale;
                pivotY *= scale;
                pivotZ *= scale;
            }
            boolean rotated = this.rotateAngleX != 0.0F || this.rotateAngleY != 0.0F || this.rotateAngleZ != 0.0F;
            boolean scaled = this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F;
            boolean offsetTranslated = false, matrixPushed = false, pivotTranslated = false;
            try {
                if (hasOffset) {
                    GlStateManager.translate(offsetX, offsetY, offsetZ);
                    offsetTranslated = true;
                }
                if (rotated || scaled) {
                    GlStateManager.pushMatrix();
                    matrixPushed = true;
                }
                if (hasPivot) {
                    GlStateManager.translate(pivotX, pivotY, pivotZ);
                    pivotTranslated = true;
                }
                if (rotated) {
                    this.optiRefine$rotatePart();
                }
                if (scaled) {
                    GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
                }
                if (!this.optiRefine$hideBoxes) {
                    GlStateManager.callList(this.displayList);
                }
                if (this.childModels != null) {
                    for (ModelRenderer childModel : this.childModels) {
                        childModel.render(scale);
                    }
                }
            } finally {
                if (matrixPushed) {
                    GlStateManager.popMatrix();
                } else if (pivotTranslated) {
                    GlStateManager.translate(-pivotX, -pivotY, -pivotZ);
                }
                if (offsetTranslated) {
                    GlStateManager.translate(-offsetX, -offsetY, -offsetZ);
                }
            }
        } finally {
            if (customTexture) {
                GlStateManager.bindTexture(previousTexture);
            }
        }
    }

// [AUDIT-OK] target renderWithRotation(F)V matches baseline; instance handler; replicates OF
    @WrapMethod(method = "renderWithRotation")
    public void optiRefine$renderCustomModelWithRotation(float scale, Operation<Void> original) {
        if (this.isHidden || !this.showModel) {
            return;
        }
        boolean customTexture = this.textureLocation != null && !optiRefine$isDamageOverlay(this.renderGlobal);
        if (customTexture && optiRefine$isEyesOverlay(this.renderGlobal)) {
            return;
        }
        int previousTexture = customTexture ? optiRefine$getBoundTexture() : 0;
        try {
            if (customTexture) {
                Config.getTextureManager().bindTexture(this.textureLocation);
            }
            if (this.modelUpdater != null) {
                this.modelUpdater.update();
            }
            if (!this.optiRefine$prepareGeometry(scale)) {
                return;
            }
            GlStateManager.pushMatrix();
            try {
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleY * (180.0F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleX * (180.0F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                }
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
                }
                if (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F) {
                    GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
                }
                if (!this.optiRefine$hideBoxes) {
                    GlStateManager.callList(this.displayList);
                }
                if (this.childModels != null) {
                    for (ModelRenderer childModel : this.childModels) {
                        childModel.render(scale);
                    }
                }
            } finally {
                GlStateManager.popMatrix();
            }
        } finally {
            if (customTexture) {
                GlStateManager.bindTexture(previousTexture);
            }
        }
    }

// [AUDIT-OK] target postRender(F)V matches baseline
    @Inject(method = "postRender", at = @At("HEAD"))
    public void optiRefine$resetDisplayListBeforePostRender(float scale, CallbackInfo ci){
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();
        }
    }

// [AUDIT-OK] target compileDisplayList(F)V matches baseline; generateDisplayLists(I)I invoke present; handler params match
    @WrapOperation(method = "compileDisplayList", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GLAllocation;generateDisplayLists(I)I"))
    private int optiRefine$reuseDisplayList(int range, Operation<Integer> original){
        return this.displayList == 0 ? original.call(range) : this.displayList;
    }

// [AUDIT-OK] glEndList()V invoke present in baseline compileDisplayList; sprites render before endList like OF
    @WrapOperation(method = "compileDisplayList", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;glEndList()V"))
    private void optiRefine$compileModelSprites(Operation<Void> original, @Local(argsOnly = true) float scale) {
        try {
            for (ModelSprite sprite : this.spriteList) {
                sprite.render(Tessellator.getInstance(), scale);
            }
        } finally {
            original.call();
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added API, not in baseline; @Public for OF-jar access
    @Public
    public void addSprite(float posX, float posY, float posZ, int sizeX, int sizeY, int sizeZ, float sizeAdd) {
        this.spriteList.add(new ModelSprite((ModelRenderer)(Object)this, this.textureOffsetX, this.textureOffsetY, posX, posY, posZ, sizeX, sizeY, sizeZ, sizeAdd));
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public boolean getCompiled() {
        return this.compiled;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public int getDisplayList() {
        return this.displayList;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline; @Public informational (OF keeps private)
    @Public
    private void checkResetDisplayList() {
        if (this.countResetDisplayList != Shaders.countResetDisplayLists) {
            this.compiled = false;
            this.countResetDisplayList = Shaders.countResetDisplayLists;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public void setTextureLocation(ResourceLocation textureLocation) {
        this.textureLocation = textureLocation;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public String getId() {
        return this.id;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public void setId(String id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    @Unique
// [AUDIT-OK] NEW resolves to mixin-added @NewConstructor ModelBox(int[][] faceUvs,...) matching OF ctor; no vanilla ctor collision
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net/minecraft/client/model/ModelBox (Lnet/minecraft/client/model/ModelRenderer;[[IFFFFFFFZ)V")
    private native static ModelBox optiRefine$createModelBox(AccessibleOperation.Construction construction, ModelRenderer renderer, int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added API (OF addBox(int[][],...)), not in baseline; @Public for OF-jar access
    @Public
    public void addBox(int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta) {
        this.cubeList.add(optiRefine$createModelBox(AccessibleOperation.Construction.construction(), optiRefine$castModelRenderer(), faceUvs, x, y, z, dx, dy, dz, delta, this.mirror));
    }

// [AUDIT-OK] cast helper (no member lookup)
    @AccessibleOperation
    public ModelRenderer optiRefine$castModelRenderer(){
        throw new AbstractMethodError();
    }

    @SuppressWarnings("unused")
    @Unique
// [AUDIT-OK] OF-added member (not in tsrg): MCP name getId correct; resolves to mixin-added @Public getId
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "getId ()Ljava/lang/String;")
    private static String optiRefine$getModelRendererId(ModelRenderer renderer){
        throw new AbstractMethodError();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public ModelRenderer getChild(String name) {
        if (name != null) {
            if (this.childModels != null) {
                for (ModelRenderer child : this.childModels) {
                    if (name.equals(optiRefine$getModelRendererId(child))) {
                        return child;
                    }
                }
            }

        }
        return null;
    }

    @SuppressWarnings("unused")
    @Unique
// [AUDIT-OK] OF-added member (not in tsrg): MCP name getChildDeep correct; resolves to mixin-added @Public getChildDeep
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "getChildDeep (Ljava/lang/String;)Lnet/minecraft/client/model/ModelRenderer;")
    private static native ModelRenderer optiRefine$getModelRendererChildDeep(ModelRenderer renderer, String name);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added API, not in baseline
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
                    for (ModelRenderer child : this.childModels) {
                        ModelRenderer mr = optiRefine$getModelRendererChildDeep(child, name);
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
// [AUDIT-OK] OF-added API, not in baseline
    @Public
    public void setModelUpdater(ModelUpdater modelUpdater) {
        this.modelUpdater = modelUpdater;
    }

    @Override
    public String toString() {
        return "id: " + this.id + ", boxes: " + (this.cubeList != null ? this.cubeList.size() : null) + ", submodels: " + (this.childModels != null ? this.childModels.size() : null);
    }


    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initializeModelRenderState(CallbackInfo ci) {
        this.spriteList = new ArrayList<>();
        this.mirrorV = false;
        this.scaleX = 1.0F;
        this.scaleY = 1.0F;
        this.scaleZ = 1.0F;
        this.textureLocation = null;
        this.id = null;
        this.renderGlobal = Config.getRenderGlobal();
    }
}
