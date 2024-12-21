package mods.Hileb.optirefine.mixin.minecraft.client.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {

    @Unique
    private ResourceLocation locationOfCape = null;

    @Unique
    private long reloadCapeTimeMs = 0L;

    @Unique
    private boolean elytraOfCape = false;

    @Unique
    private String nameClear = null;

    @Unique
    public EntityShoulderRiding entityShoulderLeft;

    @Unique
    public EntityShoulderRiding entityShoulderRight;

}
