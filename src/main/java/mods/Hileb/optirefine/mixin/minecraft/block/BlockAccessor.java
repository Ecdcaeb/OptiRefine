package mods.Hileb.optirefine.mixin.minecraft.block;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface BlockAccessor {
    @Accessor
    int getLightOpacity();

    @Accessor("lightOpacity")
    void setLightOpacity(int value);
}
