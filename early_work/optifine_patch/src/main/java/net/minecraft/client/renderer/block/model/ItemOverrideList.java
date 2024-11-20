/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Lists
 *  java.lang.Object
 *  java.util.List
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ItemOverride
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 *  net.optifine.ItemOverrideCache
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.ItemOverrideCache;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;

public class ItemOverrideList {
    public static final ItemOverrideList NONE = new ItemOverrideList();
    private final List<ItemOverride> overrides = Lists.newArrayList();
    private ItemOverrideCache itemOverrideCache;

    private ItemOverrideList() {
    }

    public ItemOverrideList(List<ItemOverride> overridesIn) {
        for (int i = overridesIn.size() - 1; i >= 0; --i) {
            this.overrides.add(overridesIn.get(i));
        }
        if (this.overrides.size() > 65) {
            this.itemOverrideCache = ItemOverrideCache.make(this.overrides);
        }
    }

    @Nullable
    public ResourceLocation applyOverride(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
        if (!this.overrides.isEmpty()) {
            ResourceLocation locationCached;
            if (this.itemOverrideCache != null && (locationCached = this.itemOverrideCache.getModelLocation(stack, worldIn, entityIn)) != null) {
                return locationCached == ItemOverrideCache.LOCATION_NULL ? null : locationCached;
            }
            for (ItemOverride itemoverride : this.overrides) {
                if (!itemoverride.matchesItemStack(stack, worldIn, entityIn)) continue;
                if (this.itemOverrideCache != null) {
                    this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, itemoverride.getLocation());
                }
                return itemoverride.getLocation();
            }
            if (this.itemOverrideCache != null) {
                this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, ItemOverrideCache.LOCATION_NULL);
            }
        }
        return null;
    }

    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
        ResourceLocation location;
        if (!stack.isEmpty() && stack.getItem().hasCustomProperties() && (location = this.applyOverride(stack, world, entity)) != null && Reflector.ModelLoader_getInventoryVariant.exists()) {
            ModelResourceLocation locationVariant = (ModelResourceLocation)Reflector.call((ReflectorMethod)Reflector.ModelLoader_getInventoryVariant, (Object[])new Object[]{location.toString()});
            return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(locationVariant);
        }
        return originalModel;
    }

    public ImmutableList<ItemOverride> getOverrides() {
        return ImmutableList.copyOf(this.overrides);
    }
}
