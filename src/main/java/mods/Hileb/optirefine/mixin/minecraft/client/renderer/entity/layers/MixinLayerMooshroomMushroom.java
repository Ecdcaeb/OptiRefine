package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerMooshroomMushroom.class)
public class MixinLayerMooshroomMushroom {
    @Shadow @Final
    private RenderMooshroom mooshroomRenderer;

    @Unique
    private ModelRenderer modelRendererMushroom;
    @Unique
    private static final ResourceLocation LOCATION_MUSHROOM_RED = new ResourceLocation("textures/entity/cow/mushroom_red.png");
    @Unique
    private static boolean hasTextureMushroom = false;


    @Unique @Public
    private static void update() {
        hasTextureMushroom = Config.hasResource(LOCATION_MUSHROOM_RED);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(RenderMooshroom p_i46114_1, CallbackInfo ci){
        this.modelRendererMushroom = new ModelRenderer(this.mooshroomRenderer.f);
        this.modelRendererMushroom.setTextureSize(16, 16);
        this.modelRendererMushroom.rotationPointX = -6.0F;
        this.modelRendererMushroom.rotationPointZ = -8.0F;
        this.modelRendererMushroom.rotateAngleY = (float) Math.PI / 4.0F;
        int[][] faceUvs = new int[][]{null, null, {16, 16, 0, 0}, {16, 16, 0, 0}, null, null};
        ModelRenderer_addBox(this.modelRendererMushroom, faceUvs, 0.0F, 0.0F, 10.0F, 20.0F, 16.0F, 0.0F, 0.0F);
        int[][] faceUvs2 = new int[][]{null, null, null, null, {16, 16, 0, 0}, {16, 16, 0, 0}};
        ModelRenderer_addBox(this.modelRendererMushroom, faceUvs2, 10.0F, 0.0F, 0.0F, 0.0F, 16.0F, 20.0F, 0.0F);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.model.ModelRenderer addBox ([[IFFFFFFF)V")
    private static native void ModelRenderer_addBox(ModelRenderer modelRendererMushroom, int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta);

    @Redirect(method = "doRenderLayer(Lnet/minecraft/entity/passive/EntityMooshroom;FFFFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/TextureMap;LOCATION_BLOCKS_TEXTURE:Lnet/minecraft/util/ResourceLocation;"))
    public ResourceLocation texture(){
        if (hasTextureMushroom) {
            return (LOCATION_MUSHROOM_RED);
        } else {
            return (TextureMap.LOCATION_BLOCKS_TEXTURE);
        }
    }

    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/passive/EntityMooshroom;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderBlockBrightness(Lnet/minecraft/block/state/IBlockState;F)V"))
    public void renderBlockBrightness(BlockRendererDispatcher instance, IBlockState blockState, float state, Operation<Void> original){
        if (hasTextureMushroom) {
            this.modelRendererMushroom.render(0.0625F);
        } else {
            original.call(instance, blockState, state);
        }
    }
}
