package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.*;

@SuppressWarnings("MissingUnique")
@Mixin(BufferBuilder.State.class)
public abstract class MixinBufferBuilderState {
    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    private int[] stateRawBuffer;

    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    private VertexFormat stateVertexFormat;

    @SuppressWarnings({"unused", "FieldCanBeLocal", "AddedMixinMembersNamePattern"})
    @Unique
    private TextureAtlasSprite[] stateQuadSprites; // How to get it???

    @SuppressWarnings({"unused", "MissingUnique", "AddedMixinMembersNamePattern"})
    @NewConstructor
    
    public void State(int[] buffer, VertexFormat format, TextureAtlasSprite[] quadSprites) {
        this.stateRawBuffer = buffer;
        this.stateVertexFormat = format;
        this.stateQuadSprites = quadSprites;
    }


}
