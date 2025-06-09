package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BufferBuilder.State.class)
public abstract class MixinBufferBuilderState {
    @Shadow
    @SuppressWarnings("all")
    private int[] stateRawBuffer;
    @Shadow
    @SuppressWarnings("all")
    private VertexFormat stateVertexFormat;

    @Unique
    private TextureAtlasSprite[] stateQuadSprites; // How to get it???

    @NewConstructor
    @SuppressWarnings("all")
    public void State(int[] buffer, VertexFormat format, TextureAtlasSprite[] quadSprites) {
        this.stateRawBuffer = buffer;
        this.stateVertexFormat = format;
        this.stateQuadSprites = quadSprites;
    }


}
