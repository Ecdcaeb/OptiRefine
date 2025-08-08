package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.ShadowSuperConstructor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.*;

@SuppressWarnings("MissingUnique")
@Mixin(BufferBuilder.State.class)
public abstract class MixinBufferBuilderState {

    @Mutable @Final
    @Shadow(remap = false)
    private BufferBuilder this$0;

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
    public void State(BufferBuilder bufferBuilder, int[] buffer, VertexFormat format, TextureAtlasSprite[] quadSprites) {
        _Object();
        this.this$0 = bufferBuilder;
        this.stateRawBuffer = buffer;
        this.stateVertexFormat = format;
        this.stateQuadSprites = quadSprites;
    }

    @ShadowSuperConstructor
    public void _Object(){}


}
