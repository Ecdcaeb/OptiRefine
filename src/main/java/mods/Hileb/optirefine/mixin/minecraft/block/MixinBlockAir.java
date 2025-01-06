package mods.Hileb.optirefine.mixin.minecraft.block;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(BlockAir.class)
public abstract class MixinBlockAir{

    @Unique
    @SuppressWarnings("all")
    @Public
    public static Map<Block, Integer> mapOriginalOpacity = new IdentityHashMap();

    @Unique
    @SuppressWarnings("all")
    @Public
    public static void setLightOpacity(Block block, int opacity) {
         if (!mapOriginalOpacity.containsKey(block)) {
             mapOriginalOpacity.put(block, ((BlockAccessor)block).getLightOpacity());
         }
        ((BlockAccessor)block).setLightOpacity(opacity);
    }

    @Unique
    @SuppressWarnings("all")
    @Public
    public static void restoreLightOpacity(Block block) {
         if (!mapOriginalOpacity.containsKey(block)) {
             return;
         }
         int opacity = mapOriginalOpacity.get(block);
         setLightOpacity(block, opacity);
    }
}
