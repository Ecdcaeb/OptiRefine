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

    public boolean apply(@Nullable EntityPlayerMP entityPlayerMP) {
        return entityPlayerMP != null && (!entityPlayerMP.isSpectator() || entityPlayerMP.getServerWorld().W().getBoolean("spectatorsGenerateChunks"));
    }

    public /* synthetic */ boolean apply(@Nullable Object object) {
        return this.apply((EntityPlayerMP)object);
    }
}
