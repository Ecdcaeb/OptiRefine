package mods.Hileb.optirefine.mixin.minecraft.client.renderer.vertex;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.Attributes;
import net.optifine.shaders.SVertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DefaultVertexFormats.class)
public abstract class MixinDefaultVertexFormats {
    @Shadow
    @SuppressWarnings("all")
    public static VertexFormat BLOCK;
    @Shadow
    @SuppressWarnings("all")
    public static VertexFormat ITEM;

    @AccessTransformer(name = "field_176600_a", deobf = true)
    @SuppressWarnings("all")
    public static VertexFormat ACC_BLOCK;

    @AccessTransformer(name = "field_176599_b", deobf = true)
    @SuppressWarnings("all")
    public static VertexFormat ACC_ITEM;

    @Unique
    private static final VertexFormat BLOCK_VANILLA = DefaultVertexFormats.BLOCK;
    @Unique
    private static final VertexFormat ITEM_VANILLA = DefaultVertexFormats.ITEM;
    @Unique
    private static final VertexFormat FORGE_BAKED = Attributes.DEFAULT_BAKED_FORMAT;

    @Unique @Public
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
