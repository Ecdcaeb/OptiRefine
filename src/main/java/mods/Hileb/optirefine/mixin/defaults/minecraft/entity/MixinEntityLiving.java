package mods.Hileb.optirefine.mixin.defaults.minecraft.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends EntityLivingBase {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private UUID teamUuid = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private String teamUuidString = null;

    @SuppressWarnings("unused")
    public MixinEntityLiving(World p_i1594_1_) {
        super(p_i1594_1_);
    }

    @WrapMethod(method = "onUpdate")
    public void injectOnUpdate(Operation<Void> original){
        if (Config.isSmoothWorld() && this.canSkipUpdate()) {
            this.onUpdateMinimal();
        } else {
            original.call();
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean canSkipUpdate() {
        if (this.isChild()) {
            return false;
        } else if (this.hurtTime > 0) {
            return false;
        } else if (this.ticksExisted < 20) {
            return false;
        } else {
            World world = this.getEntityWorld();
            if (world.playerEntities.size() != 1) {
                return false;
            } else {
                Entity player = world.playerEntities.getFirst();
                double dx = Math.max(Math.abs(this.posX - player.posX) - 16.0, 0.0);
                double dz = Math.max(Math.abs(this.posZ - player.posZ) - 16.0, 0.0);
                double distSq = dx * dx + dz * dz;
                return !this.isInRangeToRenderDist(distSq);
            }
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private void onUpdateMinimal() {
        ++this.idleTime;
        if (_cast_EntityLiving() instanceof EntityMob && this.getBrightness() > 0.5f) {
            this.idleTime += 2;
        }
        this.despawnEntity();
    }

    @Shadow
    protected abstract void despawnEntity();

    @Override
    public Team getTeam() {
        UUID uuid = _cast_EntityLiving().getUniqueID();
        if (this.teamUuid != uuid) {
            this.teamUuid = uuid;
            this.teamUuidString = String.valueOf(uuid);
        }

        return this.world.getScoreboard().getPlayersTeam(this.teamUuidString);
    }

    @Unique
    @AccessibleOperation
    private EntityLiving _cast_EntityLiving() {throw new AbstractMethodError();}


}
