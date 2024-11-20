/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  java.lang.Object
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.util.ResourceLocation
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

class ModelBakery.1
implements Predicate<ModelResourceLocation> {
    final /* synthetic */ ResourceLocation val$resourcelocation;

    ModelBakery.1() {
        this.val$resourcelocation = resourceLocation;
    }

    public boolean apply(@Nullable ModelResourceLocation p_apply_1_) {
        return this.val$resourcelocation.equals((Object)p_apply_1_);
    }
}
