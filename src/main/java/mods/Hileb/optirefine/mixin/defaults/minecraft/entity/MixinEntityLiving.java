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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;
@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends EntityLivingBase {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added fields teamUuid/teamUuidString (in OF EntityLiving, not in baseline), MCP names match
    @Unique
    private UUID teamUuid;
    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    private String teamUuidString;

    @SuppressWarnings("unused")
    public MixinEntityLiving(World p_i1594_1_) {
        super(p_i1594_1_);
    }

    @WrapMethod(method = "onUpdate")
// [AUDIT-OK] onUpdate wrap matches OF (skip -> onUpdateMinimal, else full vanilla body incl. super.onUpdate())
    public void injectOnUpdate(Operation<Void> original){
        if (Config.isSmoothWorld() && this.canSkipUpdate()) {
            this.onUpdateMinimal();
        } else {
            original.call();
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF null-world guard present; getFirst() == get(0) on JDK26 List (2026-08-05)
    private boolean canSkipUpdate() {
        if (this.isChild()) {
            return false;
        } else if (this.hurtTime > 0) {
            return false;
        } else if (this.ticksExisted < 20) {
            return false;
        } else {
            World world = this.getEntityWorld();
            if (world == null) {
                return false;
            }
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
// [AUDIT-OK] matches OF onUpdateMinimal (idleTime++, EntityMob brightness >0.5F -> idleTime+=2, despawnEntity)
    private void onUpdateMinimal() {
        ++this.idleTime;
        if (_cast_EntityLiving() instanceof EntityMob && this.getBrightness() > 0.5f) {
            this.idleTime += 2;
        }
        this.despawnEntity();
    }

    @Shadow
// [AUDIT-OK] baseline member despawnEntity exists in target class
    protected abstract void despawnEntity();

    @Override
// [AUDIT-OK] OF-added override (target EntityLiving does not declare getTeam - inherited from Entity - so plain merge applies), body matches OF uuid-caching
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
// [AUDIT-OK] NOP AccessibleOperation = self-cast idiom (call removed by processor, receiver "this" stays on stack); works because mixin instance IS the EntityLiving
    private EntityLiving _cast_EntityLiving() {throw new AbstractMethodError();}



    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.teamUuid = null;
        this.teamUuidString = null;
    }
}
