package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.vertex;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.Attributes;
import net.optifine.shaders.SVertexFormat;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
@Mixin(DefaultVertexFormats.class)
public abstract class MixinDefaultVertexFormats {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1
    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    @Public
    // [AUDIT-OK] baseline static final BLOCK/ITEM (public, tsrg field_176600_a/field_176599_b); @Mutable + @Public handle OF's final removal
    private static VertexFormat BLOCK;

    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    @Public
    private static VertexFormat ITEM;

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_176600_a", deobf = true, access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
    // [AUDIT-OK] AT SRG names field_176600_a=BLOCK / field_176599_b=ITEM + deobf=true; explicit PUBLIC|STATIC access drops final — correct
    private static VertexFormat ACC_BLOCK;

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_176599_b", deobf = true, access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
    private static VertexFormat ACC_ITEM;

    @Unique
    // [AUDIT-ISSUE] FORGE_BAKED = Attributes.DEFAULT_BAKED_FORMAT is a LIVE reference, OF uses SVertexFormat.duplicate(...) snapshot (OF:17); else-branch SVertexFormat.copy(FORGE_BAKED, DEFAULT_BAKED_FORMAT) is then a self-copy no-op, so the shaders-off restore of DEFAULT_BAKED_FORMAT is lost — needs-verification of SVertexFormat.setDefBakedFormat/copy semantics
    private static final VertexFormat BLOCK_VANILLA = DefaultVertexFormats.BLOCK;
    @Unique
    private static final VertexFormat ITEM_VANILLA = DefaultVertexFormats.ITEM;
    @Unique
    private static final VertexFormat FORGE_BAKED = SVertexFormat.duplicate(Attributes.DEFAULT_BAKED_FORMAT);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    // [AUDIT-OK] OF-added static (OF:35); @Public private static correct; shader/vanilla branches match OF:35-50
    private static void updateVertexFormats() {
        if (Config.isShaders()) {
            BLOCK = SVertexFormat.makeDefVertexFormatBlock();
            ITEM = SVertexFormat.makeDefVertexFormatItem();
            SVertexFormat.setDefBakedFormat(Attributes.DEFAULT_BAKED_FORMAT);
        } else {
            BLOCK = BLOCK_VANILLA;
            ITEM = ITEM_VANILLA;
            SVertexFormat.copy(FORGE_BAKED, Attributes.DEFAULT_BAKED_FORMAT);
        }
    }
}
