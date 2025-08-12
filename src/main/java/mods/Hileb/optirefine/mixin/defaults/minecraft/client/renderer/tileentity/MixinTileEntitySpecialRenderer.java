package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Implements;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.IEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Implements(IEntityRenderer.class)
@Mixin(TileEntitySpecialRenderer.class)
public abstract class MixinTileEntitySpecialRenderer{
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private Class<?> tileEntityClass = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private ResourceLocation locationTextureCustom = null;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public Class<?> getEntityClass() {
        return this.tileEntityClass;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void setEntityClass(Class<?> tileEntityClass) {
        this.tileEntityClass = tileEntityClass;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public ResourceLocation getLocationTextureCustom() {
        return this.locationTextureCustom;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void setLocationTextureCustom(ResourceLocation locationTextureCustom) {
        this.locationTextureCustom = locationTextureCustom;
    }

}
