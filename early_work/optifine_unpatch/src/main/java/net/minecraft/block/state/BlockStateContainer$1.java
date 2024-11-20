/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  java.lang.Object
 *  java.lang.String
 *  javax.annotation.Nullable
 *  net.minecraft.block.properties.IProperty
 */
package net.minecraft.block.state;

import com.google.common.base.Function;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;

class BlockStateContainer.1
implements Function<IProperty<?>, String> {
    BlockStateContainer.1() {
    }

    @Nullable
    public String apply(@Nullable IProperty<?> p_apply_1_) {
        return p_apply_1_ == null ? "<NULL>" : p_apply_1_.getName();
    }
}
