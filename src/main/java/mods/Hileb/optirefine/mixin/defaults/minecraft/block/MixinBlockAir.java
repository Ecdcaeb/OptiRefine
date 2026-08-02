package mods.Hileb.optirefine.mixin.defaults.minecraft.block;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;
import java.util.Map;
@Mixin(BlockAir.class)
public abstract class MixinBlockAir{
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


    @SuppressWarnings({"unchecked", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added static field mapOriginalOpacity (in OF BlockAir, not in baseline); private static + @Public per rules
    private static Map<Block, Integer> mapOriginalOpacity = new IdentityHashMap();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added static method setLightOpacity, body matches OF
    private static void setLightOpacity(Block block, int opacity) {
         if (!mapOriginalOpacity.containsKey(block)) {
             mapOriginalOpacity.put(block, block.lightOpacity);
         }
        block.lightOpacity = opacity;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added static method restoreLightOpacity, body matches OF
    private static void restoreLightOpacity(Block block) {
         if (!mapOriginalOpacity.containsKey(block)) {
             return;
         }
         int opacity = mapOriginalOpacity.get(block);
         setLightOpacity(block, opacity);
    }
}
