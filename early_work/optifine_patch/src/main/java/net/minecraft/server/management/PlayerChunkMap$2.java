/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  java.lang.Object
 *  javax.annotation.Nullable
 *  net.minecraft.entity.player.EntityPlayerMP
 */
package net.minecraft.server.management;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;

static final class PlayerChunkMap.2
implements Predicate<EntityPlayerMP> {
    PlayerChunkMap.2() {
    }

    public boolean apply(@Nullable EntityPlayerMP p_apply_1_) {
        return p_apply_1_ != null && (!p_apply_1_.isSpectator() || p_apply_1_.getServerWorld().W().getBoolean("spectatorsGenerateChunks"));
    }
}
