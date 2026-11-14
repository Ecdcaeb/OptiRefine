package mods.Hileb.optirefine.optifine.cem;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.optifine.expr.IExpressionFloat;

import java.util.UUID;

public enum CemRenderParameter implements IExpressionFloat {
    DAY_TIME("day_time", true),
    DAY_COUNT("day_count", true),
    FRAME_TIME("frame_time", true),
    FRAME_COUNTER("frame_counter", true),
    DIMENSION("dimension", true),
    PLAYER_POS_X("player_pos_x", true),
    PLAYER_POS_Y("player_pos_y", true),
    PLAYER_POS_Z("player_pos_z", true),
    PLAYER_ROT_X("player_rot_x", true),
    PLAYER_ROT_Y("player_rot_y", true),
    ROT_X("rot_x", false),
    ROT_Y("rot_y", false),
    ID("id", false),
    DEATH_TIME("death_time", false);

    private final String name;
    private final boolean blockEntity;
    private static final CemRenderParameter[] PARAMETERS = values();
    private static final float TO_RADIANS = (float) (Math.PI / 180.0);

    CemRenderParameter(String name, boolean blockEntity) {
        this.name = name;
        this.blockEntity = blockEntity;
    }

    public static CemRenderParameter resolve(String name, boolean tileEntity) {
        for (CemRenderParameter parameter : PARAMETERS) {
            if ((!tileEntity || parameter.blockEntity) && parameter.name.equals(name)) {
                return parameter;
            }
        }
        return null;
    }

    @Override
    public float eval() {
        float partial = CemRenderContext.partialTicks();
        switch (this) {
            case FRAME_TIME:
                return CemRenderContext.frameTime();
            case FRAME_COUNTER:
                return CemRenderContext.frameCounter();
            case DAY_TIME:
            case DAY_COUNT:
            case DIMENSION: {
                World world = CemRenderContext.world();
                if (world == null) {
                    return 0;
                }
                if (this == DAY_TIME) {
                    return world.getWorldTime() % 24000L + partial;
                }
                return this == DAY_COUNT ? world.getWorldTime() / 24000L : world.provider.getDimension();
            }
            case PLAYER_POS_X:
            case PLAYER_POS_Y:
            case PLAYER_POS_Z:
            case PLAYER_ROT_X:
            case PLAYER_ROT_Y: {
                Entity player = Minecraft.getMinecraft().player;
                if (player == null) {
                    return 0;
                }
                if (this == PLAYER_POS_X) {
                    return (float) (player.prevPosX + partial * (player.posX - player.prevPosX));
                }
                if (this == PLAYER_POS_Y) {
                    return (float) (player.prevPosY + partial * (player.posY - player.prevPosY));
                }
                if (this == PLAYER_POS_Z) {
                    return (float) (player.prevPosZ + partial * (player.posZ - player.prevPosZ));
                }
                return this == PLAYER_ROT_X
                        ? lerp(partial, player.prevRotationPitch, player.rotationPitch) * TO_RADIANS
                        : lerp(partial, player.prevRotationYaw, player.rotationYaw) * TO_RADIANS;
            }
            default:
                break;
        }
        Entity entity = CemRenderContext.entity();
        if (entity == null) {
            return 0;
        }
        switch (this) {
            case ROT_X:
                return lerp(partial, entity.prevRotationPitch, entity.rotationPitch) * TO_RADIANS;
            case ROT_Y:
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase living = (EntityLivingBase) entity;
                    return lerp(partial, living.prevRenderYawOffset, living.renderYawOffset) * TO_RADIANS;
                }
                return lerp(partial, entity.prevRotationYaw, entity.rotationYaw) * TO_RADIANS;
            case ID: {
                UUID id = entity.getUniqueID();
                return Math.abs(Long.hashCode(id.getLeastSignificantBits()) ^ Long.hashCode(id.getMostSignificantBits())) % 27720 + 1;
            }
            case DEATH_TIME:
                if (entity instanceof EntityLivingBase) {
                    int deathTime = ((EntityLivingBase) entity).deathTime;
                    return deathTime > 0 ? deathTime + partial : 0;
                }
                return 0;
            default:
                throw new IllegalStateException("Unhandled CEM parameter: " + this);
        }
    }

    private static float lerp(float partial, float from, float to) {
        return from + partial * (to - from);
    }
}
