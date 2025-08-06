package mods.Hileb.optirefine.mixin.minecraft.block;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Mixin(BlockAir.class)
public abstract class MixinBlockAir{

    @SuppressWarnings({"unchecked", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private static Map<Block, Integer> mapOriginalOpacity = new IdentityHashMap();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private static void setLightOpacity(Block block, int opacity) {
         if (!mapOriginalOpacity.containsKey(block)) {
             mapOriginalOpacity.put(block, block.lightOpacity);
         }
        block.lightOpacity = opacity;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    
    @Public
    private static void restoreLightOpacity(Block block) {
         if (!mapOriginalOpacity.containsKey(block)) {
             return;
         }
         int opacity = mapOriginalOpacity.get(block);
         setLightOpacity(block, opacity);
    }
}
