/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.ItemMeshDefinition
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.item.ItemStack
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

class RenderItem.10
implements ItemMeshDefinition {
    RenderItem.10(RenderItem this$0) {
    }

    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return new ModelResourceLocation("filled_map", "inventory");
    }
}