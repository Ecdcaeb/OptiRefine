package mods.Hileb.optirefine.mixin.defaults.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerZoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(GenLayerZoom.class)
public abstract class MixinGenLayerZoom extends GenLayer {
    @SuppressWarnings("unused")
    public MixinGenLayerZoom(long p_i2125_1_) {
        super(p_i2125_1_);
    }

    @Redirect(method = "getInts", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/layer/GenLayerZoom;selectRandom([I)I"))
    private int getInts$UseSelectRandom2(GenLayerZoom instance, int[] ints){
        return selectRandom2(ints[0], ints[1]);
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    protected int selectRandom2(int i0, int i1) {
        int index = this.nextInt(2);
        if (index == 0) {
            return i0;
        }
        return i1;
    }
}
