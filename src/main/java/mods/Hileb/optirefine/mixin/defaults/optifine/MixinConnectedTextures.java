package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.ConnectedProperties;
import net.optifine.ConnectedTextures;
import net.optifine.ConnectedTexturesCompact;
import net.optifine.render.RenderEnv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ConnectedTextures.class, remap = false)
public abstract class MixinConnectedTextures {
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
