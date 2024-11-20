/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  java.lang.Comparable
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map$Entry
 *  javax.annotation.Nullable
 *  net.minecraft.block.properties.IProperty
 */
package net.minecraft.block.state;

import com.google.common.base.Function;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;

class BlockStateBase.1
implements Function<Map.Entry<IProperty<?>, Comparable<?>>, String> {
    BlockStateBase.1() {
    }

    @Nullable
    public String apply(@Nullable Map.Entry<IProperty<?>, Comparable<?>> p_apply_1_) {
        if (p_apply_1_ == null) {
            return "<NULL>";
        }
        IProperty iproperty = (IProperty)p_apply_1_.getKey();
        return iproperty.getName() + "=" + this.getPropertyName(iproperty, (Comparable)p_apply_1_.getValue());
    }

    private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> entry) {
        return property.getName(entry);
    }
}
