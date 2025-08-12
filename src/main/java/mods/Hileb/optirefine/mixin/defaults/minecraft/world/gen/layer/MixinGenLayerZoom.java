package mods.Hileb.optirefine.mixin.defaults.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerZoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GenLayerZoom.class)
public abstract class MixinGenLayerZoom extends GenLayer {
    @SuppressWarnings("unused")
    public MixinGenLayerZoom(long p_i2125_1_) {
        super(p_i2125_1_);
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
