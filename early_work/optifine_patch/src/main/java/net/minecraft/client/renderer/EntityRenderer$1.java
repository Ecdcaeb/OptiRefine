/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  java.lang.Object
 *  javax.annotation.Nullable
 *  net.minecraft.entity.Entity
 */
package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;

class EntityRenderer.1
implements Predicate<Entity> {
    EntityRenderer.1() {
    }

    public boolean apply(@Nullable Entity p_apply_1_) {
        return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
    }
}
