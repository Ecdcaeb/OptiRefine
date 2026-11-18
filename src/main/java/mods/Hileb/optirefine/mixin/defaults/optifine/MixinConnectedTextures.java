package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.resources.ConnectedTextureModes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.optifine.ConnectedProperties;
import net.optifine.ConnectedTextures;
import net.optifine.ConnectedTexturesCompact;
import net.optifine.render.RenderEnv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ConnectedTextures.class, remap = false)
public abstract class MixinConnectedTextures {
    @ModifyReturnValue(method = "isNeighbour(Lnet/optifine/ConnectedProperties;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;ILnet/minecraft/client/renderer/texture/TextureAtlasSprite;I)Z",
            at = @At("RETURN"))
    private static boolean optiRefine$matchNeighborState(boolean original, @Local(argsOnly = true) ConnectedProperties properties,
                                                        @Local(argsOnly = true, ordinal = 0) IBlockState state,
                                                        @Local(argsOnly = true, ordinal = 1) IBlockState neighbor) {
        if (properties.connect != ConnectedTextureModes.STATE) return original;
        if (state instanceof IExtendedBlockState extended) state = extended.getClean();
        if (neighbor instanceof IExtendedBlockState extended) neighbor = extended.getClean();
        return state.equals(neighbor);
    }

    @Shadow
    private static int getConnectedTextureCtmIndex(ConnectedProperties properties, IBlockAccess world, IBlockState state,
                                                   BlockPos pos, int axis, int side, TextureAtlasSprite sprite, int metadata,
                                                   RenderEnv env) {
        throw new AssertionError();
    }

    /**
     * @author OptiRefine
     * @reason Keep neighbor reads outside the shared baked-quad cache lock.
     */
    @Overwrite
    private static BakedQuad[] getConnectedTextureCtmCompact(ConnectedProperties properties, IBlockAccess world,
                                                            IBlockState state, BlockPos pos, int axis, int side,
                                                            BakedQuad quad, int metadata, RenderEnv env) {
        int index = getConnectedTextureCtmIndex(properties, world, state, pos, axis, side, quad.getSprite(), metadata, env);
        synchronized (ConnectedTextures.class) {
            return ConnectedTexturesCompact.getConnectedTextureCtmCompact(index, properties, side, quad, env);
        }
    }
}
