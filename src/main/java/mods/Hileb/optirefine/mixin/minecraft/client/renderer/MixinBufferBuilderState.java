package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BufferBuilder.State.class)
public abstract class MixinBufferBuilderState {
    @SuppressWarnings("unused")
    @Shadow @Final
    private int[] stateRawBuffer;

    @SuppressWarnings("unused")
    @Shadow @Final
    private VertexFormat stateVertexFormat;

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    @Unique
    private TextureAtlasSprite[] stateQuadSprites; // How to get it???

    @SuppressWarnings("unused")
    @NewConstructor
    
    public void State(int[] buffer, VertexFormat format, TextureAtlasSprite[] quadSprites) {
        this.stateRawBuffer = buffer;
        this.stateVertexFormat = format;
        this.stateQuadSprites = quadSprites;
    }


}
