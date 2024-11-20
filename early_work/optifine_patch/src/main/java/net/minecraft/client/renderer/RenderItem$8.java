/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.ItemMeshDefinition
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.item.ItemStack
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

class RenderItem.8
implements ItemMeshDefinition {
    RenderItem.8() {
    }

    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return new ModelResourceLocation("shield", "inventory");
    }
}
