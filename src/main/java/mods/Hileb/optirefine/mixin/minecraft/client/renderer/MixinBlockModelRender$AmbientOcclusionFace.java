package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.util.math.BlockPos;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace")
public class MixinBlockModelRender$AmbientOcclusionFace {

    @AccessTransformer(name = "<class>", access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
    public void access(){

    }

    @Shadow @Final
    private float[] vertexColorMultiplier;

    @Shadow @Final
    private int[] vertexBrightness;

    @Unique
    private BlockPos.MutableBlockPos[] blockPosArr = new BlockPos.MutableBlockPos[5];

    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectConstructor(BlockModelRenderer p_i46235_1, CallbackInfo ci){
        for (int i = 0; i < this.blockPosArr.length; i++) {
            this.blockPosArr[i] = new BlockPos.MutableBlockPos();
        }
    }

    @NewConstructor
    public void AmbientOcclusionFace(BlockModelRenderer bmr) {
        for (int i = 0; i < this.blockPosArr.length; i++) {
            this.blockPosArr[i] = new BlockPos.MutableBlockPos();
        }
    }

    @Unique
    public void setMaxBlockLight() {
        int maxBlockLight = 240;
        this.vertexBrightness[0] = this.vertexBrightness[0] | maxBlockLight;
        this.vertexBrightness[1] = this.vertexBrightness[1] | maxBlockLight;
        this.vertexBrightness[2] = this.vertexBrightness[2] | maxBlockLight;
        this.vertexBrightness[3] = this.vertexBrightness[3] | maxBlockLight;
        this.vertexColorMultiplier[0] = 1.0F;
        this.vertexColorMultiplier[1] = 1.0F;
        this.vertexColorMultiplier[2] = 1.0F;
        this.vertexColorMultiplier[3] = 1.0F;
    }

    pub

    //TODO
    //MutableBlockPos blockpos$pooledmutableblockpos1 = this.blockPosArr[1].setPos(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[0]);
    //BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = PooledMutableBlockPos.retain(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[0]);


}
