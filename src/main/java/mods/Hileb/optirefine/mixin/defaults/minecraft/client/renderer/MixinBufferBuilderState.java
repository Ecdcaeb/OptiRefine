package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.ShadowSuperConstructor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.*;
@SuppressWarnings("MissingUnique")
@Mixin(BufferBuilder.State.class)
public abstract class MixinBufferBuilderState {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 1

    @Mutable @Final
    @Shadow(remap = false)
// [AUDIT-OK] synthetic outer-class field this$0, remap=false correct (synthetic fields keep name at SRG runtime)
    private BufferBuilder this$0;

    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
// [AUDIT-OK] baseline member stateRawBuffer (SRG field_179019_b)
    private int[] stateRawBuffer;

    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
// [AUDIT-OK] baseline member stateVertexFormat (SRG field_179018_e)
    private VertexFormat stateVertexFormat;

    @SuppressWarnings({"unused", "FieldCanBeLocal", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-ISSUE] @Unique private field read cross-class by MixinBufferBuilder BufferBuilder$State_stateQuadSprites_get (GETFIELD from BufferBuilder) -> IllegalAccessError risk; make @Public
    @Public
    private TextureAtlasSprite[] stateQuadSprites;

    @SuppressWarnings({"unused", "MissingUnique", "AddedMixinMembersNamePattern"})
    @NewConstructor
    public void State(BufferBuilder bufferBuilder, int[] buffer, VertexFormat format, TextureAtlasSprite[] quadSprites) {
// [AUDIT-OK] OF-added ctor State(LBufferBuilder;[ILVertexFormat;[LTextureAtlasSprite;)V matching OF; note MixinBufferBuilder NEW AccessibleOperation desc must match this arity (see its issue)
        _Object();
        this.this$0 = bufferBuilder;
        this.stateRawBuffer = buffer;
        this.stateVertexFormat = format;
        this.stateQuadSprites = quadSprites;
    }

    @ShadowSuperConstructor
    public void _Object(){}


}
