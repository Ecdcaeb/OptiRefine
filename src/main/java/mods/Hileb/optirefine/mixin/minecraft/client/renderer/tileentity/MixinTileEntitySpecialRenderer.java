package mods.Hileb.optirefine.mixin.minecraft.client.renderer.tileentity;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Implements;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.IEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Implements(IEntityRenderer.class)
@Mixin(TileEntitySpecialRenderer.class)
public abstract class MixinTileEntitySpecialRenderer{
    @Unique
    private Class<?> tileEntityClass = null;
    @Unique
    private ResourceLocation locationTextureCustom = null;

    @Unique
    public Class<?> getEntityClass() {
        return this.tileEntityClass;
    }

    @Unique
    public void setEntityClass(Class<?> tileEntityClass) {
        this.tileEntityClass = tileEntityClass;
    }

    @Unique
    public ResourceLocation getLocationTextureCustom() {
        return this.locationTextureCustom;
    }

    @Unique
    public void setLocationTextureCustom(ResourceLocation locationTextureCustom) {
        this.locationTextureCustom = locationTextureCustom;
    }

}
